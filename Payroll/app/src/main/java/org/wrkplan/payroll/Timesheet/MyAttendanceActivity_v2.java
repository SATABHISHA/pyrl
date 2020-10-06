package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAttendanceActivity_v2 extends AppCompatActivity implements View.OnClickListener {
    TextView tv_punch_time;
    CheckBox chck_wrk_frm_home;
    EditText ed_wrk_frm_home_detail;
    public static int timesheet_id, work_from_home_flag;
    UserSingletonModel userSingletonModel = UserSingletonModel.getUserSingletonModel();
    Button btn_subordinate_attendance_log, btn_my_attendance_log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance_v2);

        tv_punch_time = findViewById(R.id.tv_punch_time);
        chck_wrk_frm_home = findViewById(R.id.chck_wrk_frm_home);
        ed_wrk_frm_home_detail = findViewById(R.id.ed_wrk_frm_home_detail);
        btn_subordinate_attendance_log = findViewById(R.id.btn_subordinate_attendance_log);
        btn_my_attendance_log = findViewById(R.id.btn_my_attendance_log);

        tv_punch_time.setOnClickListener(this);
        chck_wrk_frm_home.setOnClickListener(this);
        btn_subordinate_attendance_log.setOnClickListener(this);
        btn_my_attendance_log.setOnClickListener(this);

        load_biometric_data();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_punch_time:
                save_in_out_data(work_from_home_flag, ed_wrk_frm_home_detail.getText().toString());
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
            case R.id.btn_subordinate_attendance_log:
                Intent intent_subordinate_attendance_log = new Intent(MyAttendanceActivity_v2.this, SubordinateAttendanceActivity_v2.class);
                intent_subordinate_attendance_log.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_subordinate_attendance_log);
                break;
                case R.id.btn_my_attendance_log:
                Intent intent_my_attendance_log = new Intent(MyAttendanceActivity_v2.this, MyAttendanceLogActivity_v2.class);
                    intent_my_attendance_log.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_my_attendance_log);
                break;
            default:
                break;
        }
    }

    //========function to save data for IN/OUT, code starts=======
    public void save_in_out_data(int work_frm_home_flag, String work_from_home_detail){

        try {
            final JSONObject DocumentElementobj = new JSONObject();
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
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
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity_v2.this);
//                                        alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setMessage(resobj.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        //-----following code is commented on 6th dec to get the calender saved state data------
                                                        alertDialogBuilder.setCancelable(true);
//                                                        load_data_check_od_duty();
//                                                        recreate();
                                                        Intent t= new Intent(MyAttendanceActivity_v2.this,MyAttendanceActivity_v2.class);
                                                        startActivity(t);
                                                        finish();
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }else{

//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity_v2.this);
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

                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());

                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    //========function to save data for IN/OUT, code ends=======

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_dashboard = new Intent(MyAttendanceActivity_v2.this, HomeActivity.class);
        intent_dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_dashboard);
    }

    //===========Code for getting biometric data, starts==========
    ProgressDialog loading;

    public void load_biometric_data(){
        loading = ProgressDialog.show(MyAttendanceActivity_v2.this, "Loading", "Please wait! Syncing data from biometric machine.", true, false);
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"od/request/check-exist/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/biometric/fetch/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL+"timesheet/log/today/EMC_NEW/42";
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get_biometric_update(response);
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
    public void get_biometric_update(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
//            JSONObject jsonObject1 = jsonObject.getJSONObject("response");

            if(jsonObject.getString("status").contentEquals("true")){
                loading.dismiss();
                //---------Alert dialog code starts(added on 21st nov)--------
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAttendanceActivity_v2.this);
//                                        alertDialogBuilder.setMessage(jsonObject.getString("message"));
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage(jsonObject.getString("message"));
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //-----following code is commented on 6th dec to get the calender saved state data------
                                alertDialogBuilder.setCancelable(true);
//                                                        load_data_check_od_duty();
//                                                        recreate();
                               /* Intent t= new Intent(MyAttendanceActivity_v2.this,MyAttendanceActivity_v2.class);
                                startActivity(t);
                                finish();*/
                            }
                        });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //--------Alert dialog code ends--------
                }

        } catch (JSONException e) {
            loading.dismiss();
            e.printStackTrace();
        }
    }

    //===========Code for getting biometric data, ends==========
}
