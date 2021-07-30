package org.wrkplan.payroll.OutDoorDutyLog;

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
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.CustomSubordinateOutdoorListAdapter;
import org.wrkplan.payroll.OutDoorDuty.SubordinateOutdoorListActivity;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class SubordinateOdDutyLogListActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<OutDoorLogListModel> outDoorLogListModelArrayList = new ArrayList<>();
    ArrayList<OutDoorLogListModel> filteredData = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata;
    RecyclerView recycler_view;
    EditText ed_search;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subordinate_od_duty_log_list_activity);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        ed_search = findViewById(R.id.ed_search);
        img_back=findViewById(R.id.img_back);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//             loadData(ed_search.getText().toString());
                display_filtered_data(ed_search.getText().toString());
            }
        });

        loadData();
        img_back.setOnClickListener(this);
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
        String url = Url.BASEURL()+"od/log/list/"+userSingletonModel.getCorporate_id()+"/2/"+userSingletonModel.getEmployee_id();
        Log.d("listurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(SubordinateOdDutyLogListActivity.this, "Loading", "Please wait...", true, false);
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
            if(!outDoorLogListModelArrayList.isEmpty()){
                outDoorLogListModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    OutDoorLogListModel outDoorLogListModel = new OutDoorLogListModel();
                    outDoorLogListModel.setOd_duty_log_date(jsonObject2.getString("od_duty_log_date"));
                    outDoorLogListModel.setOd_request_id(jsonObject2.getString("od_request_id"));
                    outDoorLogListModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    outDoorLogListModel.setEmployee_name(jsonObject2.getString("employee_name"));

                    outDoorLogListModelArrayList.add(outDoorLogListModel);

                }
                display_filtered_data("");
//                recycler_view.setAdapter(new CustomSubordinateOdDutyLogListAdapter(this, outDoorLogListModelArrayList));
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

    //===========code to filter data and display in list, code starts=======
    public void display_filtered_data(String employeename){
        if (!filteredData.isEmpty()){
            filteredData.clear();
        }
        if(outDoorLogListModelArrayList.size()>0) {
            for (int i = 0; i < outDoorLogListModelArrayList.size(); i++){
                if(outDoorLogListModelArrayList.get(i).getEmployee_name().toLowerCase().trim().contains(employeename.toLowerCase())){
                    OutDoorLogListModel outDoorLogListModel = new OutDoorLogListModel();
                    outDoorLogListModel.setOd_duty_log_date(outDoorLogListModelArrayList.get(i).getOd_duty_log_date());
                    outDoorLogListModel.setOd_request_id(outDoorLogListModelArrayList.get(i).getOd_request_id());
                    outDoorLogListModel.setEmployee_id(outDoorLogListModelArrayList.get(i).getEmployee_id());
                    outDoorLogListModel.setEmployee_name(outDoorLogListModelArrayList.get(i).getEmployee_name());

                    filteredData.add(outDoorLogListModel);

                }
            }
            recycler_view.setAdapter(new CustomSubordinateOdDutyLogListAdapter(SubordinateOdDutyLogListActivity.this, filteredData));
        }/*else{
            ll_recycler.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
            tv_nodata.setText(jsonObject1.getString("message"));
        }*/
    }
    //===========code to filter data and display in list, code ends=======

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent_od_duty_loglist = new Intent(this, OdDutyLogListActivity.class);
        intent_od_duty_loglist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_od_duty_loglist);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent_od_duty_loglist = new Intent(this, OdDutyLogListActivity.class);
                intent_od_duty_loglist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_od_duty_loglist);
                break;
        }
    }
}
