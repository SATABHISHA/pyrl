package org.wrkplan.payroll.Lta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.OutDoorListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.CustomOutdoorListAdapter;
import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDuty.OutdoorListActivity;
import org.wrkplan.payroll.OutDoorDuty.SubordinateOutdoorListActivity;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class LtaListActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<LTAModel> ltaModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata, tv_button_subordinate, tv_btn_new_rqst;
    RecyclerView recycler_view;
    public static int new_create_yn = 1;
    public static CustomLTAListActivityAdapter customLTAListActivityAdapter;
    public static String EmployeeType = "", mediclaim_status = "", lta_application_id = "", employee_id = "";
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_list);

        customLTAListActivityAdapter = new CustomLTAListActivityAdapter(this,ltaModelArrayList);

        img_back=findViewById(R.id.img_back);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_button_subordinate = findViewById(R.id.tv_button_subordinate);
        tv_btn_new_rqst = findViewById(R.id.tv_btn_new_rqst);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======


        loadData();
//        load_temp_data();

        tv_button_subordinate.setOnClickListener(this);
        tv_btn_new_rqst.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                Intent intent_home = new Intent(this, HomeActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_home);
                break;
            case R.id.tv_button_subordinate:
                new_create_yn = 0;
                Intent i = new Intent(LtaListActivity.this, SubordinateLtaListActivity.class);
                startActivity(i);
                EmployeeType = "Subordinate";
                break;
            case R.id.tv_btn_new_rqst:
                new_create_yn = 1;
                Intent i1 = new Intent(LtaListActivity.this, LtaRequestActivity.class);
                startActivity(i1);
                mediclaim_status = "";
                EmployeeType = "Employee";
                break;
            default:
                break;
        }
    }

    /*public void load_temp_data(){
        if(!ltaModelArrayList.isEmpty()){
            ltaModelArrayList.clear();
        }
        ll_recycler.setVisibility(View.VISIBLE);
        tv_nodata.setVisibility(View.GONE);

        LTAModel ltaModel = new LTAModel();
        ltaModel.setLta_status("Saved");
        ltaModel.setLta_amount("15000.00");
        ltaModel.setLta_date("14-April-2021");
        ltaModel.setLta_no("kjd/lohjfjfh");
        ltaModelArrayList.add(ltaModel);

        LTAModel ltaModel1 = new LTAModel();
        ltaModel1.setLta_status("Approved");
        ltaModel1.setLta_amount("10000.00");
        ltaModel1.setLta_date("12-April-2021");
        ltaModel1.setLta_no("jkj/lohjfjfh");
        ltaModelArrayList.add(ltaModel1);

        recycler_view.setAdapter(customLTAListActivityAdapter);
    }*/
    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
        String fin_year_id;
        if (userSingletonModel.getFin_year_id().contains("null")) {
            fin_year_id = "0";
        }else{
            fin_year_id = userSingletonModel.getFin_year_id();
        }
        String url = Url.BASEURL()+"lta/list/"+userSingletonModel.getCorporate_id()+"/employee/"+userSingletonModel.getEmployee_id()+"/"+userSingletonModel.getBranch_office_id()+"/0/"+fin_year_id;
        Log.d("listurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(LtaListActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponseData(response);
                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void getResponseData(String response){
        try {
            if(!ltaModelArrayList.isEmpty()){
                ltaModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("lta_list");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    LTAModel ltaModel = new LTAModel();
                    ltaModel.setLta_application_id(jsonObject2.getString("lta_application_id"));
                    ltaModel.setLta_application_no(jsonObject2.getString("lta_application_no"));
                    ltaModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    ltaModel.setDate_from(jsonObject2.getString("date_from"));
                    ltaModel.setEmployee_name(jsonObject2.getString("employee_name"));
                    ltaModel.setDate_to(jsonObject2.getString("date_to"));
                    ltaModel.setLta_amount(String.valueOf(jsonObject2.getDouble("lta_amount")));
                    ltaModel.setLta_application_status(jsonObject2.getString("lta_application_status"));

                    ltaModelArrayList.add(ltaModel);

                }
//                recycler_view.setAdapter(new CustomOutdoorListAdapter(OutdoorListActivity.this, outdoorListActivityArrayList));
                recycler_view.setAdapter(customLTAListActivityAdapter);
            }else if(jsonObject1.getString("status").contentEquals("false")){
                ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========Code to get data from api and load data to recycler view, ends==========


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent_home = new Intent(this, HomeActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_home);
    }
}
