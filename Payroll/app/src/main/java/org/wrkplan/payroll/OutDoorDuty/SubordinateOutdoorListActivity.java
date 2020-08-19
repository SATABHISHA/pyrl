package org.wrkplan.payroll.OutDoorDuty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.wrkplan.payroll.Model.OutDoorListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class SubordinateOutdoorListActivity extends AppCompatActivity {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<OutDoorListModel> outdoorListActivityArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata;
    RecyclerView recycler_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_outdoor_list);

        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        loadData();
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/2/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/20";
        final ProgressDialog loading = ProgressDialog.show(SubordinateOutdoorListActivity.this, "Loading", "Please wait...", true, false);
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
            if(!outdoorListActivityArrayList.isEmpty()){
                outdoorListActivityArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("request_list");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    OutDoorListModel outDoorListModel = new OutDoorListModel();
                    outDoorListModel.setOd_request_no(jsonObject2.getString("od_request_no"));
                    outDoorListModel.setOd_request_id(jsonObject2.getString("od_request_id"));
                    outDoorListModel.setOd_request_date(jsonObject2.getString("od_request_date"));
                    outDoorListModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    outDoorListModel.setEmployee_name(jsonObject2.getString("employee_name"));
                    outDoorListModel.setFrom_date(jsonObject2.getString("from_date"));
                    outDoorListModel.setTo_date(jsonObject2.getString("to_date"));
                    outDoorListModel.setTotal_days(jsonObject2.getString("total_days"));
                    outDoorListModel.setDescription(jsonObject2.getString("description"));
                    outDoorListModel.setSupervisor_remark(jsonObject2.getString("supervisor_remark"));
                    outDoorListModel.setOd_status(jsonObject2.getString("od_status"));
                    outDoorListModel.setApproved_by_id(jsonObject2.getString("approved_by_id"));
                    outDoorListModel.setApproved_by_id(jsonObject2.getString("approved_by_id"));
                    outDoorListModel.setApproved_by_name(jsonObject2.getString("approved_by_name"));
                    outDoorListModel.setApproved_date(jsonObject2.getString("approved_date"));
                    outdoorListActivityArrayList.add(outDoorListModel);

                }
                recycler_view.setAdapter(new CustomSubordinateOutdoorListAdapter(SubordinateOutdoorListActivity.this, outdoorListActivityArrayList));
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
        Intent intent_odlist = new Intent(this,OutdoorListActivity.class);
        intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_odlist);
    }
}
