package org.wrkplan.payroll.AdvanceRequisition;

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
import org.wrkplan.payroll.Model.Requisition_Model;
import org.wrkplan.payroll.Model.Subordinate_Requsition_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class AdvanceRequisitionActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    RelativeLayout rl_btn_new,btn_subadv_req;
    LinearLayout ll_recycler,ll_1recycler;
    RecyclerView requi_recycler_view,requi_subordinate_recycler_view;
    TextView tv_button_subordinate_requisition,tv_requisition_title;
    EditText ed_search;


    //Custom Adapter
    public  static  CustomRequisitionListAdapter adapter;
    public static   CustomSubordinateRequisitionListAdapter subadapter;
    //Arraylist
    ArrayList<Requisition_Model> requisitionlist=new ArrayList<>();
    ArrayList<Subordinate_Requsition_Model>subordinate_requsition_modelsList=new ArrayList<>();

    ImageView img_back;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if(Url.isSubordinateRequisition==true)
        {

            rl_btn_new.setVisibility(View.GONE);
            btn_subadv_req.setVisibility(View.GONE);
            ll_recycler.setVisibility(View.VISIBLE);
            ll_1recycler.setVisibility(View.GONE);

            Intent intent=new Intent(AdvanceRequisitionActivity.this,AdvanceRequisitionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(Url.isMyRequisition==true)
        {

            Url.isSubordinateRequisition=false;
            rl_btn_new.setVisibility(View.VISIBLE);
            btn_subadv_req.setVisibility(View.VISIBLE);
            ll_recycler.setVisibility(View.VISIBLE);
            ll_1recycler.setVisibility(View.GONE);

            Intent intent=new Intent(AdvanceRequisitionActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advance_requsation_activity);

        Url.isMyRequisition=true;

        // ........initialize views starts..........//

        img_back=findViewById(R.id.img_back);
        rl_btn_new=findViewById(R.id.rl_btn_new);
        btn_subadv_req=findViewById(R.id.btn_subadv_req);
        ll_recycler=findViewById(R.id.ll_recycler);
        ll_1recycler=findViewById(R.id.ll_1recycler);
        requi_recycler_view=findViewById(R.id.requi_recycler_view);
        requi_subordinate_recycler_view=findViewById(R.id.requi_subordinate_recycler_view);
        tv_button_subordinate_requisition=findViewById(R.id.tv_button_subordinate_requisition);
        tv_requisition_title=findViewById(R.id.tv_requisition_title);
        ed_search=findViewById(R.id.ed_search);
        tv_requisition_title.setText("My Advance Requisition List");

        img_back.setOnClickListener(this);




        // ........initialize views end..........//

        //==========Recycler code initializing and setting layoutManager starts======

        requi_recycler_view.setHasFixedSize(true);
        requi_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        //==========Recycler code subordinate requisition initializing and setting layoutManager starts======
        requi_subordinate_recycler_view.setHasFixedSize(true);
        requi_subordinate_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======



        LoadData();

        rl_btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isNewEntry=true;
                Url.isMyRequisition=false;
                Url.isSubordinateRequisition=false;
                Intent i = new Intent(AdvanceRequisitionActivity.this, AdvanceRequisitionEntryActivity.class);
                startActivity(i);
            }
        });





        tv_button_subordinate_requisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_requisition_title.setText("Subordinate Advance Requisition List");
                ll_recycler.setVisibility(View.GONE);
                ll_1recycler.setVisibility(View.VISIBLE);
                btn_subadv_req.setVisibility(View.GONE);
                rl_btn_new.setVisibility(View.GONE);
                Url.isSubordinateRequisition=true;
                Url.isMyRequisition=false;

                LoadSubRequiData();
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

            }
        });

    }

    private void filter(String text) {
        ArrayList<Subordinate_Requsition_Model> filterlist=new ArrayList<>();
        for(Subordinate_Requsition_Model item:subordinate_requsition_modelsList)
        {
            if(item.getEmployee_name().toLowerCase().contains(text.toLowerCase()))
            {
                filterlist.add(item);
            }

        }

        subadapter.filltered(filterlist);
    }


    private void LoadSubRequiData() {
        String url=Url.BASEURL()+"advance-requisition/list/"+userSingletonModel.getCorporate_id()+"/Supervisor/"+userSingletonModel.getEmployee_id();
        Log.d("sub_requisitionList=>",url);
        final ProgressDialog loading = ProgressDialog.show(AdvanceRequisitionActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if(!subordinate_requsition_modelsList.isEmpty())
                    {
                        subordinate_requsition_modelsList.clear();
                    }

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("requisition_list");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);
                        Subordinate_Requsition_Model model=new Subordinate_Requsition_Model(
                                jb1.getString("approved_by_id"),
                                jb1.getString("approved_by_name"),
                                jb1.getString("approved_date"),
                                jb1.getString("approved_requisition_amount"),
                                jb1.getString("description"),
                                jb1.getString("employee_id"),
                                jb1.getString("employee_name"),
                                jb1.getString("reason"),
                                jb1.getString("requisition_amount"),
                                jb1.getString("requisition_date"),
                                jb1.getString("requisition_id"),
                                jb1.getString("requisition_no"),
                                jb1.getString("requisition_status"),
                                jb1.getString("requisition_type"),
                                jb1.getString("return_period_in_months"),
                                jb1.getString("supervisor1_id"),
                                jb1.getString("supervisor2_id"),
                                jb1.getString("supervisor_remark")
                        );

                        subordinate_requsition_modelsList.add(model);
                        subadapter=new CustomSubordinateRequisitionListAdapter(AdvanceRequisitionActivity.this,subordinate_requsition_modelsList);
                        requi_subordinate_recycler_view.setAdapter(subadapter);

                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(AdvanceRequisitionActivity.this, "could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(AdvanceRequisitionActivity.this).add(stringRequest);
    }

    private void LoadData() {

        String url= Url.BASEURL()+"advance-requisition/list/"+userSingletonModel.getCorporate_id()+"/Employee/"+userSingletonModel.getEmployee_id();
        Log.d("requisitionList=>",url);
        final ProgressDialog loading = ProgressDialog.show(AdvanceRequisitionActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if(!requisitionlist.isEmpty()){
                        requisitionlist.clear();
                    }
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("requisition_list");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);
                        Requisition_Model requisition_model=new Requisition_Model(
                                jb1.getString("approved_by_id"),
                                jb1.getString("approved_by_name"),
                                jb1.getString("approved_date"),
                                jb1.getString("approved_requisition_amount"),
                                jb1.getString("description"),
                                jb1.getString("employee_id"),
                                jb1.getString("employee_name"),
                                jb1.getString("requisition_amount"),
                                jb1.getString("requisition_date"),
                                jb1.getString("requisition_id"),
                                jb1.getString("requisition_no"),
                                jb1.getString("requisition_status"),
                                jb1.getString("requisition_type"),
                                jb1.getString("supervisor1_id"),
                                jb1.getString("supervisor2_id"),
                                jb1.getString("supervisor_remark"),
                                jb1.getString("reason"),
                                jb1.getString("return_period_in_months")

                        );

                        requisitionlist.add(requisition_model);
                        adapter=new CustomRequisitionListAdapter(AdvanceRequisitionActivity.this,requisitionlist);
                        requi_recycler_view.setAdapter(adapter);

                    }
                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(AdvanceRequisitionActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(AdvanceRequisitionActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(AdvanceRequisitionActivity.this).add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
