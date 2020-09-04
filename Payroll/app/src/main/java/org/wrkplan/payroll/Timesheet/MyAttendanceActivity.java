package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDutyLog.CustomOdDutyLogListAdapter;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<TimesheetMyAttendanceModel> timesheetMyAttendanceModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    RelativeLayout rl_button, rl_out, rl_in;
    TextView tv_button_subordinate, tv_nodata, tv_in, tv_out, tv_time_in, tv_time_out, tv_date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);

        rl_button = findViewById(R.id.rl_button);
        tv_button_subordinate = findViewById(R.id.tv_button_subordinate);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);

        tv_time_in = findViewById(R.id.tv_time_in);
        tv_time_out = findViewById(R.id.tv_time_out);
        tv_in = findViewById(R.id.tv_in);
        tv_out = findViewById(R.id.tv_out);
        tv_date = findViewById(R.id.tv_date);
        rl_in = findViewById(R.id.rl_in);
        rl_out = findViewById(R.id.rl_out);

        rl_button.setOnClickListener(this);
        tv_button_subordinate.setOnClickListener(this);
        tv_in.setOnClickListener(this);
        tv_out.setOnClickListener(this);

        //----default making tv_in, tv_out visibility gone
        tv_in.setVisibility(View.INVISIBLE);
        tv_out.setVisibility(View.GONE);
        rl_in.setVisibility(View.INVISIBLE);
        rl_out.setVisibility(View.GONE);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        //=========get current date and set curretnt date, code starts========
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        tv_date.setText(formattedDate);
        //=========get current date and set curretnt date, code ends========

        loadData();
        load_data_check_od_duty();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_button:
                startActivity(new Intent(this, SubordinateAttendanceActivity.class));
                break;
            case R.id.tv_button_subordinate:
                startActivity(new Intent(this, SubordinateAttendanceActivity.class));
                break;
            case R.id.tv_in:
                break;
            case R.id.tv_out:
                break;
        }
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL+"timesheet/log/previous/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"timesheet/log/previous/EMC_NEW/42";
        Log.d("listurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(MyAttendanceActivity.this, "Loading", "Please wait...", true, false);
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
            if(!timesheetMyAttendanceModelArrayList.isEmpty()){
                timesheetMyAttendanceModelArrayList.clear();
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
                    TimesheetMyAttendanceModel timesheetMyAttendanceModel = new TimesheetMyAttendanceModel();
                    timesheetMyAttendanceModel.setSlno(jsonObject2.getString("slno"));
                    timesheetMyAttendanceModel.setTs_date(jsonObject2.getString("ts_date"));
                    timesheetMyAttendanceModel.setTime_in(jsonObject2.getString("time_in"));
                    timesheetMyAttendanceModel.setTime_out(jsonObject2.getString("time_out"));
                    timesheetMyAttendanceModel.setWork_from_home(jsonObject2.getString("work_from_home"));
                    timesheetMyAttendanceModel.setAttendance_status(jsonObject2.getString("attendance_status"));

                    timesheetMyAttendanceModelArrayList.add(timesheetMyAttendanceModel);

                }
                recycler_view.setAdapter(new MyAttendanceListAdapter(MyAttendanceActivity.this, timesheetMyAttendanceModelArrayList));
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

    //-------added 0n 2nd sept
    //===========Code for getting time_in and time_out, starts==========
    public void load_data_check_od_duty(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"od/request/check-exist/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL+"timesheet/log/today/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"timesheet/log/today/EMC_NEW/42";
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(MyAttendanceActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get_today_time_in_out(response);
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
    public void get_today_time_in_out(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");

            if(jsonObject1.getString("status").contentEquals("true")){
              if(jsonObject.getString("timesheet_in_out_action").trim().contentEquals("IN")){
                  tv_in.setVisibility(View.VISIBLE);
                  tv_out.setVisibility(View.GONE);

                  rl_in.setVisibility(View.VISIBLE);
                  rl_out.setVisibility(View.GONE);
              }else if(jsonObject.getString("timesheet_in_out_action").trim().contentEquals("OUT")){
                  tv_in.setVisibility(View.GONE);
                  tv_out.setVisibility(View.VISIBLE);

                  rl_in.setVisibility(View.GONE);
                  rl_out.setVisibility(View.VISIBLE);
              }else {
                  tv_in.setVisibility(View.GONE);
                  tv_out.setVisibility(View.GONE);

                  rl_in.setVisibility(View.GONE);
                  rl_out.setVisibility(View.GONE);
              }

              tv_time_in.setText(jsonObject.getString("time_in"));
              tv_time_out.setText(jsonObject.getString("time_out"));
            }else if(jsonObject.getString("status").contentEquals("false")){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code for getting time_in and time_out, ends==========
}
