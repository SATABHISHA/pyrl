package org.wrkplan.payroll.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
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
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.ImageUtil;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.HolidayDetail.HolidayDetailActivity;
import org.wrkplan.payroll.HolidayModel.Holiday;
import org.wrkplan.payroll.Model.DashboardPendingItemModel;
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v3;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDutyLog.SubordinateOdDutyLogListActivity;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.Timesheet.MyAttendanceActivity_LogList_Adapter_v3;
import org.wrkplan.payroll.Timesheet.MyAttendanceActivity_v3;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    //----Calendar variable, code starts---
    CaldroidFragment caldroidFragment;
    ArrayList<Holiday> arrayList = new ArrayList<>();
    ArrayList<Holiday> arrayList1 = new ArrayList<>();
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    String dateString, holiday_name1;
    TextView txt_date, txt_day_name, txt_holiday_name;
    public static Bundle savedInstanceState;
    SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
    List<Date> selectedDateRangeList = new ArrayList<>();
    public static int count = 0;
    //----Calendar variable, code ends---

    //----Attendance variable, code starts-----
    RelativeLayout rl_out, rl_in;
    TextView tv_out, tv_in, txt_in_time, txt_in_time_abbrebiation, txt_out_time, txt_out_time_abbrebiation;
    CheckBox chck_wrk_frm_home;
    EditText ed_wrk_frm_home_detail;
    public static int timesheet_id, work_from_home_flag;
    public static String timesheet_in_out_action, work_from_home_detail;

    public String latitude = "", longitude = "", locationAddress=""; //---added on 27th may
    LocationManager locationManager;

    public static final int RequestPermissionCode = 1;
    public static String base64String, SelfieInOut, SelfieMessage;

    ArrayList<TimesheetMyAttendanceModel> timesheetMyAttendanceModelArrayList = new ArrayList<>();
    ArrayList<TimesheetMyAttendanceModel_v3> timesheetMyAttendanceModel_v3ArrayList = new ArrayList<>();
    //----Attendance variable, code ends-----

    //-----Pending Items variable, code starts----
    LinearLayout ll_recycler;
    TextView tv_nodata;
    RecyclerView recycler_view;
    ArrayList<DashboardPendingItemModel> dashboardPendingItemModelArrayList = new ArrayList<>();
    //-----Pending Items variable, code ends----

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LoadCalendarData(savedInstanceState);
        LoadAttendanceData();
        LoadPendingItems();

    }

    //=========///----Pending Items, code starts---///======
    public void LoadPendingItems(){
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        recycler_view = findViewById(R.id.recycler_view);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends=====

        loadPendingItemsApiData();
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadPendingItemsApiData(){
        String url = Url.BASEURL()+"pending_actions/fetch/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        Log.d("listurl-=>",url);
        final ProgressDialog loading = ProgressDialog.show(DashboardActivity.this, "Loading", "Please wait...", true, false);
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
            if(!dashboardPendingItemModelArrayList.isEmpty()){
                dashboardPendingItemModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("pending_actions");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    DashboardPendingItemModel pendingItemModel = new DashboardPendingItemModel();
                    pendingItemModel.setEvent_name(jsonObject2.getString("event_name"));
                    pendingItemModel.setEvent_id(jsonObject2.getString("event_id"));
                    pendingItemModel.setEvent_type(jsonObject2.getString("event_type"));
                    pendingItemModel.setEvent_owner_id(jsonObject2.getString("event_owner_id"));
                    pendingItemModel.setEvent_owner_name(jsonObject2.getString("event_owner_name"));
                    pendingItemModel.setEvent_status(jsonObject2.getString("event_status"));


                    dashboardPendingItemModelArrayList.add(pendingItemModel);

                }
                recycler_view.setAdapter(new CustomDashboardPendingItemsListAdapter(this, dashboardPendingItemModelArrayList));
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
    //=========///----Pending Items, code ends---///======

    //=========///----Attendance code, starts----///======
    public void LoadAttendanceData(){
        rl_out = findViewById(R.id.rl_out);
        rl_in = findViewById(R.id.rl_in);
        tv_out = findViewById(R.id.tv_out);
        tv_in = findViewById(R.id.tv_in);
        txt_in_time = findViewById(R.id.txt_in_time);
        txt_in_time_abbrebiation = findViewById(R.id.txt_in_time_abbrebiation);
        txt_out_time = findViewById(R.id.txt_out_time);
        txt_out_time_abbrebiation = findViewById(R.id.txt_out_time_abbrebiation);
        chck_wrk_frm_home = findViewById(R.id.chck_wrk_frm_home);
        chck_wrk_frm_home = findViewById(R.id.chck_wrk_frm_home);
        ed_wrk_frm_home_detail = findViewById(R.id.ed_wrk_frm_home_detail);

        chck_wrk_frm_home.setOnClickListener(this);
        tv_in.setOnClickListener(this);
        tv_out.setOnClickListener(this);
        statusCheck();
        getLocation();
        load_data_check_od_duty();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_in:
                if(!latitude.contentEquals("") && !longitude.contentEquals("")) {
//                    save_in_out_data("IN", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance IN time recorded"); //--commented on 28th May
                    if(userSingletonModel.getAttendance_with_selfie_yn().contentEquals("1")) {
                        SelfieInOut = "IN";
                        SelfieMessage = "Attendance IN time recorded";
                        open_selfie_popup("IN", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance IN time recorded"); //--added on 28th May
                    }else{
                        save_in_out_data("IN", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance IN time recorded","");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Unable to track location. Please try again.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_out:
                if((work_from_home_flag == 1) && ed_wrk_frm_home_detail.getText().toString().trim().isEmpty()){

                    //---------Alert dialog code starts(added on 21st nov)--------
                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DashboardActivity.this);
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
                    if(!latitude.contentEquals("") && !longitude.contentEquals("")) {
//                        save_in_out_data("OUT", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance OUT time recorded"); //--commented on 28th May
                        if(userSingletonModel.getAttendance_with_selfie_yn().contentEquals("1")) {
                            SelfieInOut = "OUT";
                            SelfieMessage = "Attendance OUT time recorded";
                            open_selfie_popup("OUT", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance OUT time recorded"); //--added on 28th May
                        }else{
                            save_in_out_data("OUT", work_from_home_flag, ed_wrk_frm_home_detail.getText().toString(), "Attendance OUT time recorded","");
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Unable to track location. Please try again.",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.chck_wrk_frm_home:
                if(chck_wrk_frm_home.isChecked() && chck_wrk_frm_home.isClickable()) {
                    ed_wrk_frm_home_detail.setEnabled(true);
                    work_from_home_flag = 1;
                }else{
                    ed_wrk_frm_home_detail.setEnabled(false);
                    work_from_home_flag = 0;
                }
                break;
            default:
                break;
        }
    }

    //========function to save data for IN/OUT, code starts=======
    public void save_in_out_data(String in_out, int work_frm_home_flag, String work_from_home_detail, String message_in_out, String imageBase64){
        if(message_in_out.contentEquals("IN")){
            /*rl_in.setAlpha(0.5f);
            tv_in.setClickable(false);*/

            rl_in.setVisibility(View.GONE);
            tv_in.setVisibility(View.GONE);

            rl_out.setVisibility(View.VISIBLE);
            tv_out.setVisibility(View.VISIBLE);
        }else if(message_in_out.contentEquals("OUT")){
            /*rl_out.setAlpha(0.5f);
            tv_out.setClickable(false);*/

            rl_in.setVisibility(View.GONE);
            tv_in.setVisibility(View.GONE);

            rl_out.setVisibility(View.GONE);
            tv_out.setVisibility(View.GONE);
        }
        try {
            final JSONObject DocumentElementobj = new JSONObject();
           /* DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("timesheet_id", timesheet_id);
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("in_out_action", in_out);
            DocumentElementobj.put("work_from_home_flag", work_frm_home_flag);
            DocumentElementobj.put("work_from_home_detail", work_from_home_detail);*/ //--commented on 27th may

            //--added on 27th may starts
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("work_from_home_flag", work_frm_home_flag);
            DocumentElementobj.put("work_from_home_detail", work_from_home_detail);
            DocumentElementobj.put("latitude", latitude);
            DocumentElementobj.put("longitude",longitude);
            DocumentElementobj.put("address",locationAddress);
            DocumentElementobj.put("imageBase64",imageBase64);
            //--added on 27th may ends

            Log.d("jsonObjectTest",DocumentElementobj.toString());
//            final String URL = Url.BASEURL() + "timesheet/save";
            final String URL = Url.BASEURL() + "timesheet/save-with-geo-location"; //added on 27th may

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
                                        load_data_check_od_duty();
                                        if(message_in_out.contentEquals("IN")){
                                            /*rl_in.setAlpha(1.0f);
                                            tv_in.setClickable(true);*/

                                            rl_in.setVisibility(View.GONE);
                                            tv_in.setVisibility(View.GONE);

                                            rl_out.setVisibility(View.VISIBLE);
                                            tv_out.setVisibility(View.VISIBLE);
                                        }else if(message_in_out.contentEquals("OUT")){
                                           /* rl_out.setAlpha(1.0f);
                                            tv_out.setClickable(true);*/

                                            rl_in.setVisibility(View.GONE);
                                            tv_in.setVisibility(View.GONE);

                                            rl_out.setVisibility(View.GONE);
                                            tv_out.setVisibility(View.GONE);
                                        }
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DashboardActivity.this);
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
                                                        /*Intent t= new Intent(DashboardActivity.this,DashboardActivity.class);
                                                        startActivity(t);
                                                        finish();*/
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }else{

                                        if(message_in_out.contentEquals("IN")){
                                            /*rl_in.setAlpha(1.0f);
                                            tv_in.setClickable(true);*/

                                            rl_in.setVisibility(View.VISIBLE);
                                            tv_in.setVisibility(View.VISIBLE);

                                            rl_out.setVisibility(View.GONE);
                                            tv_out.setVisibility(View.GONE);
                                        }else if(message_in_out.contentEquals("OUT")){
                                           /* rl_out.setAlpha(1.0f);
                                            tv_out.setClickable(true);*/

                                            rl_in.setVisibility(View.GONE);
                                            tv_in.setVisibility(View.GONE);

                                            rl_out.setVisibility(View.VISIBLE);
                                            tv_out.setVisibility(View.VISIBLE);
                                        }
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DashboardActivity.this);
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
                                        rl_in.setVisibility(View.VISIBLE);
                                        tv_in.setVisibility(View.VISIBLE);

                                        rl_out.setVisibility(View.GONE);
                                        tv_out.setVisibility(View.GONE);
                                    }else if(message_in_out.contentEquals("OUT")){
                                        rl_in.setVisibility(View.GONE);
                                        tv_in.setVisibility(View.GONE);

                                        rl_out.setVisibility(View.VISIBLE);
                                        tv_out.setVisibility(View.VISIBLE);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                                if(message_in_out.contentEquals("IN")){
                                    rl_in.setVisibility(View.VISIBLE);
                                    tv_in.setVisibility(View.VISIBLE);

                                    rl_out.setVisibility(View.GONE);
                                    tv_out.setVisibility(View.GONE);
                                }else if(message_in_out.contentEquals("OUT")){
                                    rl_in.setVisibility(View.GONE);
                                    tv_in.setVisibility(View.GONE);

                                    rl_out.setVisibility(View.VISIBLE);
                                    tv_out.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());

                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    if(message_in_out.contentEquals("IN")){
                        rl_in.setVisibility(View.VISIBLE);
                        tv_in.setVisibility(View.VISIBLE);

                        rl_out.setVisibility(View.GONE);
                        tv_out.setVisibility(View.GONE);
                    }else if(message_in_out.contentEquals("OUT")){
                        rl_in.setVisibility(View.GONE);
                        tv_in.setVisibility(View.GONE);

                        rl_out.setVisibility(View.VISIBLE);
                        tv_out.setVisibility(View.VISIBLE);
                    }

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        }catch (JSONException e){
            e.printStackTrace();

            if(message_in_out.contentEquals("IN")){
                rl_in.setVisibility(View.VISIBLE);
                tv_in.setVisibility(View.VISIBLE);

                rl_out.setVisibility(View.GONE);
                tv_out.setVisibility(View.GONE);
            }else if(message_in_out.contentEquals("OUT")){
                rl_in.setVisibility(View.GONE);
                tv_in.setVisibility(View.GONE);

                rl_out.setVisibility(View.VISIBLE);
                tv_out.setVisibility(View.VISIBLE);
            }
        }
    }
    //========function to save data for IN/OUT, code ends=======
    //----------Code for getting time_in and time_out, starts----------
    public void load_data_check_od_duty(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"od/request/check-exist/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/today/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"timesheet/log/today/EMC_NEW/42";
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(DashboardActivity.this, "Loading", "Please wait...", true, false);
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
            if(!timesheetMyAttendanceModel_v3ArrayList.isEmpty()){
                timesheetMyAttendanceModel_v3ArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonDatacheck-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");

            if(jsonObject1.getString("status").contentEquals("true")) {
                timesheet_id = jsonObject.getInt("timesheet_id");
                work_from_home_flag = jsonObject.getInt("work_from_home_flag");
                work_from_home_detail = jsonObject.getString("work_from_home_detail");
                if (timesheet_id != 0) {
                    if (jsonObject.getInt("work_from_home_flag_branch") == 1) { //--work_from_home_flag_branch added on 9th dec
                        if (work_from_home_flag == 1) {
//                            chck_wrk_frm_home.setVisibility(View.VISIBLE);
                            chck_wrk_frm_home.setChecked(true);
                            chck_wrk_frm_home.setClickable(false);

//                            ed_wrk_frm_home_detail.setVisibility(View.VISIBLE);
                            ed_wrk_frm_home_detail.setText(work_from_home_detail);
                            ed_wrk_frm_home_detail.setEnabled(true);
                        } else if (work_from_home_flag == 0) {
//                            chck_wrk_frm_home.setVisibility(View.GONE);
//                            ed_wrk_frm_home_detail.setVisibility(View.GONE);
                            chck_wrk_frm_home.setClickable(false);
                            ed_wrk_frm_home_detail.setEnabled(false);
                        } else {
//                            chck_wrk_frm_home.setVisibility(View.VISIBLE);
                            chck_wrk_frm_home.setClickable(true);

                            ed_wrk_frm_home_detail.setEnabled(false);
//                            ed_wrk_frm_home_detail.setVisibility(View.GONE);
                        }
                    } else {
                        chck_wrk_frm_home.setVisibility(View.GONE);
                        ed_wrk_frm_home_detail.setVisibility(View.GONE);

                        chck_wrk_frm_home.setClickable(false);
                        ed_wrk_frm_home_detail.setEnabled(false);
                    }
                }
                if (jsonObject.has("timesheet_in_out_action")) {
                    if (jsonObject.getString("timesheet_in_out_action").trim().contentEquals("IN")) {
                        if (jsonObject.getInt("work_from_home_flag_branch") == 1) {
//                            chck_wrk_frm_home.setVisibility(View.VISIBLE); //--added on 8th oct

                            chck_wrk_frm_home.setClickable(true);
                            ed_wrk_frm_home_detail.setEnabled(false);
                        } else {
//                            chck_wrk_frm_home.setVisibility(View.GONE);

                            chck_wrk_frm_home.setClickable(false);
                            ed_wrk_frm_home_detail.setEnabled(false);
                        }

                        tv_in.setVisibility(View.VISIBLE);
                        tv_out.setVisibility(View.GONE);

                        rl_in.setVisibility(View.VISIBLE);
                        rl_out.setVisibility(View.GONE);
                    } else if (jsonObject.getString("timesheet_in_out_action").trim().contentEquals("OUT")) {
                        if (jsonObject.getInt("work_from_home_flag_branch") == 1) {
//                            chck_wrk_frm_home.setVisibility(View.VISIBLE); //--added on 8th oct

                            chck_wrk_frm_home.setClickable(true);
                            ed_wrk_frm_home_detail.setEnabled(false);
                        } else {
//                            chck_wrk_frm_home.setVisibility(View.GONE);

                            chck_wrk_frm_home.setClickable(false);
                            ed_wrk_frm_home_detail.setEnabled(false);
                        }

                        tv_in.setVisibility(View.GONE);
                        tv_out.setVisibility(View.VISIBLE);

                        rl_in.setVisibility(View.GONE);
                        rl_out.setVisibility(View.VISIBLE);
                    } else {
//                        chck_wrk_frm_home.setVisibility(View.GONE); //--added on 8th oct

                        chck_wrk_frm_home.setClickable(false);
                        ed_wrk_frm_home_detail.setEnabled(false);

                        tv_in.setVisibility(View.GONE);
                        tv_out.setVisibility(View.GONE);

                        rl_in.setVisibility(View.GONE);
                        rl_out.setVisibility(View.GONE);
                    }
                } else {
//                    chck_wrk_frm_home.setVisibility(View.GONE); //--added on 8th oct

                    chck_wrk_frm_home.setClickable(false);
                    ed_wrk_frm_home_detail.setEnabled(false);

                    tv_in.setVisibility(View.GONE);
                    tv_out.setVisibility(View.GONE);

                    rl_in.setVisibility(View.GONE);
                    rl_out.setVisibility(View.GONE);
                }

                DateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
//                DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("hh:mm a");

                if (!jsonObject.getString("time_in").contentEquals("")) {
                    String inputText_time_in = jsonObject.getString("time_in");

                    Date date_log_time_in = null;
                    try {
                        date_log_time_in = inputFormat.parse(inputText_time_in);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


//                    txt_in_time.setText(outputFormat.format(date_log_time_in));
                    txt_in_time.setText(outputFormat.format(date_log_time_in).substring(0,outputFormat.format(date_log_time_in).length()-2));
                    txt_in_time_abbrebiation.setText(outputFormat.format(date_log_time_in).substring(outputFormat.format(date_log_time_in).length()-2));
                } else {
                    txt_in_time.setText("");
                    txt_in_time_abbrebiation.setText("");
                }
                if (!jsonObject.getString("time_out").contentEquals("")) {
                    String inputText_time_out = jsonObject.getString("time_out");

                    Date date_log_time_out = null;
                    try {
                        date_log_time_out = inputFormat.parse(inputText_time_out);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txt_out_time.setText(outputFormat.format(date_log_time_out).substring(0,outputFormat.format(date_log_time_out).length()-2));
                    txt_out_time_abbrebiation.setText(outputFormat.format(date_log_time_out).substring(outputFormat.format(date_log_time_out).length()-2));


                } else {
                    txt_out_time.setText("");
                    txt_out_time_abbrebiation.setText("");
                }

            }


            else if(jsonObject1.getString("status").contentEquals("false")){
               /* ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //--------Code for getting time_in and time_out, ends----------
    //-------------location code starts(added on 27th may)--------
    @SuppressLint("MissingPermission")
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            //---minTime(in millisec), minDistance(in meters)
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//        Toast.makeText(getApplicationContext(), "Latitude:" + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//            Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2), Toast.LENGTH_LONG).show();
//            String locationAddress = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2);


//            latitude = String.valueOf(location.getLatitude());
            latitude = String.format("%.6f", location.getLatitude());
//            latitude = "";
//            longitude = String.valueOf(location.getLongitude());
            longitude = String.format("%.6f", location.getLongitude());
//            longitude = "";
            locationAddress = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2);
            Log.d("Location-=>",locationAddress);
            final Context context = this;

        } catch (Exception e) {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    //-------------location code ends(added on 27th may)--------

    //===========Code to enable gps, starts(added on 27th May)=========
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //-----------code to check location permission, code starts(added on 25th nov)---------
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }else if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

//                finish();
            }

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable your GPS")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        startActivity(new Intent(HomeActivity.this, FingerprintActivity.class));
                    }
                });
               /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });*/
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //===========Code to enable gps, ends(added on 27th May)=========
    //==========selfie popup, code starts(added on 28th May)=========

    public void open_selfie_popup(String InOut, int work_from_home_flag, String work_from_home_detail, String message){
        //-------custom dialog code starts=========
        LayoutInflater li2 = LayoutInflater.from(this);
        View dialog = li2.inflate(R.layout.activity_myattendancev3_selfie_popup, null);
        TextView tv_button_yes = dialog.findViewById(R.id.tv_button_yes);
        TextView tv_button_no = dialog.findViewById(R.id.tv_button_no);


        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setView(dialog);
        //Creating an alert dialog
        final androidx.appcompat.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();
        tv_button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
//                saveInOut("OUT","PUNCHED_OUT");
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //--added on 14th sept,2021, code starts--
                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                // Samsung
                intent.putExtra("camerafacing", "front");
                intent.putExtra("previous_mode", "front");

                // Huawei
                intent.putExtra("default_camera", "1");
                intent.putExtra("default_mode", "com.huawei.camera2.mode.photo.PhotoMode");
                //--added on 14th sept,2021, code ends--
//                intent.putExtra("android.intent.extras.CAMERA_FACING", 1); //--commented on 14th Sept 2021
                startActivityForResult(intent, 7);

            }
        });
        tv_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
//                saveInOut("OUT","BREAK_STARTS");
//                save_in_out_data(InOut, work_from_home_flag, work_from_home_detail, message, "");

            }
        });
        //-------custom dialog code ends=========
    }
    //==========selfie popup, code ends(added on 28th May)=========
    //========Camera code starts(added on 28th May)=======
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            img.setImageBitmap(bitmap);
            base64String = ImageUtil.convert(bitmap);
            save_in_out_data(SelfieInOut, work_from_home_flag, work_from_home_detail, SelfieMessage, base64String);
            Log.d("base64-=>",base64String);

//            recognize(base64String);
//            Log.d("base64-=>",base64String);
        }
    }
    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                Manifest.permission.CAMERA)) {
//            Toast.makeText(RecognizeHomeActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(MyAttendanceActivity_v3.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    //========Camera code ends(added on 28th May)=======
    //=========///-----Attendance code, ends----///==========

    //-------Calendar code, starts-----
    public void LoadCalendarData(Bundle savedInstanceState){
        txt_date = findViewById(R.id.txt_date);
        txt_day_name = findViewById(R.id.txt_day_name);
        txt_holiday_name = findViewById(R.id.txt_holiday_name);

        caldroidFragment = new CaldroidFragment();
        // If Activity is created after rotation
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            // flag=1;
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
            caldroidFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar_date_list, caldroidFragment);
            t.commit();
        }

            getholiday("1");


        //arrayList.get(position).getFrom_date();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        final CaldroidListener listener = new CaldroidListener() {


            @Override
            public void onSelectDate(Date date, View view) {
               count = count + 1;


                // Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                Log.d("date==", date.toString());
                SimpleDateFormat inputformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                //SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                //---get current day date, code starts---

                if (count <= 2) {
                    ColorDrawable color = new ColorDrawable(Color.parseColor("#E4FCAD"));
                    caldroidFragment.setBackgroundDrawableForDate(color, date);
                    caldroidFragment.refreshView();
                    selectedDateRangeList.add(date);
                }else if(count>2){
                    for(int i=0; i<selectedDateRangeList.size(); i++){

                        caldroidFragment.clearBackgroundDrawableForDate(selectedDateRangeList.get(i));
                        caldroidFragment.refreshView();
                    }
                    selectedDateRangeList.clear();
                    count = 0;

                }
                //---get current day date, code ends---


                try {
                    Date d1 = inputformat.parse(date.toString());
                    Log.d("niladri=>", d1.toString());
                    String formateDate = new SimpleDateFormat("dd/MM/yyyy").format(d1);
                    Log.d("DraftDate1-=>", formateDate);


                    for (int j = 0; j < arrayList1.size(); j++) {
                        if (formateDate.equals(arrayList1.get(j).getFrom_date())) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
                            Date myDate = null;
                            try {
                                myDate = sdf.parse(arrayList1.get(j).getFrom_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdf.applyPattern("EEE, d MMM yyyy");
                            //sdf.applyPattern("d MMM YYYY");
                            String sMyDate = sdf.format(myDate);
                            txt_date.setText(sMyDate);
                            txt_holiday_name.setText(arrayList1.get(j).getHoliday_name());
                        }
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                //

                //}

            }

        };
        caldroidFragment.setCaldroidListener(listener);


      /*  try {


            t = getSupportFragmentManager().beginTransaction();
            t.addToBackStack(null);
            t.replace(R.id.ll_cal, caldroidFragment, null);
            t.commit();


        } catch (NullPointerException e) {
            e.getMessage();
        }*/
    }

    public void getholiday(String year_code) {
        String url = Url.BASEURL() + "holidays/" + userSingletonModel.corporate_id + "/" + year_code;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("holidays");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jb1 = jsonArray.getJSONObject(i);
                        String holiday_name = jb1.getString("holiday_name");
                        String from_date = jb1.getString("from_date");
                        String total_days = jb1.getString("total_days");
                        String id = jb1.getString("id");
                        String to_date=jb1.getString("to_date");
                        Holiday holiday = new Holiday();
                        holiday.setHoliday_name(holiday_name);
                        holiday.setFrom_date(from_date);
                        holiday.setTotal_days(total_days);
                        holiday.setId(id);
                        holiday.setTo_date(to_date);
                        arrayList1.add(holiday);

                        for (int j = 0; j < arrayList1.size(); j++) {
                            dateString = arrayList1.get(j).getFrom_date();

                            holiday_name1 = arrayList1.get(j).getHoliday_name();


                        }
                        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date draft_date_current_format = inputFormat.parse(dateString);
                        Log.d("datenew==", dateString);
                        String draft_date_otput_format = outputFormat.format(draft_date_current_format);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        ColorDrawable color = new ColorDrawable(Color.parseColor("#c2c2c2"));
                        Log.d("DraftDate-=>", draft_date_otput_format.toString());
                        caldroidFragment.setBackgroundDrawableForDate(color, dateFormat.parse(draft_date_otput_format));


                        Log.d("dateString-=>", dateString);
                        /*SimpleDateFormat myFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                        selectDate = myFormat1.parse(dateString);

                        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date_current_format = inputFormat.parse(dateString);
                        String draft_date_otput_format = outputFormat.format(date_current_format);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Log.d("dateString-=>",dateFormat.parse(draft_date_otput_format).toString());
                        ColorDrawable color = new ColorDrawable(Color.parseColor("#c2c2c2"));
                        caldroidFragment.setBackgroundDrawableForDate(color, dateFormat.parse(draft_date_otput_format));*/
                        // caldroidFragment.setTextColorForDate(color, selectDate);


                    }

                    //---get current day date, code starts---
                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);
                    Date today = (Date) myFormat.parse(fDate);
                    //---get current day date, code ends---
                    ColorDrawable color = new ColorDrawable(Color.parseColor("#E4FCAD"));
                    caldroidFragment.setBackgroundDrawableForDate(color, today);


//                    lv1.setAdapter(new HolidayDetailActivity.Nr());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(DashboardActivity.this).add(stringRequest);


    }

    //-------Calendar code, ends-----

}
