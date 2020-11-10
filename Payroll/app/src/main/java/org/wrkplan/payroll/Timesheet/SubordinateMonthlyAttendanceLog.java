package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v2;
import org.wrkplan.payroll.Model.TimesheetSubordinateModel;
import org.wrkplan.payroll.Model.TimesheetSubordinateMonthlyAttendanceModel;
import org.wrkplan.payroll.Model.TimesheetSubordinateMonthlyAttendanceModel1;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class SubordinateMonthlyAttendanceLog extends AppCompatActivity implements View.OnClickListener {
    MaterialSpinner spinner_emp;
    String sl_no;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    TextView tv_nodata, tv_title;
    String month_name, year = "", month_number = "";
    public static String emp_id;
    ImageButton imgbtn_prev, imgbtn_next;
    ArrayList<TimesheetSubordinateMonthlyAttendanceModel1> timesheetSubordinateMonthlyAttendanceModel1ArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_monthly_attendancelog);
        spinner_emp = findViewById(R.id.spinner_emp);

        tv_nodata = findViewById(R.id.tv_nodata);
        tv_title = findViewById(R.id.tv_title);
        imgbtn_prev = findViewById(R.id.imgbtn_prev);
        imgbtn_next = findViewById(R.id.imgbtn_next);
        ll_recycler = findViewById(R.id.ll_recycler);
        recycler_view = findViewById(R.id.recycler_view);

        tv_title.setVisibility(View.INVISIBLE);
        imgbtn_prev.setVisibility(View.INVISIBLE);
        imgbtn_next.setVisibility(View.INVISIBLE);


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

        loadData();

    }

    public void loadData(){
        //--------Spinner code starts------
        List<String> emp_name_list = new ArrayList<>();
        for(int i=0; i<SubordinateAttendanceActivity_v2.timesheetSubordinateMonthlyAttendanceModelArrayList.size(); i++){
//           stateList  = new ArrayList<>(Arrays.asList(trackingDetailsStateSpinnerModelArrayList.get(i).getStateName()));
            emp_name_list.add(SubordinateAttendanceActivity_v2.timesheetSubordinateMonthlyAttendanceModelArrayList.get(i).getEmployee_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SubordinateMonthlyAttendanceLog.this, android.R.layout.simple_spinner_item, emp_name_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_emp.setAdapter(adapter);

        spinner_emp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == -1){
//                    edt_date_select.setClickable(false);
                }else {
//                                Toast.makeText(getApplicationContext(), "Selected: " + trackingDetailsMsrNameModelArrayList.get(position).getMsr_id(), Toast.LENGTH_LONG).show();
                    sl_no = SubordinateAttendanceActivity_v2.timesheetSubordinateMonthlyAttendanceModelArrayList.get(position).getSlno();
//                    edt_date_select.setClickable(true);
                    emp_id = sl_no;
                    loadDataDetails(sl_no);
//                    Toast.makeText(getApplicationContext(),sl_no,Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //--------Spinner code ends------
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadDataDetails(String emp_id){
        tv_title.setVisibility(View.VISIBLE);
        imgbtn_prev.setVisibility(View.VISIBLE);
        imgbtn_next.setVisibility(View.VISIBLE);
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
//        String url = Url.BASEURL()+"timesheet/log/subordinate/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/monthly/"+userSingletonModel.getCorporate_id()+"/"+emp_id+"/"+month_number+"/"+year;
//        String url = Url.BASEURL+"timesheet/log/previous/EMC_NEW/42";
        Log.d("listurlsub-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(SubordinateMonthlyAttendanceLog.this, "Loading", "Please wait...", true, false);
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
            if(!timesheetSubordinateMonthlyAttendanceModel1ArrayList.isEmpty()){
                timesheetSubordinateMonthlyAttendanceModel1ArrayList.clear();
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
                    TimesheetSubordinateMonthlyAttendanceModel1 timesheetSubordinateMonthlyAttendanceModel1 = new TimesheetSubordinateMonthlyAttendanceModel1();
                    timesheetSubordinateMonthlyAttendanceModel1.setDay_no(jsonObject2.getString("day_no"));
                    timesheetSubordinateMonthlyAttendanceModel1.setDay_name(jsonObject2.getString("day_name"));
                    timesheetSubordinateMonthlyAttendanceModel1.setDate(jsonObject2.getString("date"));
                    if(jsonObject2.getString("time_in").trim().contentEquals("")){
                        timesheetSubordinateMonthlyAttendanceModel1.setTime_in("             ");
                    }else{
                        timesheetSubordinateMonthlyAttendanceModel1.setTime_in(jsonObject2.getString("time_in"));
                    }
                    if(jsonObject2.getString("time_out").trim().contentEquals("")){
                        timesheetSubordinateMonthlyAttendanceModel1.setTime_out("              ");
                    }else {
                        timesheetSubordinateMonthlyAttendanceModel1.setTime_out(jsonObject2.getString("time_out"));
                    }
                    timesheetSubordinateMonthlyAttendanceModel1.setAttendance_status(jsonObject2.getString("attendance_status"));
                    timesheetSubordinateMonthlyAttendanceModel1.setAttendance_color(jsonObject2.getString("attendance_color"));
//                    timesheetMyAttendanceModel_v2.setMonth_no(jsonObject2.getString("month_no"));
//                    timesheetMyAttendanceModel_v2.setMonth_name(jsonObject2.getString("month_name"));
//                    timesheetMyAttendanceModel_v2.setYear(jsonObject2.getString("year"));

                    timesheetSubordinateMonthlyAttendanceModel1ArrayList.add(timesheetSubordinateMonthlyAttendanceModel1);

                }
                recycler_view.setAdapter(new SubordinateMonthlyAttendanceLogListAdapter(SubordinateMonthlyAttendanceLog.this, timesheetSubordinateMonthlyAttendanceModel1ArrayList));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

                loadDataDetails(emp_id);
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

                loadDataDetails(emp_id);
                break;
        }
    }
    //===========Code to get data from api and load data to recycler view, ends==========


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this,SubordinateAttendanceActivity_v2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
