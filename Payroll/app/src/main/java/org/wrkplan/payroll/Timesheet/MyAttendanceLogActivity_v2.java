package org.wrkplan.payroll.Timesheet;

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
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v2;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;
import java.util.Date;

public class MyAttendanceLogActivity_v2 extends AppCompatActivity {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<TimesheetMyAttendanceModel_v2> timesheetMyAttendanceModel_v2ArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    TextView tv_nodata, tv_title;
    String month_name, year, month_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance_log_v2);

        tv_nodata = findViewById(R.id.tv_nodata);
        tv_title = findViewById(R.id.tv_title);
        ll_recycler = findViewById(R.id.ll_recycler);
        recycler_view = findViewById(R.id.recycler_view);


        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        month_name = (String)android.text.format.DateFormat.format("MMMM", new Date());
        month_number = (String)android.text.format.DateFormat.format("MM", new Date());
        year=(String)android.text.format.DateFormat.format("yyyy", new Date());
        tv_title.setText(month_name+", "+year);

        loadData();
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/monthly/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id()+"/"+month_number+"/"+year;
//        String url = Url.BASEURL+"timesheet/log/previous/EMC_NEW/42";
        Log.d("listurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(MyAttendanceLogActivity_v2.this, "Loading", "Please wait...", true, false);
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
            if(!timesheetMyAttendanceModel_v2ArrayList.isEmpty()){
                timesheetMyAttendanceModel_v2ArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("day_wise_logs");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    TimesheetMyAttendanceModel_v2 timesheetMyAttendanceModel_v2 = new TimesheetMyAttendanceModel_v2();
                    timesheetMyAttendanceModel_v2.setDay_no(jsonObject2.getString("day_no"));
                    timesheetMyAttendanceModel_v2.setDay_name(jsonObject2.getString("day_name"));
                    timesheetMyAttendanceModel_v2.setDate(jsonObject2.getString("date"));
                    timesheetMyAttendanceModel_v2.setTime_in(jsonObject2.getString("time_in"));
                    timesheetMyAttendanceModel_v2.setTime_out(jsonObject2.getString("time_out"));
                    timesheetMyAttendanceModel_v2.setAttendance_status(jsonObject2.getString("attendance_status"));
                    timesheetMyAttendanceModel_v2.setAttendance_color(jsonObject2.getString("attendance_color"));
                    timesheetMyAttendanceModel_v2.setMonth_no(jsonObject2.getString("month_no"));
                    timesheetMyAttendanceModel_v2.setMonth_name(jsonObject2.getString("month_name"));
                    timesheetMyAttendanceModel_v2.setYear(jsonObject2.getString("year"));

                    timesheetMyAttendanceModel_v2ArrayList.add(timesheetMyAttendanceModel_v2);

                }
                recycler_view.setAdapter(new MyAttendanceLogListAdapter_v2(MyAttendanceLogActivity_v2.this, timesheetMyAttendanceModel_v2ArrayList));
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
        super.onBackPressed();
        Intent intent_myattendence = new Intent(MyAttendanceLogActivity_v2.this, MyAttendanceActivity_v2.class);
        intent_myattendence.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_myattendence);
    }
}
