package org.wrkplan.payroll.Timesheet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDutyLog.CustomOdDutyLogListAdapter;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
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

    CheckBox chck_wrk_frm_home;
    EditText ed_wrk_frm_home_detail;
    public static int timesheet_id, work_from_home_flag;
    public static String timesheet_in_out_action, work_from_home_detail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);
//        setContentView(R.layout.activity_my_attendance1);

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

        chck_wrk_frm_home = findViewById(R.id.chck_wrk_frm_home);
        ed_wrk_frm_home_detail = findViewById(R.id.ed_wrk_frm_home_detail);

        rl_button.setOnClickListener(this);
        tv_button_subordinate.setOnClickListener(this);
        tv_in.setOnClickListener(this);
        tv_out.setOnClickListener(this);
        chck_wrk_frm_home.setOnClickListener(this);

        //----default making tv_in, tv_out visibility gone
//        tv_in.setVisibility(View.INVISIBLE);
        tv_in.setVisibility(View.GONE);
        tv_out.setVisibility(View.GONE);
//        rl_in.setVisibility(View.INVISIBLE);
        rl_in.setVisibility(View.GONE);
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
                save_in_out_data("IN", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance IN time recorded");
                break;
            case R.id.tv_out:
                if((work_from_home_flag == 1) && ed_wrk_frm_home_detail.getText().toString().trim().isEmpty()){

                    //---------Alert dialog code starts(added on 21st nov)--------
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Cannot save without work from home details");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //-----following code is commented on 6th dec to get the calender saved state data------
                                    alertDialogBuilder.setCancelable(true);
                                }
                            });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //--------Alert dialog code ends--------

                }else{
                    save_in_out_data("OUT", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance OUT time recorded");
                }
                break;
            case R.id.chck_wrk_frm_home:
                if(chck_wrk_frm_home.isChecked()) {
                    ed_wrk_frm_home_detail.setVisibility(View.VISIBLE);
                    work_from_home_flag = 1;
                }else{
                    ed_wrk_frm_home_detail.setVisibility(View.GONE);
                    work_from_home_flag = 0;
                }
                break;
        }
    }

    //========function to save data for IN/OUT, code starts=======
    public void save_in_out_data(String in_out, int work_frm_home_flag, String work_from_home_detail, String message_in_out){
           if(message_in_out.contentEquals("IN")){
               rl_in.setAlpha(0.5f);
               tv_in.setClickable(false);
           }else if(message_in_out.contentEquals("OUT")){
               rl_out.setAlpha(0.5f);
               tv_out.setClickable(false);
           }
        try {
            final JSONObject DocumentElementobj = new JSONObject();
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("timesheet_id", timesheet_id);
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("in_out_action", in_out);
            DocumentElementobj.put("work_from_home_flag", work_frm_home_flag);
            DocumentElementobj.put("work_from_home_detail", work_from_home_detail);

            Log.d("jsonObjectTest",DocumentElementobj.toString());
            final String URL = Url.BASEURL() + "timesheet/save";

            JsonObjectRequest request_json = null;
            request_json = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(DocumentElementobj.toString()),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Process os success response
                                JSONObject jsonObj = null;
                                try{
                                    String responseData = response.toString();
                                    Log.d("getData",responseData.toString());
                                    JSONObject resobj = new JSONObject(responseData);
//                                    JSONObject jsonObject = resobj.getJSONObject("response");


                                    if(resobj.getString("status").contentEquals("true")){

                                        if(message_in_out.contentEquals("IN")){
                                            rl_in.setAlpha(1.0f);
                                            tv_in.setClickable(true);
                                        }else if(message_in_out.contentEquals("OUT")){
                                            rl_out.setAlpha(1.0f);
                                            tv_out.setClickable(true);
                                        }
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity.this);
//                                        alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setMessage(message_in_out);
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        //-----following code is commented on 6th dec to get the calender saved state data------
                                                        alertDialogBuilder.setCancelable(true);
//                                                        load_data_check_od_duty();
//                                                        recreate();
                                                        Intent t= new Intent(MyAttendanceActivity.this,MyAttendanceActivity.class);
                                                        startActivity(t);
                                                        finish();
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }else{

                                        if(message_in_out.contentEquals("IN")){
                                            rl_in.setAlpha(1.0f);
                                            tv_in.setClickable(true);
                                        }else if(message_in_out.contentEquals("OUT")){
                                            rl_out.setAlpha(1.0f);
                                            tv_out.setClickable(true);
                                        }
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity.this);
                                        alertDialogBuilder.setMessage(resobj.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        //-----following code is commented on 6th dec to get the calender saved state data------
                                                        alertDialogBuilder.setCancelable(true);
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }


                                }catch (Exception e){
                                    //                            loading.dismiss();
                                    e.printStackTrace();
                                    if(message_in_out.contentEquals("IN")){
                                        rl_in.setAlpha(1.0f);
                                        tv_in.setClickable(true);
                                    }else if(message_in_out.contentEquals("OUT")){
                                        rl_out.setAlpha(1.0f);
                                        tv_out.setClickable(true);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                                if(message_in_out.contentEquals("IN")){
                                    rl_in.setAlpha(1.0f);
                                    tv_in.setClickable(true);
                                }else if(message_in_out.contentEquals("OUT")){
                                    rl_out.setAlpha(1.0f);
                                    tv_out.setClickable(true);
                                }

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());

                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    if(message_in_out.contentEquals("IN")){
                        rl_in.setAlpha(1.0f);
                        tv_in.setClickable(true);
                    }else if(message_in_out.contentEquals("OUT")){
                        rl_out.setAlpha(1.0f);
                        tv_out.setClickable(true);
                    }

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        }catch (JSONException e){
            e.printStackTrace();

            if(message_in_out.contentEquals("IN")){
                rl_in.setAlpha(1.0f);
                tv_in.setClickable(true);
            }else if(message_in_out.contentEquals("OUT")){
                rl_out.setAlpha(1.0f);
                tv_out.setClickable(true);
            }
        }
    }
    //========function to save data for IN/OUT, code ends=======
    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/previous/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
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
        String url = Url.BASEURL()+"timesheet/log/today/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
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
                timesheet_id = jsonObject.getInt("timesheet_id");
                work_from_home_flag = jsonObject.getInt("work_from_home_flag");
                work_from_home_detail = jsonObject.getString("work_from_home_detail");
                if(timesheet_id != 0 ){
                    if(work_from_home_flag == 1){
                        chck_wrk_frm_home.setVisibility(View.VISIBLE);
                        chck_wrk_frm_home.setChecked(true);
                        chck_wrk_frm_home.setClickable(false);

                        ed_wrk_frm_home_detail.setVisibility(View.VISIBLE);
                        ed_wrk_frm_home_detail.setText(work_from_home_detail);
                        ed_wrk_frm_home_detail.setEnabled(true);
                    }else if(work_from_home_flag == 0){
                        chck_wrk_frm_home.setVisibility(View.GONE);
                        ed_wrk_frm_home_detail.setVisibility(View.GONE);
                    }else{
                        chck_wrk_frm_home.setVisibility(View.VISIBLE);
                        chck_wrk_frm_home.setClickable(true);

                        ed_wrk_frm_home_detail.setEnabled(true);
                        ed_wrk_frm_home_detail.setVisibility(View.GONE);
                    }
                }
                if(jsonObject.has("timesheet_in_out_action")) {
                    if (jsonObject.getString("timesheet_in_out_action").trim().contentEquals("IN")) {
                        tv_in.setVisibility(View.VISIBLE);
                        tv_out.setVisibility(View.GONE);

                        rl_in.setVisibility(View.VISIBLE);
                        rl_out.setVisibility(View.GONE);
                    } else if (jsonObject.getString("timesheet_in_out_action").trim().contentEquals("OUT")) {
                        tv_in.setVisibility(View.GONE);
                        tv_out.setVisibility(View.VISIBLE);

                        rl_in.setVisibility(View.GONE);
                        rl_out.setVisibility(View.VISIBLE);
                    } else {
                        tv_in.setVisibility(View.GONE);
                        tv_out.setVisibility(View.GONE);

                        rl_in.setVisibility(View.GONE);
                        rl_out.setVisibility(View.GONE);
                    }
                }else {
                    tv_in.setVisibility(View.GONE);
                    tv_out.setVisibility(View.GONE);

                    rl_in.setVisibility(View.GONE);
                    rl_out.setVisibility(View.GONE);
                }

                DateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
//                DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("hh:mm a");

                if(!jsonObject.getString("time_in").contentEquals("")) {
                    String inputText_time_in = jsonObject.getString("time_in");

                    Date date_log_time_in = null;
                    try {
                        date_log_time_in = inputFormat.parse(inputText_time_in);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    tv_time_in.setText(outputFormat.format(date_log_time_in));


                }else{
                    tv_time_in.setText("");
                }
                if(!jsonObject.getString("time_out").contentEquals("")){
                    String inputText_time_out = jsonObject.getString("time_out");

                    Date date_log_time_out = null;
                    try {
                        date_log_time_out = inputFormat.parse(inputText_time_out);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tv_time_out.setText(outputFormat.format(date_log_time_out));
                }else{
                    tv_time_out.setText("");
                }
            }else if(jsonObject.getString("status").contentEquals("false")){


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code for getting time_in and time_out, ends==========


    //========following function is to resign keyboard on touching anywhere in the screen
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
