package org.wrkplan.payroll.OutDoorDutyLog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Model.OutDoorTaskModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OdDutyLogEmployeeTaskActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<OutDoorTaskModel> outDoorTaskModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler, ll_supervisor, ll_button;
    TextView tv_empname, tv_status, tv_task_details_date_time, tv_nodata, tv_button_back, tv_button_cancel, tv_button_save, tv_button_submit, tv_button_return, tv_button_approve, tv_btn_new_task, tv_supervisor_name;
    public static String status_user;
    RecyclerView recycler_view;
    EditText ed_remarks;
    public static Integer od_duty_task_head_id;
    public static String task_date, task_status; //--task_status is to check the status of save, submitted...for deleting task
    public static Integer back_btn_save_unsave_check=0;
//    CustomOdDutyLogTaskAdapter customOdDutyLogTaskAdapter = new CustomOdDutyLogTaskAdapter(OdDutyLogEmployeeTaskActivity.this, outDoorTaskModelArrayList);
    public static CustomOdDutyLogTaskAdapter customOdDutyLogTaskAdapter;
    androidx.appcompat.app.AlertDialog.Builder builder;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.od_duty_log_employee_task_activity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        customOdDutyLogTaskAdapter =  new CustomOdDutyLogTaskAdapter(OdDutyLogEmployeeTaskActivity.this, outDoorTaskModelArrayList);

        builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        img_back=findViewById(R.id.img_back);
        ll_recycler = findViewById(R.id.ll_recycler);
        ll_supervisor = findViewById(R.id.ll_supervisor);
        ll_button = findViewById(R.id.ll_button);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_empname = findViewById(R.id.tv_empname);
        tv_status = findViewById(R.id.tv_status);
        tv_task_details_date_time = findViewById(R.id.tv_task_details_date_time);
        tv_btn_new_task = findViewById(R.id.tv_btn_new_task);
        tv_supervisor_name = findViewById(R.id.tv_supervisor_name);
        ed_remarks = findViewById(R.id.ed_remarks);

        tv_button_back = findViewById(R.id.tv_button_back);
        tv_button_cancel = findViewById(R.id.tv_button_cancel);
        tv_button_save = findViewById(R.id.tv_button_save);
        tv_button_submit = findViewById(R.id.tv_button_submit);
        tv_button_return = findViewById(R.id.tv_button_return);
        tv_button_approve = findViewById(R.id.tv_button_approve);

        tv_button_back.setOnClickListener(this);
        tv_button_cancel.setOnClickListener(this);
        tv_button_save.setOnClickListener(this);
        tv_button_submit.setOnClickListener(this);
        tv_button_return.setOnClickListener(this);
        tv_button_approve.setOnClickListener(this);
        tv_btn_new_task.setOnClickListener(this);
        img_back.setOnClickListener(this);

        ed_remarks.setClickable(false);
        ed_remarks.setEnabled(false);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        tv_empname.setText(userSingletonModel.getLog_task_employee_name());
        tv_task_details_date_time.setText("Task Details of "+userSingletonModel.getLog_task_date());
