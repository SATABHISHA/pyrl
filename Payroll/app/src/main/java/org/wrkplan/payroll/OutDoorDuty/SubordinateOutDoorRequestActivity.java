package org.wrkplan.payroll.OutDoorDuty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubordinateOutDoorRequestActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    TextView tv_requisition_no, tv_emp_name, tv_total_days;
    EditText edt_from_date_select, edt_date_to_select, ed_reason, ed_approval;
    Spinner spinner_rqst_status, spinner_od_duty_type;
    Button btn_save, btn_cancel;
    public static String od_status="";
    int flag_edt_rmarks_chck = 0;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_outdoor_request);

//        img_back=findViewById(R.id.img_back);
        tv_emp_name = findViewById(R.id.tv_emp_name);
        tv_requisition_no = findViewById(R.id.tv_requisition_no);
        edt_from_date_select = findViewById(R.id.edt_from_date_select);
        edt_date_to_select = findViewById(R.id.edt_date_to_select);
        ed_reason = findViewById(R.id.ed_reason);
        ed_approval = findViewById(R.id.ed_approval);
        tv_total_days = findViewById(R.id.tv_total_days);
        spinner_rqst_status = findViewById(R.id.spinner_rqst_status);
        spinner_od_duty_type = findViewById(R.id.spinner_od_duty_type);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

//        img_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_save.setClickable(false);
        btn_save.setAlpha(0.7f);

        loadData();
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_save:
               saveData();
               break;
