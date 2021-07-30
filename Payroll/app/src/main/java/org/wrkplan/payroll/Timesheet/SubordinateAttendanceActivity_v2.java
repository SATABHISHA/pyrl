package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import org.wrkplan.payroll.Model.TimesheetSubordinateModel;
import org.wrkplan.payroll.Model.TimesheetSubordinateMonthlyAttendanceModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubordinateAttendanceActivity_v2 extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList = new ArrayList<>();
    public static ArrayList<TimesheetSubordinateMonthlyAttendanceModel> timesheetSubordinateMonthlyAttendanceModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    RelativeLayout rl_button, rl_out, rl_in;
    TextView tv_button_subordinate, tv_nodata, tv_date;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_attendance_v2);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_date = findViewById(R.id.tv_date);
        tv_button_subordinate = findViewById(R.id.tv_button_subordinate);
        img_back=findViewById(R.id.img_back);

        //=========get current date and set curretnt date, code starts========
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        tv_date.setText(formattedDate);
        //=========get current date and set curretnt date, code ends========

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        tv_button_subordinate.setOnClickListener(this);
        img_back.setOnClickListener(this);

        loadData();
    }
    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/subordinate/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"timesheet/log/previous/EMC_NEW/42";
        Log.d("listurlsub-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(SubordinateAttendanceActivity_v2.this, "Loading", "Please wait...", true, false);
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
            if(!timesheetSubordinateModelArrayList.isEmpty()){
                timesheetSubordinateModelArrayList.clear();
            }

            if(!timesheetSubordinateMonthlyAttendanceModelArrayList.isEmpty()){
                timesheetSubordinateMonthlyAttendanceModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("subordinate_logs");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    TimesheetSubordinateModel timesheetSubordinateModel = new TimesheetSubordinateModel();
                    TimesheetSubordinateMonthlyAttendanceModel timesheetSubordinateMonthlyAttendanceModel = new TimesheetSubordinateMonthlyAttendanceModel();

                    timesheetSubordinateModel.setSlno(jsonObject2.getString("slno"));
//                    timesheetSubordinateMonthlyAttendanceModel.setSlno(jsonObject2.getString("slno"));
                    timesheetSubordinateMonthlyAttendanceModel.setSlno(jsonObject2.getString("employee_id")); //emp_id will be there in place of slno(previosuly there was no emp_id, that's why slno is being used)

                    timesheetSubordinateModel.setTs_date(jsonObject2.getString("ts_date"));
                    timesheetSubordinateModel.setTime_in(jsonObject2.getString("time_in"));
                    timesheetSubordinateModel.setTime_out(jsonObject2.getString("time_out"));

                    timesheetSubordinateModel.setEmployee_name(jsonObject2.getString("employee_name"));
                    timesheetSubordinateModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    timesheetSubordinateMonthlyAttendanceModel.setEmployee_name(jsonObject2.getString("employee_name"));

                    timesheetSubordinateModel.setAttendance_status(jsonObject2.getString("status"));

                    timesheetSubordinateModelArrayList.add(timesheetSubordinateModel);
                    timesheetSubordinateMonthlyAttendanceModelArrayList.add(timesheetSubordinateMonthlyAttendanceModel);

                }
                recycler_view.setAdapter(new SubordinateAttendanceListAdapterv2(SubordinateAttendanceActivity_v2.this, timesheetSubordinateModelArrayList));
                recycler_view.getRecycledViewPool().setMaxRecycledViews(0, 0); //--to protect from getting items hidden or something other
            }else if(jsonObject1.getString("status").contentEquals("false")){
                ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));
//                tv_nodata.setText("Subordinate timesheet data not found");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========Code to get data from api and load data to recycler view, ends==========


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_myattendence = new Intent(SubordinateAttendanceActivity_v2.this, MyAttendanceActivity_v3.class);
        intent_myattendence.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_myattendence);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                Intent intent_myattendence = new Intent(SubordinateAttendanceActivity_v2.this, MyAttendanceActivity_v3.class);
                intent_myattendence.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_myattendence);
                break;
            case R.id.tv_button_subordinate:
                Intent intent = new Intent(SubordinateAttendanceActivity_v2.this,SubordinateMonthlyAttendanceLog.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