//        loadData(0);
        loadData(OdDutyLogListActivity.log_task_status);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if(back_btn_save_unsave_check == 1) {
                    //---------Alert dialog code starts(added on 1st dec)--------
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Unsaved data will be lost.\nDo you want to continue?");
                    alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialogBuilder.setCancelable(false);
                            Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,OdDutyLogListActivity.class);
                            intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_odlist);

                            back_btn_save_unsave_check = 0;
                        }
                    });
                    alertDialogBuilder.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    alertDialogBuilder.setCancelable(true);
                                }
                            });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    //--------Alert dialog code ends--------
                }else if(back_btn_save_unsave_check == 0){

                    if(OdDutyLogListActivity.log_task_status == 0){
                        Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,OdDutyLogListActivity.class);
                        intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_odlist);
                    }else if(OdDutyLogListActivity.log_task_status == 1){
                        Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,SubordinateOdDutyLogListActivity.class);
                        intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_odlist);
                    }
                }
                break;
            case R.id.tv_button_back:

                if(back_btn_save_unsave_check == 1) {
                    //---------Alert dialog code starts(added on 1st dec)--------
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Unsaved data will be lost.\nDo you want to continue?");
                    alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialogBuilder.setCancelable(false);
                            Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,OdDutyLogListActivity.class);
                            intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_odlist);

                            back_btn_save_unsave_check = 0;
                        }
                    });
                    alertDialogBuilder.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    alertDialogBuilder.setCancelable(true);
                                }
                            });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    //--------Alert dialog code ends--------
                }else if(back_btn_save_unsave_check == 0){

                    if(OdDutyLogListActivity.log_task_status == 0){
                        Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,OdDutyLogListActivity.class);
                        intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_odlist);
                    }else if(OdDutyLogListActivity.log_task_status == 1){
                        Intent intent_odlist = new Intent(OdDutyLogEmployeeTaskActivity.this,SubordinateOdDutyLogListActivity.class);
                        intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_odlist);
                    }
                }
                break;
            case R.id.tv_button_cancel:
                approve_return_cancel("Cancelled");
                break;
            case R.id.tv_button_save:
                save_submit("Saved");

                back_btn_save_unsave_check = 0; //--added on 2nd June
                break;
            case R.id.tv_button_submit:

                //------added od_status_checkin on 18th June(submit will only happen if status is stopped), code starts
                if(OdDutyLogListActivity.od_duty_status.contains("STOP") || OdDutyLogListActivity.od_duty_status.contains("NA")) {
                    save_submit("Submitted");
                    back_btn_save_unsave_check = 0; //--added on 2nd June
                }else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(OdDutyLogEmployeeTaskActivity.this);
                    builder.setMessage("You cannot submit today's task(s) until OD Duty is stopped.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    androidx.appcompat.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                //------added od_status_checkin on 18th June(submit will only happen if status is stopped), code ends


                break;
            case R.id.tv_button_return:
                approve_return_cancel("Returned");
                break;
            case R.id.tv_button_approve:
                approve_return_cancel("Approved");
                break;
            case R.id.tv_btn_new_task:
//                loadPopupAddNewTask();

                //--Date checkin added on 22nd jan
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = null;
                try {
                    strDate = sdf.parse(userSingletonModel.getLog_task_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat sdf_current_date = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = sdf_current_date.format(new Date());
                Integer datediff = Integer.parseInt(get_date_difference(currentDate,userSingletonModel.getLog_task_date()));

                if (datediff<1) {
                    builder.setMessage("You cannot add task for a expired OD Duty")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   dialog.cancel();

                                }
                            });
                    //Creating dialog box
                    androidx.appcompat.app.AlertDialog alert_logout = builder.create();
                    //Setting the title manually
//                    alert_logout.setTitle("Alert!");
                    alert_logout.show();
//                    Toast.makeText(getApplicationContext(),"Outdated",Toast.LENGTH_LONG).show();
                }
                else {
                    loadPopupAddNewTask();
                }
                break;
        }
    }
    //---------Date Difference function, code starts-------
    public String get_date_difference(String fromDate, String toDate){
        String dayDifference = "";
        try {
            //Dates to compare
           /* String CurrentDate=  "09/24/2015";
            String FinalDate=  "09/26/2015";*/

            Date date1;
            Date date2;

//            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

            //Setting dates
            date1 = dates.parse(fromDate);
            date2 = dates.parse(toDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
//            long difference = date1.getTime() - date2.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            if (date2.getTime() < date1.getTime()) {
//                Toast.makeText(getApplicationContext(), "To Date should be graeter than \"From Date\"", Toast.LENGTH_LONG).show();
                dayDifference = Long.toString(-(differenceDates+1));
                dayDifference = dayDifference;
            }else if(date2.getTime() >= date1.getTime()){
                //Convert long to String

                dayDifference = Long.toString(differenceDates+1);
            }
//            dayDifference = Long.toString(differenceDates+1);

            Log.e("HERE","HERE: " + dayDifference);


        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
        return dayDifference;
    }
    //---------Date Difference function, code ends-------

    //=======code for popup to create new task, starts=======
    public void loadPopupAddNewTask(){

        LayoutInflater li = LayoutInflater.from(this);
        final View dialog = li.inflate(R.layout.od_duty_log_employee_task_popup, null);
        final EditText ed_task_name = (EditText) dialog.findViewById(R.id.ed_task_name);
        final EditText ed_description = (EditText) dialog.findViewById(R.id.ed_description);
        TextView tv_button_cancel = dialog.findViewById(R.id.tv_button_cancel);
        final TextView tv_dialog_button_save = dialog.findViewById(R.id.tv_button_save);



        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(dialog);
        alert.setCancelable(false);
        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        ed_description.setClickable(false);
        ed_description.setEnabled(false);
        ed_description.setAlpha(0.6f);

        tv_dialog_button_save.setClickable(false);
        tv_dialog_button_save.setAlpha(0.6f);

        ed_task_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(ed_task_name.getText().equals("")){
                    tv_dialog_button_save.setClickable(false);
                    tv_dialog_button_save.setAlpha(0.6f);

                    ed_description.setClickable(false);
                    ed_description.setEnabled(false);
                    ed_description.setAlpha(0.6f);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ed_task_name.getText().toString().trim().equals("")){
                    //write your code here
                    tv_dialog_button_save.setClickable(false);
                    tv_dialog_button_save.setAlpha(0.6f);

                    ed_description.setClickable(false);
                    ed_description.setEnabled(false);
                    ed_description.setAlpha(0.6f);
                }else if(!ed_task_name.getText().toString().trim().equals("")){
                    tv_dialog_button_save.setClickable(false);
                    tv_dialog_button_save.setAlpha(0.6f);

                    ed_description.setClickable(true);
                    ed_description.setEnabled(true);
                    ed_description.setAlpha(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(ed_description.getText().equals("")){
                    tv_dialog_button_save.setClickable(false);
                    tv_dialog_button_save.setAlpha(0.6f);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ed_description.getText().toString().trim().equals("")){
                    //write your code here
                    tv_dialog_button_save.setClickable(false);
                    tv_dialog_button_save.setAlpha(0.6f);
                }else{
                    tv_dialog_button_save.setClickable(true);
                    tv_dialog_button_save.setAlpha(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        tv_dialog_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (!outDoorTaskModelArrayList.isEmpty()) {
                    outDoorTaskModelArrayList.clear();
                }*/
                OutDoorTaskModel outDoorTaskModel = new OutDoorTaskModel();
                outDoorTaskModel.setOd_duty_task_detail_id(String.valueOf(od_duty_task_head_id));
                outDoorTaskModel.setOd_duty_task_head_id(String.valueOf(od_duty_task_head_id));
                outDoorTaskModel.setTask_name(ed_task_name.getText().toString());
                outDoorTaskModel.setTask_description(ed_description.getText().toString());
                outDoorTaskModel.setTask_delete_api_call(0);

                outDoorTaskModelArrayList.add(outDoorTaskModel);
                customOdDutyLogTaskAdapter.notifyDataSetChanged();
//                loadData(1);

                if(!outDoorTaskModelArrayList.isEmpty()){
                    tv_button_save.setClickable(true);
                    tv_button_save.setAlpha(1.0f);

                    tv_button_submit.setClickable(true);
                    tv_button_submit.setAlpha(1.0f);
                }

                //---added on 15th May
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                back_btn_save_unsave_check = 1;

                alertDialog.dismiss();
            }
        });
    }
    //=======code for popup to create new task, ends=======

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(final Integer status) {
//        String url = Url.BASEURL+"od/task/detail/"+userSingletonModel.getCorporate_id()+"/"+OdDutyLogListActivity.od_log_date+"/"+OdDutyLogListActivity.od_request_id+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"od/task/detail/"+userSingletonModel.getCorporate_id()+"/"+OdDutyLogListActivity.od_log_date+"/"+OdDutyLogListActivity.od_request_id+"/"+userSingletonModel.getLog_employee_id();
//        String url = "http://220.225.40.151:9018/api/od/task/detail/payroll_713/2020-05-06/9/50";
        Log.d("tasklisturl-=>", url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(OdDutyLogEmployeeTaskActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponseData(response,status);
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

    public void getResponseData(String response, Integer status) {
        try {
                if (!outDoorTaskModelArrayList.isEmpty()) {
                    outDoorTaskModelArrayList.clear();
                }
            JSONObject jsonObject = new JSONObject(response);


            Log.d("jsonData-=>", jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if (jsonObject1.getString("status").contentEquals("true")) {

                ed_remarks.setText(jsonObject.getString("supervisor_remark"));
                tv_supervisor_name.setText("By "+jsonObject.getString("action_taken_by_name"));

                od_duty_task_head_id = jsonObject.getInt("od_duty_task_head_id");

                //==========Date format code starts========
               /* DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                String inputText = jsonObject.getString("task_date");

                Date task_date_format = null;
                try {
                    task_date_format = inputFormat.parse(inputText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
//                task_date = outputFormat.format(task_date_format);
                task_date = jsonObject.getString("task_date"); //--format changed again
                //==========Date format code ends========

//                tv_status.setText(jsonObject.getString("task_status"));

                task_status = jsonObject.getString("task_status");
                if(status == 0) {
                    ed_remarks.setTextColor(Color.parseColor("#b2b2b2"));
                    if (jsonObject.getString("task_status").contentEquals("Saved")) {
                        tv_status.setText(jsonObject.getString("task_status"));
                        status_user = jsonObject.getString("task_status");
                        ll_supervisor.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Submitted")) {
                        tv_btn_new_task.setVisibility(View.GONE);

                        tv_status.setText(jsonObject.getString("task_status"));
                        status_user = jsonObject.getString("task_status");

                        ll_supervisor.setVisibility(View.GONE);

                        tv_button_save.setClickable(false);
                        tv_button_save.setAlpha(0.6f);

                        tv_button_submit.setClickable(false);
                        tv_button_submit.setAlpha(0.6f);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Approved")) {
                        tv_btn_new_task.setVisibility(View.GONE);

                        tv_status.setText(jsonObject.getString("task_status"));
                        status_user = jsonObject.getString("task_status");

                        ll_supervisor.setVisibility(View.VISIBLE);

                        /*tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);*/

                        tv_button_save.setClickable(false);
                        tv_button_save.setAlpha(0.6f);

                        tv_button_submit.setClickable(false);
                        tv_button_submit.setAlpha(0.6f);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Returned")) {
                        tv_btn_new_task.setVisibility(View.GONE);

                        tv_status.setText(jsonObject.getString("task_status"));
                        status_user = jsonObject.getString("task_status");

                        ll_supervisor.setVisibility(View.VISIBLE);

                        tv_button_save.setClickable(false);
                        tv_button_save.setAlpha(0.6f);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Cancelled")) {
                        tv_btn_new_task.setVisibility(View.GONE);

                        tv_status.setText(jsonObject.getString("task_status"));
                        status_user = jsonObject.getString("task_status");

                        ll_supervisor.setVisibility(View.VISIBLE);

                        tv_button_save.setClickable(false);
                        tv_button_save.setAlpha(0.6f);

                        tv_button_submit.setClickable(false);
                        tv_button_submit.setAlpha(0.6f);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("")) {
                        ll_supervisor.setVisibility(View.GONE);

                        ll_recycler.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(jsonObject1.getString("message"));

                        /*ll_recycler.setVisibility(View.GONE);
                        ll_button.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);

                        tv_nodata.setText(jsonObject1.getString("message"));*/

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);

                        if(outDoorTaskModelArrayList.isEmpty()){
                            tv_button_save.setClickable(false);
                            tv_button_save.setAlpha(0.6f);

                            tv_button_submit.setClickable(false);
                            tv_button_submit.setAlpha(0.6f);
                        }

                    }
                }else if(status == 1) {
                    tv_btn_new_task.setVisibility(View.GONE);
                    ed_remarks.setTextColor(Color.parseColor("#7b7a7a"));
                    if (jsonObject.getString("task_status").contentEquals("Saved")) {
                       /* ll_supervisor.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.GONE);
                        tv_button_return.setVisibility(View.GONE);
                        tv_button_approve.setVisibility(View.GONE);*/

                        ll_recycler.setVisibility(View.GONE);
//                        ll_button.setVisibility(View.GONE);
                        ll_supervisor.setVisibility(View.GONE);
                        recycler_view.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);

                        tv_nodata.setText("No OD task detail exists for this day");

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_return.setClickable(false);
                        tv_button_return.setAlpha(0.6f);

                        tv_button_approve.setClickable(false);
                        tv_button_approve.setAlpha(0.6f);

                        tv_button_cancel.setClickable(false);
                        tv_button_cancel.setAlpha(0.6f);
                    } else if (jsonObject.getString("task_status").contentEquals("Submitted")) {

                        tv_supervisor_name.setText("By "+userSingletonModel.getFull_employee_name());

                        ll_supervisor.setVisibility(View.VISIBLE);
                        ed_remarks.setClickable(true);
                        ed_remarks.setEnabled(true);

                        tv_status.setText(jsonObject.getString("task_status"));

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.VISIBLE);
                        tv_button_return.setVisibility(View.VISIBLE);
                        tv_button_approve.setVisibility(View.VISIBLE);

                        //---added on 2nd June


                        if(!ed_remarks.getText().toString().trim().isEmpty()){
                            tv_button_return.setClickable(true);
                            tv_button_return.setAlpha(1.0f);

                            tv_button_approve.setClickable(true);
                            tv_button_approve.setAlpha(1.0f);

                            tv_button_cancel.setClickable(true);
                            tv_button_cancel.setAlpha(1.0f);
                        }else if(ed_remarks.getText().toString().trim().isEmpty()){
                            tv_button_return.setClickable(false);
                            tv_button_return.setAlpha(0.6f);

                            tv_button_approve.setClickable(false);
                            tv_button_approve.setAlpha(0.6f);

                            tv_button_cancel.setClickable(false);
                            tv_button_cancel.setAlpha(0.6f);
                        }

                        ed_remarks.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                if(!ed_remarks.getText().toString().trim().isEmpty()){
                                    tv_button_return.setClickable(true);
                                    tv_button_return.setAlpha(1.0f);

                                    tv_button_approve.setClickable(true);
                                    tv_button_approve.setAlpha(1.0f);

                                    tv_button_cancel.setClickable(true);
                                    tv_button_cancel.setAlpha(1.0f);
                                }else if(ed_remarks.getText().toString().trim().isEmpty()){
                                    tv_button_return.setClickable(false);
                                    tv_button_return.setAlpha(0.6f);

                                    tv_button_approve.setClickable(false);
                                    tv_button_approve.setAlpha(0.6f);

                                    tv_button_cancel.setClickable(false);
                                    tv_button_cancel.setAlpha(0.6f);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Approved")) {
                        tv_status.setText(jsonObject.getString("task_status"));
                        ll_supervisor.setVisibility(View.VISIBLE);

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.VISIBLE);
                        tv_button_return.setVisibility(View.VISIBLE);
                        tv_button_approve.setVisibility(View.VISIBLE);

                        tv_button_return.setClickable(false);
                        tv_button_return.setAlpha(0.6f);

                        tv_button_approve.setClickable(false);
                        tv_button_approve.setAlpha(0.6f);

                        tv_button_cancel.setClickable(false);
                        tv_button_cancel.setAlpha(0.6f);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Returned")) {
                        tv_status.setText(jsonObject.getString("task_status"));
                        ll_supervisor.setVisibility(View.VISIBLE);

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.VISIBLE);
                        tv_button_return.setVisibility(View.VISIBLE);
                        tv_button_approve.setVisibility(View.VISIBLE);

                        tv_button_return.setClickable(false);
                        tv_button_return.setAlpha(0.6f);

                        tv_button_approve.setClickable(false);
                        tv_button_approve.setAlpha(0.6f);

                        tv_button_cancel.setClickable(false);
                        tv_button_cancel.setAlpha(0.6f);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("Cancelled")) {
                        tv_status.setText(jsonObject.getString("task_status"));
                        ll_supervisor.setVisibility(View.VISIBLE);

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.VISIBLE);
                        tv_button_return.setVisibility(View.VISIBLE);
                        tv_button_approve.setVisibility(View.VISIBLE);

                        tv_button_return.setClickable(false);
                        tv_button_return.setAlpha(0.6f);

                        tv_button_approve.setClickable(false);
                        tv_button_approve.setAlpha(0.6f);

                        tv_button_cancel.setClickable(false);
                        tv_button_cancel.setAlpha(0.6f);

                        //---added on 15th May
                        ll_recycler.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    } else if (jsonObject.getString("task_status").contentEquals("")) {
                        tv_status.setText(jsonObject.getString("task_status"));
                        ll_supervisor.setVisibility(View.GONE);
                        ll_recycler.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("No OD task detail exists for this day");

                        tv_button_save.setVisibility(View.GONE);
                        tv_button_submit.setVisibility(View.GONE);

                        tv_button_cancel.setVisibility(View.VISIBLE);
                        tv_button_return.setVisibility(View.VISIBLE);
                        tv_button_approve.setVisibility(View.VISIBLE);

                        tv_button_return.setClickable(false);
                        tv_button_return.setAlpha(0.6f);

                        tv_button_approve.setClickable(false);
                        tv_button_approve.setAlpha(0.6f);

                        tv_button_cancel.setClickable(false);
                        tv_button_cancel.setAlpha(0.6f);


                    }
                }

               /* ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);*/ //15th may

                if(!jsonObject.getString("task_status").contentEquals("")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("tasks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        OutDoorTaskModel outDoorTaskModel = new OutDoorTaskModel();
                        outDoorTaskModel.setOd_duty_task_detail_id(jsonObject2.getString("od_duty_task_detail_id"));
                        outDoorTaskModel.setOd_duty_task_head_id(jsonObject2.getString("od_duty_task_head_id"));
                        outDoorTaskModel.setTask_name(jsonObject2.getString("task_name"));
                        outDoorTaskModel.setTask_description(jsonObject2.getString("task_description"));
                        outDoorTaskModel.setTask_delete_api_call(1);

                        outDoorTaskModelArrayList.add(outDoorTaskModel);

                    }
                }
//                recycler_view.setAdapter(new CustomOdDutyLogTaskAdapter(OdDutyLogEmployeeTaskActivity.this, outDoorTaskModelArrayList));
                recycler_view.setAdapter(customOdDutyLogTaskAdapter);

            } else if (jsonObject1.getString("status").contentEquals("false")) {
                ll_recycler.setVisibility(View.GONE);
                ll_button.setVisibility(View.GONE);
                ll_supervisor.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);

                tv_nodata.setText(jsonObject1.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code to get data from api and load data to recycler view, ends==========

    //===========code to save/submit data, code starts===========
    public void save_submit(String task_status){

        final JSONObject DocumentElementobj = new JSONObject();
        JSONArray req = new JSONArray();
        JSONObject reqObjdt = new JSONObject();
        try {
            for (int i = 0; i < outDoorTaskModelArrayList.size(); i++) {
                JSONObject reqObj = new JSONObject();
//                reqObj.put("od_duty_task_head_id", Integer.parseInt(outDoorTaskModelArrayList.get(i).getOd_duty_task_detail_id()));
                reqObj.put("od_duty_task_head_id", od_duty_task_head_id);
                reqObj.put("task_name", outDoorTaskModelArrayList.get(i).getTask_name());
                reqObj.put("task_description", outDoorTaskModelArrayList.get(i).getTask_description());
                reqObj.put("saved_from_mobile_app", 1);
                req.put(reqObj);
            }
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("od_duty_task_head_id", od_duty_task_head_id);
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("task_date", task_date);
            DocumentElementobj.put("od_request_id", Integer.parseInt(OdDutyLogListActivity.od_request_id));
            DocumentElementobj.put("task_status", task_status);
            DocumentElementobj.put("entry_user", LoginActivity.entry_user);
            DocumentElementobj.put("saved_from_mobile_app", 1);
            DocumentElementobj.put("task_detail", req);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("jsontesting-=>",DocumentElementobj.toString());


        //------calling api to save data
        JsonObjectRequest request_json = null;
        String URL = Url.BASEURL()+"od/task/save";
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(DocumentElementobj.toString()),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Process os success response
                                JSONObject jsonObj = null;
                                try{
                                    String responseData = response.toString();
                                    String val = "";
                                    JSONObject resobj = new JSONObject(responseData);
                                    Log.d("getData",resobj.toString());

                                    if(resobj.getString("status").contentEquals("true")){
                                        Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(getIntent());
                                    }else{
                                        Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                    }
                                }catch (JSONException e){
                                    //  loading.dismiss();
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
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========code to save/submit data, code ends===========

    //===========code to approve/return/cancel, code starts==========
     public void approve_return_cancel(String status){
         final JSONObject DocumentElementobj = new JSONObject();
         JSONObject reqObjdt = new JSONObject();

         try {
         DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
         DocumentElementobj.put("od_duty_task_head_id", od_duty_task_head_id);
         DocumentElementobj.put("task_status", status);
         DocumentElementobj.put("supervisor_remark", ed_remarks.getText().toString());
         DocumentElementobj.put("action_taken_by_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
         DocumentElementobj.put("update_user", LoginActivity.entry_user);
         }catch (JSONException e){
             e.printStackTrace();
         }
         Log.d("jsonSupervisor-=>",DocumentElementobj.toString());

         //------calling api to save data
         JsonObjectRequest request_json = null;
         String URL = Url.BASEURL()+"od/task/approval";
         try {
             request_json = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(DocumentElementobj.toString()),
                     new Response.Listener<JSONObject>() {
                         @Override
                         public void onResponse(JSONObject response) {
                             try {
                                 //Process os success response
                                 JSONObject jsonObj = null;
                                 try{
                                     String responseData = response.toString();
                                     String val = "";
                                     JSONObject resobj = new JSONObject(responseData);
                                     Log.d("getData",resobj.toString());

                                     if(resobj.getString("status").contentEquals("true")){
                                         Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                         finish();
                                         startActivity(getIntent());
                                     }else{
                                         Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                     }
                                 }catch (JSONException e){
                                     //  loading.dismiss();
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
                 }
             });
             RequestQueue requestQueue = Volley.newRequestQueue(this);
             requestQueue.add(request_json);
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
    //===========code to approve/return/cancel, code ends==========

    //========following function is to resign keyboard on touching anywhere in the screen
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
