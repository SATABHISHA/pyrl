package org.wrkplan.payroll.Mediclaim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.Mediclaim.My_Mediclaim_Model;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Mediclaim_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class MediclaimActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    TextView tv_mediclaim_title,tv_button_subordinate_mediclaim, tv_nodata;
    RelativeLayout rl_btn_new;
    LinearLayout ll_recycler,ll_1recycler;
    RelativeLayout btn_subadv_mediclaim;
    EditText ed_search;
    ImageView img_search_logo;
    RecyclerView mediclaim_recycler_view,mediclaim_subordinate_recycler_view;

    //Custom Adapter
    public  static CustomMediclaimListAdapter customMediclaimListAdapter;
    public static  CustomSubordinateMediclaimListAdapter subordinateMediclaimListAdapter;
    //Arraylist
    ArrayList<My_Mediclaim_Model> mediclaim_modelArrayList=new ArrayList<>();
    ArrayList<Subordinate_Mediclaim_Model> subordinate_mediclaim_modelArrayList=new ArrayList<>();

    ImageView img_back;

    @Override
    public void onBackPressed() {
        if(Url.isSubordinateMediclaim==true)
        {

            rl_btn_new.setVisibility(View.GONE);
            btn_subadv_mediclaim.setVisibility(View.GONE);
            ll_recycler.setVisibility(View.VISIBLE);
            ll_1recycler.setVisibility(View.GONE);

            Intent intent=new Intent(MediclaimActivity.this,MediclaimActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(Url.isMyMediclaim==true)
        {

            Url.isSubordinateMediclaim=false;
            rl_btn_new.setVisibility(View.VISIBLE);
            btn_subadv_mediclaim.setVisibility(View.VISIBLE);
            ll_recycler.setVisibility(View.VISIBLE);
            ll_1recycler.setVisibility(View.GONE);

            Intent intent=new Intent(MediclaimActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediclaim_activity);


        //-----------intialize view start-------------//

        Url.isMyMediclaim=true;
        img_back=findViewById(R.id.img_back);
        tv_mediclaim_title=findViewById(R.id.tv_mediclaim_title);
        tv_button_subordinate_mediclaim=findViewById(R.id.tv_button_subordinate_mediclaim);
        tv_nodata=findViewById(R.id.tv_nodata);
        rl_btn_new=findViewById(R.id.rl_btn_new);
        ll_1recycler=findViewById(R.id.ll_1recycler);
        ll_recycler=findViewById(R.id.ll_recycler);
        btn_subadv_mediclaim=findViewById(R.id.btn_subadv_mediclaim);
        ed_search=findViewById(R.id.ed_search);
        img_search_logo=findViewById(R.id.img_search_logo);
        mediclaim_recycler_view=findViewById(R.id.mediclaim_recycler_view);
        mediclaim_subordinate_recycler_view=findViewById(R.id.mediclaim_subordinate_recycler_view);

        //-----------intialize view end-------------//
        tv_mediclaim_title.setText(" My Medical Reimb List");


        //==========Recycler code initializing and setting layoutManager starts======

        mediclaim_recycler_view.setHasFixedSize(true);
        mediclaim_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        //==========Recycler code subordinate requisition initializing and setting layoutManager starts======
        mediclaim_subordinate_recycler_view.setHasFixedSize(true);
        mediclaim_subordinate_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======


        tv_button_subordinate_mediclaim.setOnClickListener(this);
        rl_btn_new.setOnClickListener(this);
        img_back.setOnClickListener(this);

        loadMyMediclaimList();

//        tv_button_subordinate_mediclaim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_recycler.setVisibility(View.GONE);
//                ll_1recycler.setVisibility(View.VISIBLE);
//                btn_subadv_mediclaim.setVisibility(View.GONE);
//                tv_mediclaim_title.setText("Subordinate Mediclaim List");
//                rl_btn_new.setVisibility(View.GONE);
//            }
//        });

        //---------when press new Mediclaim Entry--------------//

//        rl_btn_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Url.isNewEntryMediclaim=true;
//                Url.isMyMediclaim=false;
//                Intent i = new Intent(MediclaimActivity.this, MediclaimEntryActivity.class);
//                startActivity(i);
//            }
//        });

    }

    private void loadMyMediclaimList() {
        String url= Url.BASEURL()+"mediclaim/list/"+userSingletonModel.getCorporate_id()+"/employee/"+userSingletonModel.getEmployee_id();
        Log.d("mediclaimList=>",url);
        final ProgressDialog loading = ProgressDialog.show(MediclaimActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if(!mediclaim_modelArrayList.isEmpty()){
                        mediclaim_modelArrayList.clear();
                    }
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObjectResponseStatus = jsonObject.getJSONObject("response");
                    if (jsonObjectResponseStatus.getString("status").contains("true")) {
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("mediclaim_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);
                            My_Mediclaim_Model model = new My_Mediclaim_Model(
                                    jb1.getString("approved_mediclaim_amount"),
                                    jb1.getString("employee_id"),
                                    jb1.getString("employee_name"),
                                    jb1.getString("mediclaim_amount"),
                                    jb1.getString("mediclaim_date"),
                                    jb1.getString("mediclaim_id"),
                                    jb1.getString("mediclaim_no"),
                                    jb1.getString("mediclaim_status"),
                                    jb1.getString("payment_amount")
                            );
                            mediclaim_modelArrayList.add(model);
                            customMediclaimListAdapter = new CustomMediclaimListAdapter(mediclaim_modelArrayList, MediclaimActivity.this);
                            mediclaim_recycler_view.setAdapter(customMediclaimListAdapter);
                        }
                        loading.dismiss();
                    }else if (jsonObjectResponseStatus.getString("status").contains("false")){
                        loading.dismiss();
                        ll_recycler.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(jsonObjectResponseStatus.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(MediclaimActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MediclaimActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MediclaimActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                if(Url.isSubordinateMediclaim==true)
                {

                    rl_btn_new.setVisibility(View.GONE);
                    btn_subadv_mediclaim.setVisibility(View.GONE);
                    ll_recycler.setVisibility(View.VISIBLE);
                    ll_1recycler.setVisibility(View.GONE);

                    Intent intent=new Intent(MediclaimActivity.this,MediclaimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if(Url.isMyMediclaim==true)
                {

                    Url.isSubordinateMediclaim=false;
                    rl_btn_new.setVisibility(View.VISIBLE);
                    btn_subadv_mediclaim.setVisibility(View.VISIBLE);
                    ll_recycler.setVisibility(View.VISIBLE);
                    ll_1recycler.setVisibility(View.GONE);

                    Intent intent=new Intent(MediclaimActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.tv_button_subordinate_mediclaim:
                ll_recycler.setVisibility(View.GONE);
                ll_1recycler.setVisibility(View.VISIBLE);
                btn_subadv_mediclaim.setVisibility(View.GONE);
                tv_mediclaim_title.setText("Subordinate Medical Reimb List");
                rl_btn_new.setVisibility(View.GONE);
                Url.isSubordinateMediclaim=true;
                Url.isMyMediclaim=false;
                LoadSubordinateMediclaim();

                ed_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        filter(s.toString());

                    }
                });
                break;
            case  R.id.rl_btn_new:
                Url.isNewEntryMediclaim=true;
                Url.isMyMediclaim=false;
                Url.isSubordinateMediclaim=false;
                Intent i = new Intent(MediclaimActivity.this, MediclaimEntryActivity.class);
                startActivity(i);
            default:
                break;
        }
    }

    private void filter(String text) {
        ArrayList<Subordinate_Mediclaim_Model> filterlist=new ArrayList<>();
        for(Subordinate_Mediclaim_Model item:subordinate_mediclaim_modelArrayList)
        {
            if(item.getEmployee_name().toLowerCase().contains(text.toLowerCase()))
            {
                filterlist.add(item);
            }

        }

        subordinateMediclaimListAdapter.filltered(filterlist);
    }

    private void LoadSubordinateMediclaim() {
        String url= Url.BASEURL()+"mediclaim/list/"+userSingletonModel.getCorporate_id()+"/subordinate/"+userSingletonModel.getEmployee_id();
        Log.d("submediclaimList=>",url);
        final ProgressDialog loading = ProgressDialog.show(MediclaimActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if(!mediclaim_modelArrayList.isEmpty()){
                        mediclaim_modelArrayList.clear();
                    }
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObjectResponseStatus = jsonObject.getJSONObject("response");
                    if (jsonObjectResponseStatus.getString("status").contains("true")) {
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("mediclaim_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);
                            Subordinate_Mediclaim_Model model = new Subordinate_Mediclaim_Model(
                                    jb1.getString("approved_mediclaim_amount"),
                                    jb1.getString("employee_id"),
                                    jb1.getString("employee_name"),
                                    jb1.getString("mediclaim_amount"),
                                    jb1.getString("mediclaim_date"),
                                    jb1.getString("mediclaim_id"),
                                    jb1.getString("mediclaim_no"),
                                    jb1.getString("mediclaim_status"),
                                    jb1.getString("payment_amount")
                            );
                            subordinate_mediclaim_modelArrayList.add(model);
                            subordinateMediclaimListAdapter = new CustomSubordinateMediclaimListAdapter(subordinate_mediclaim_modelArrayList, MediclaimActivity.this);
                            mediclaim_subordinate_recycler_view.setAdapter(subordinateMediclaimListAdapter);
                        }
                        loading.dismiss();

                    }else if (jsonObjectResponseStatus.getString("status").contains("false")){
                        loading.dismiss();
                        ll_recycler.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(jsonObjectResponseStatus.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(MediclaimActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MediclaimActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MediclaimActivity.this).add(stringRequest);
    }
}
