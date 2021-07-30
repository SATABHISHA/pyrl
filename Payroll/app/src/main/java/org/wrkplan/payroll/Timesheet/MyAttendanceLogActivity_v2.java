package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;

public class MyAttendanceLogActivity_v2 extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<TimesheetMyAttendanceModel_v2> timesheetMyAttendanceModel_v2ArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    TextView tv_nodata, tv_title;
    String month_name, year = "", month_number = "";
    ImageButton imgbtn_prev, imgbtn_next;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance_log_v2);

        img_back=findViewById(R.id.img_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_title = findViewById(R.id.tv_title);
        imgbtn_prev = findViewById(R.id.imgbtn_prev);
        imgbtn_next = findViewById(R.id.imgbtn_next);
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
//        tv_title.setText(month_name+", "+year);

        imgbtn_prev.setOnClickListener(this);
        imgbtn_next.setOnClickListener(this);
        img_back.setOnClickListener(this);

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
            tv_title.setText(jsonObject.getString("month_name")+", "+jsonObject.getString("year"));
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
                    if(jsonObject2.getString("time_in").trim().contentEquals("")){
                        timesheetMyAttendanceModel_v2.setTime_in("             ");
                    }else{
                        timesheetMyAttendanceModel_v2.setTime_in(jsonObject2.getString("time_in"));
                    }
                    if(jsonObject2.getString("time_out").trim().contentEquals("")){
                        timesheetMyAttendanceModel_v2.setTime_out("              ");
                    }else {
                        timesheetMyAttendanceModel_v2.setTime_out(jsonObject2.getString("time_out"));
                    }
                    timesheetMyAttendanceModel_v2.setAttendance_status(jsonObject2.getString("attendance_status"));
                    timesheetMyAttendanceModel_v2.setAttendance_color(jsonObject2.getString("attendance_color"));
//                    timesheetMyAttendanceModel_v2.setMonth_no(jsonObject2.getString("month_no"));
//                    timesheetMyAttendanceModel_v2.setMonth_name(jsonObject2.getString("month_name"));
//                    timesheetMyAttendanceModel_v2.setYear(jsonObject2.getString("year"));

                    timesheetMyAttendanceModel_v2ArrayList.add(timesheetMyAttendanceModel_v2);

                }
                recycler_view.setAdapter(new MyAttendanceLogListAdapter_v2(MyAttendanceLogActivity_v2.this, timesheetMyAttendanceModel_v2ArrayList));
                recycler_view.getRecycledViewPool().setMaxRecycledViews(0, 0); //--to protect from getting items hidden or something other
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
        Intent intent_myattendence = new Intent(MyAttendanceLogActivity_v2.this, MyAttendanceActivity_v3.class);
        intent_myattendence.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_myattendence);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                Intent intent_myattendence = new Intent(MyAttendanceLogActivity_v2.this, MyAttendanceActivity_v3.class);
                intent_myattendence.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_myattendence);
                break;
            case R.id.imgbtn_prev:
                int temp_month_no = Integer.parseInt(month_number);
                int temp_year = Integer.parseInt(year);
                if(temp_month_no<=1) {
                    temp_month_no = 12;
                    temp_year = temp_year - 1;
                }else{
                    temp_month_no = temp_month_no - 1;
                    temp_year = temp_year;
                }
                month_number = String.valueOf(temp_month_no);
                year = String.valueOf(temp_year);
//                Toast.makeText(getApplicationContext(),month_number+","+year,Toast.LENGTH_LONG).show();
//                month_name = (String)android.text.format.DateFormat.format("MMMM", temp_month_no);
                loadData();
                break;
            case R.id.imgbtn_next:
                int temp_month_no1 = Integer.parseInt(month_number);
                int temp_year1 = Integer.parseInt(year);
                if(temp_month_no1>=12) {
                    temp_month_no1 = 1;
                    temp_year1 = temp_year1 + 1;
                }else{
                    temp_month_no1 = temp_month_no1 + 1;
                    temp_year1 = temp_year1;
                }
                month_number = String.valueOf(temp_month_no1);
                year = String.valueOf(temp_year1);
//                Toast.makeText(getApplicationContext(),month_number+","+year,Toast.LENGTH_LONG).show();
//                month_name = (String)android.text.format.DateFormat.format("MMMM", temp_month_no);
                loadData();
                break;
            default:
                break;
        }
    }
}