//           case R.id.img_back:
           case R.id.btn_cancel:
               Intent intent_subordinate_odlist = new Intent(this,SubordinateOutdoorListActivity.class);
               intent_subordinate_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent_subordinate_odlist);
               break;
           default:
               break;
       }
    }



    //===========Code to get data from api using volley and load data, starts==========
    public void loadData(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"od/request/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomSubordinateOutdoorListAdapter.od_request_id+"/2";
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(SubordinateOutDoorRequestActivity.this, "Loading", "Please wait...", true, false);
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

        //------Spinner code starts(added on 16th sept), as per requirments------
        List<String> spinnerList_od_duty_type = new ArrayList<>();
        spinnerList_od_duty_type.add("Work From Home");

        ArrayAdapter<String> adapter_od_duty_type = new ArrayAdapter<String>(SubordinateOutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_od_duty_type);
        adapter_od_duty_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_od_duty_type.setAdapter(adapter_od_duty_type);
        spinner_od_duty_type.setSelection(1);
        spinner_od_duty_type.setEnabled(false);
        //------Spinner code ends(added on 16th sept)-----
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                JSONObject jsonObject2 = jsonObject.getJSONObject("fields");
                tv_emp_name.setText(jsonObject2.getString("employee_name"));
//                tv_emp_name.setTextColor(Color.parseColor("#b2b2b2"));
                tv_requisition_no.setText(jsonObject2.getString("od_request_no"));
//                tv_requisition_no.setTextColor(Color.parseColor("#b2b2b2"));

                od_status = jsonObject2.getString("od_status");


                //---from/to date code starts----
//                DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //--on 13th jan
                DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //--on 21st jan
                DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inputTextFromDate = jsonObject2.getString("from_date");
                String inputTextToDate = jsonObject2.getString("to_date");
                Date fromDate = null, toDate = null;
                try {
                    fromDate = inputFormat.parse(inputTextFromDate);
                    toDate = inputFormat.parse(inputTextToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputTextFromDate = outputFormat.format(fromDate);
                String outputTextToDate = outputFormat.format(toDate);
                edt_from_date_select.setText(outputTextFromDate);

                edt_from_date_select.setEnabled(false);
                edt_from_date_select.setFocusable(false);
//                edt_from_date_select.setTextColor(Color.parseColor("#b2b2b2"));

                edt_date_to_select.setText(outputTextToDate);
                edt_date_to_select.setEnabled(false);
                edt_date_to_select.setFocusable(false);
//                edt_date_to_select.setTextColor(Color.parseColor("#b2b2b2"));
                //---from/to date code ends----

//                tv_total_days.setText(jsonObject2.getString("total_days")); //---commented on 26th July
                int total_days = jsonObject2.getInt("total_days");
                tv_total_days.setText(String.valueOf(total_days));
//                tv_total_days.setTextColor(Color.parseColor("#b2b2b2"));

                ed_reason.setText(jsonObject2.getString("description"));
                ed_reason.setEnabled(false);
                ed_reason.setFocusable(false);
//                ed_reason.setTextColor(Color.parseColor("#b2b2b2"));

                ed_approval.setText(jsonObject2.getString("supervisor_remark"));


                if(jsonObject2.getInt("approved_by_id") == 0){

                    ed_approval.setClickable(true);
//                    ed_approval.setTextColor(Color.parseColor("#7b7a7a"));
//                    ed_approval.setFocusable(true);

                    spinner_rqst_status.setClickable(false);
                    spinner_rqst_status.setEnabled(false);
                    spinner_rqst_status.setAlpha(0.5f);
                    //--------Spinner code starts(making default select disabled)------
                    /*List<String> spinnerList_request_status_disabled_mode = new ArrayList<>();
                    spinnerList_request_status_disabled_mode.add("Select Status");

                    ArrayAdapter<String> adapter_disabled_mode = new ArrayAdapter<String>(SubordinateOutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_request_status_disabled_mode);
                    adapter_disabled_mode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_rqst_status.setAdapter(adapter_disabled_mode);
                    spinner_rqst_status.setSelection(1);
                    spinner_rqst_status.setEnabled(false);*/
                    //--------Spinner code ends------

                    ed_approval.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(!ed_approval.getText().toString().isEmpty()){

                                spinner_rqst_status.setClickable(true);
                                spinner_rqst_status.setEnabled(true);
                                spinner_rqst_status.setAlpha(1.0f);

                                if(spinner_rqst_status.getSelectedItem() != null){
                                    btn_save.setClickable(true);
                                    btn_save.setAlpha(1.0f);
                                }


                            }else if(ed_approval.getText().toString().isEmpty()){


                                spinner_rqst_status.setClickable(false);
                                spinner_rqst_status.setEnabled(false);
                                spinner_rqst_status.setAlpha(0.5f);


                                btn_save.setClickable(false);
                                btn_save.setAlpha(0.7f);

                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            /*if(ed_approval.getText().toString().trim().equals("")){
                                btn_save.setClickable(false);
                                btn_save.setAlpha(0.7f);

                            }*/

                        }
                    });
                    /*ed_approval.setClickable(true);
                    ed_approval.setFocusable(true);
                    spinner_rqst_status.setClickable(true);*/

                    //--------Spinner code starts------
                    List<String> spinnerList_request_status = new ArrayList<>();
                    spinnerList_request_status.add("Approved");
                   /* spinnerList_request_status.add("Cancelled");
                    spinnerList_request_status.add("Returned");*/
                    spinnerList_request_status.add("Canceled");
                    spinnerList_request_status.add("Return");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SubordinateOutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_request_status);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_rqst_status.setAdapter(adapter);
                    spinner_rqst_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i == -1){

                                btn_save.setClickable(false);
                                btn_save.setAlpha(0.7f);

                            }else {
                                btn_save.setClickable(true);
                                btn_save.setAlpha(1.0f);

//                                Toast.makeText(getApplicationContext(),spinner_rqst_status.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    //--------Spinner code ends------


                }else {
                    ed_approval.setClickable(false);
                    ed_approval.setFocusable(false);
                    ed_approval.setEnabled(false);
//                    ed_approval.setTextColor(Color.parseColor("#b2b2b2"));

                    spinner_rqst_status.setClickable(false);
                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

                    //--------Spinner code starts------
                    List<String> spinnerList_request_status1 = new ArrayList<>();
                    spinnerList_request_status1.add(jsonObject2.getString("od_status"));

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SubordinateOutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_request_status1);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_rqst_status.setAdapter(adapter1);
                    spinner_rqst_status.setSelection(1);
                    spinner_rqst_status.setEnabled(false);
                    //--------Spinner code ends------
                }

            }else if(jsonObject1.getString("status").contentEquals("false")){
               /* ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code to get data from api and load data, ends==========

    //===========code to save data, starts==============
    public void saveData(){
        final JSONObject DocumentElementobj = new JSONObject();
        try {
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("od_request_id", Integer.parseInt(CustomSubordinateOutdoorListAdapter.od_request_id));
            DocumentElementobj.put("od_request_no", tv_requisition_no.getText().toString());
            DocumentElementobj.put("employee_id", CustomSubordinateOutdoorListAdapter.subordinate_employee_id);
            DocumentElementobj.put("from_date", edt_from_date_select.getText().toString());
            DocumentElementobj.put("to_date", edt_date_to_select.getText().toString());
            DocumentElementobj.put("total_days", tv_total_days.getText().toString());
            DocumentElementobj.put("description", ed_reason.getText().toString());
            DocumentElementobj.put("supervisor_remark", ed_approval.getText().toString());
           /* if(spinner_rqst_status.getSelectedItem().toString().contentEquals("Returned")){
                DocumentElementobj.put("od_status", "Return");
            }else  if(spinner_rqst_status.getSelectedItem().toString().contentEquals("Cancelled")){
                DocumentElementobj.put("od_status", "Canceled");
            }else  if(spinner_rqst_status.getSelectedItem().toString().contentEquals("Approved")){
                DocumentElementobj.put("od_status", "Approved");
            }*/
            DocumentElementobj.put("od_status", spinner_rqst_status.getSelectedItem().toString());


            DocumentElementobj.put("approved_by_id", userSingletonModel.getEmployee_id());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DocumentElementobj.put("approved_date", sdf.format(new Date()));

            Log.d("testingApproval-=>",DocumentElementobj.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }


        //------calling api to save data
        JsonObjectRequest request_json = null;
        String URL = Url.BASEURL()+"od/request/save";
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
//                                        Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(),spinner_rqst_status.getSelectedItem().toString()+" OD Duty Request",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SubordinateOutDoorRequestActivity.this, SubordinateOutdoorListActivity.class);
                                        startActivity(intent);
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
    //===========code to save data, ends==============


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
