package org.wrkplan.payroll.Lta;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Model.LtaDocumentsModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.CustomOutdoorListAdapter;
import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LtaRequestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_document_view, tv_lta_no, tv_employee_name, tv_from_year_lta_limit, tv_to_year_lta_limit, tv_total_lta_amount, tv_remaining_lta_amount, tv_lta_requisition_status;
    Button btn_back, btn_cancel, btn_return, btn_approve, btn_submit, btn_save;
    EditText edt_from_date_select, edt_to_date_select, ed_lta_amount, ed_detail, ed_approved_amount, ed_supervisor_remark, ed_final_supervisor_remark;
    ImageButton imgBtnCalenderFrom, imgBtnCalenderTo;
    TextView tv_total_days, tv_docs, tv_dayscount_alert_title;
    final Calendar myCalendarFromDate = Calendar.getInstance();
    final Calendar myCalendarToDate = Calendar.getInstance();
    Integer flag_datefield_check = 1;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    public static ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayList = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_request);

        btn_back = findViewById(R.id.btn_back);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_return = findViewById(R.id.btn_return);
        btn_approve = findViewById(R.id.btn_approve);
        btn_submit = findViewById(R.id.btn_submit);
        btn_save = findViewById(R.id.btn_save);
        tv_document_view = findViewById(R.id.tv_document_view);
        tv_docs = findViewById(R.id.tv_docs);
        tv_lta_no = findViewById(R.id.tv_lta_no);
        tv_employee_name = findViewById(R.id.tv_employee_name);
        tv_from_year_lta_limit = findViewById(R.id.tv_from_year_lta_limit);
        tv_to_year_lta_limit = findViewById(R.id.tv_to_year_lta_limit);
        tv_total_lta_amount = findViewById(R.id.tv_total_lta_amount);
        tv_remaining_lta_amount = findViewById(R.id.tv_remaining_lta_amount);
        tv_dayscount_alert_title = findViewById(R.id.tv_dayscount_alert_title);

        tv_total_days = findViewById(R.id.tv_total_days);

        ed_lta_amount = findViewById(R.id.ed_lta_amount);
        ed_detail = findViewById(R.id.ed_detail);
        edt_from_date_select = findViewById(R.id.edt_from_date_select);
        edt_to_date_select = findViewById(R.id.edt_to_date_select);
        imgBtnCalenderFrom = findViewById(R.id.imgBtnCalenderFrom);
        imgBtnCalenderTo = findViewById(R.id.imgBtnCalenderTo);

        tv_lta_requisition_status = findViewById(R.id.tv_lta_requisition_status);
        ed_approved_amount = findViewById(R.id.ed_approved_amount);
        ed_supervisor_remark = findViewById(R.id.ed_supervisor_remark);
        ed_final_supervisor_remark = findViewById(R.id.ed_final_supervisor_remark);

        imgBtnCalenderFrom.setOnClickListener(this);
        imgBtnCalenderTo.setOnClickListener(this);

        tv_document_view.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_approve.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        LoadButtons();
        tv_docs.setText(String.valueOf(ltaDocumentsModelArrayList.size())+" Doc(s)");
        if (LtaListActivity.new_create_yn == 1) {
            imgBtnCalenderTo.setVisibility(View.VISIBLE);
            imgBtnCalenderFrom.setVisibility(View.VISIBLE);

            edt_from_date_select.setEnabled(true);
            edt_from_date_select.setFocusable(true);

            edt_to_date_select.setEnabled(true);
            edt_to_date_select.setFocusable(true);

            ed_lta_amount.setEnabled(true);
            ed_lta_amount.setFocusable(true);

            ed_detail.setEnabled(true);
            ed_detail.setFocusable(true);

            ed_approved_amount.setEnabled(false);
            ed_approved_amount.setFocusable(false);

            ed_supervisor_remark.setEnabled(false);
            ed_supervisor_remark.setFocusable(false);

            ed_final_supervisor_remark.setEnabled(false);
            ed_final_supervisor_remark.setFocusable(false);

            buttonEnableDisable();

        }
        if (LtaListActivity.new_create_yn == 0) {
            loadData(LtaListActivity.lta_application_id);
        }
    }

    //=====function to enable/disable buttons according to field check, code starts====
    public void buttonEnableDisable(){
        btn_save.setClickable(false);
        btn_save.setAlpha(0.7f);

        btn_submit.setClickable(false);
        btn_submit.setAlpha(0.7f);

        imgBtnCalenderFrom.setClickable(false);
        imgBtnCalenderFrom.setAlpha(0.7f);

        imgBtnCalenderTo.setClickable(false);
        imgBtnCalenderTo.setAlpha(0.7f);


        ed_lta_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!ed_lta_amount.getText().toString().isEmpty()){
                    imgBtnCalenderFrom.setClickable(true);
                    imgBtnCalenderFrom.setAlpha(1.0f);
                    if(!edt_from_date_select.getText().toString().trim().isEmpty() &&
                    !edt_to_date_select.getText().toString().trim().isEmpty()){
                        btn_save.setClickable(true);
                        btn_save.setAlpha(1.0f);

                        btn_submit.setClickable(true);
                        btn_submit.setAlpha(1.0f);
                    }
                }else if(ed_lta_amount.getText().toString().isEmpty()){
                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

                    btn_submit.setClickable(false);
                    btn_submit.setAlpha(0.7f);

                    imgBtnCalenderFrom.setClickable(false);
                    imgBtnCalenderFrom.setAlpha(0.7f);
                }
            }
        });

        edt_from_date_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!edt_from_date_select.getText().toString().isEmpty()){

                    imgBtnCalenderTo.setClickable(true);
                    imgBtnCalenderTo.setAlpha(1.0f);
                    if(!edt_from_date_select.getText().toString().trim().isEmpty() &&
                            !edt_to_date_select.getText().toString().trim().isEmpty()){
                        btn_save.setClickable(true);
                        btn_save.setAlpha(1.0f);

                        btn_submit.setClickable(true);
                        btn_submit.setAlpha(1.0f);
                    }
                }else if(edt_from_date_select.getText().toString().trim().isEmpty()){
                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

                    btn_submit.setClickable(false);
                    btn_submit.setAlpha(0.7f);

                    imgBtnCalenderTo.setClickable(false);
                    imgBtnCalenderTo.setAlpha(0.7f);
                }
            }
        });

        edt_to_date_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!edt_to_date_select.getText().toString().trim().isEmpty()){
                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);

                    btn_submit.setClickable(true);
                    btn_submit.setAlpha(1.0f);
                }else if(edt_to_date_select.getText().toString().trim().isEmpty()){
                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

                    btn_submit.setClickable(false);
                    btn_submit.setAlpha(0.7f);
                }
            }
        });
    }
    //=====function to enable/disable buttons according to field check, code ends====
    //=====onClick code starts=====
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_document_view:
                startActivity(new Intent(LtaRequestActivity.this,LtaDocumentsActivity.class));
                break;
            case R.id.btn_back:
                if(!ltaDocumentsModelArrayList.isEmpty()){
                    ltaDocumentsModelArrayList.clear();
                }
                startActivity(new Intent(LtaRequestActivity.this,LtaListActivity.class));
                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_return:
                break;
            case R.id.btn_approve:
                break;
            case R.id.btn_submit:
                if (LtaListActivity.new_create_yn == 1) {
                    makeJsonObjectAndSaveDataToServer("0", edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString(), tv_total_days.getText().toString(), ed_lta_amount.getText().toString(), "0.0", ed_detail.getText().toString(), ed_supervisor_remark.getText().toString(), "Submitted", "0");
                }else{
                    makeJsonObjectAndSaveDataToServer(LtaListActivity.lta_application_id, edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString(), tv_total_days.getText().toString(), ed_lta_amount.getText().toString(), "0.0", ed_detail.getText().toString(), ed_supervisor_remark.getText().toString(), "Submitted", "0");
                }
                break;
            case R.id.btn_save:
                if (LtaListActivity.new_create_yn == 1) {
                    makeJsonObjectAndSaveDataToServer("0", edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString(), tv_total_days.getText().toString(), ed_lta_amount.getText().toString(), "0.0", ed_detail.getText().toString(), ed_supervisor_remark.getText().toString(), "Saved", "0");
                }else{
                    makeJsonObjectAndSaveDataToServer(LtaListActivity.lta_application_id, edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString(), tv_total_days.getText().toString(), ed_lta_amount.getText().toString(), "0.0", ed_detail.getText().toString(), ed_supervisor_remark.getText().toString(), "Saved", "0");
                }
                break;
            case R.id.imgBtnCalenderFrom:
                calendarPicker(myCalendarFromDate,edt_from_date_select, "from_date");
                break;
            case R.id.imgBtnCalenderTo:
                calendarPicker(myCalendarToDate,edt_to_date_select, "to_date");
                break;
        }
    }
    //=====onClick code ends=====
    //----function to load buttons acc to the logic, code starts
    public void LoadButtons(){


        if (LtaListActivity.EmployeeType == "Employee"){
            tv_employee_name.setText(userSingletonModel.getFull_employee_name());
//            LabelNavBarTitle.text = "My Advance Requisition"
//            btn_reason_select_type.isUserInteractionEnabled = true
//            btn_reason_select_type.alpha = 1.0
            if (LtaListActivity.mediclaim_status.contentEquals("")){
                btn_back.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);




                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true

                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/

            }
            if (LtaListActivity.mediclaim_status.contentEquals("Saved")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);



                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Submitted")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);



                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false

                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
            if (LtaListActivity.mediclaim_status.contentEquals("Returned")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);

                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
        }
        if (LtaListActivity.EmployeeType == "Supervisor"){
//            LabelNavBarTitle.text = "Subordinate Advance Requisition"
//            btn_reason_select_type.isUserInteractionEnabled = false
//            btn_reason_select_type.alpha = 0.6
            if (LtaListActivity.mediclaim_status.contentEquals("Submitted")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.VISIBLE);
                btn_return.setVisibility(View.VISIBLE);

                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false
                TxtApprovedAmount.isUserInteractionEnabled = true
                TxtViewApprovalRemark.isUserInteractionEnabled = true*/
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Returned")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);




                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
        }


    }

    //----function to load buttons acc to the logic, code ends

    //---------Calendar code starts--------
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
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");

            //Setting dates
            date1 = dates.parse(fromDate);
            date2 = dates.parse(toDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
//            long difference = date1.getTime() - date2.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            if (date2.getTime() < date1.getTime()) {
                Toast.makeText(getApplicationContext(), "To Date should be graeter than \"From Date\"", Toast.LENGTH_LONG).show();
                flag_datefield_check = 0;

//                    edt_date_to_select.setText("");
//                    edt_date_to_select.setText(edt_from_date_select.getText().toString());
//                    calendarPicker(myCalendarToDate, edt_date_to_select);
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please enter valid date")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dialog.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/
                dayDifference = Long.toString(-(differenceDates+1));
                dayDifference = dayDifference;
            }else if(date2.getTime() >= date1.getTime()){
                //Convert long to String
                flag_datefield_check = 1;
                dayDifference = Long.toString(differenceDates+1);
            }
//            dayDifference = Long.toString(differenceDates+1);

            Log.e("HERE","HERE: " + dayDifference);


        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
        if (Integer.parseInt(dayDifference) < 5){
           tv_dayscount_alert_title.setTextColor(Color.parseColor("#FF0000"));
        }else{
            tv_dayscount_alert_title.setTextColor(Color.parseColor("#717171"));
        }
        return dayDifference;
    }
    //---------Date Difference function, code ends-------
    public void calendarPicker(final Calendar myCalendar, final EditText editText, final String button_status){
        try {
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(myCalendar, editText);

                    //---for toDate checking, added on 29th May
//                    get_date_difference(edt_from_date_select.getText().toString(), edt_date_to_select.getText().toString(), button_status);
                   /* if(LtaListActivity.mediclaim_status != "Saved" || LtaListActivity.mediclaim_status != "Returned") {
                        if (edt_date_to_select.getText().toString().isEmpty()) {
                            edt_date_to_select.setText(edt_from_date_select.getText().toString());
                        }
                    }*/

                   /* btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);*/ //--commented on 13h July


                    if (!edt_from_date_select.getText().toString().isEmpty() && !edt_to_date_select.getText().toString().isEmpty()) {
                        Log.d("date diff check-=>", get_date_difference(edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString()));
//                    edt_date_to_select.setText(edt_from_date_select.getText().toString()); //added on 29th May
                        tv_total_days.setText(get_date_difference(edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString()));
                    } else if (edt_from_date_select.getText().toString().isEmpty()) {
                        tv_total_days.setText("");
                    }
                }

            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(LtaRequestActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            // to set Max Date
//                myCalendar.set(2019, -1, 1);
            long now = System.currentTimeMillis() - 1000;
//               datePickerDialog.getDatePicker().setMaxDate(now); //---set max date

            Calendar cal = Calendar.getInstance();
//               cal.add(Calendar.MONTH, -2);
//        cal.add(Calendar.MONTH, 1);
            Date preToPreMonthDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = sdf.format(preToPreMonthDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date mDate = sdf1.parse(strDate);
                long timeInMilliseconds = mDate.getTime();
                datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);
                System.out.println("Date in milli :: " + timeInMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePickerDialog.show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Internal Error occurred",Toast.LENGTH_LONG).show();
        }
    }

    private void updateLabel(Calendar myCalendar,EditText editText) {
//        String myFormat = "MM/dd/yy"; //In which you need put here
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
//        loadLocationData(sdf.format(myCalendar.getTime()));
    }
    //---------Calendar code ends--------

    //=========function to make json object and save data, code starts======
    public void makeJsonObjectAndSaveDataToServer(String lta_application_id, String date_from, String date_to, String total_days, String lta_amount, String approved_lta_amount, String description, String supervisor_remark, String lta_application_status, String approved_by_id){

        final JSONObject DocumentElementobj = new JSONObject();
        JSONArray req = new JSONArray();
        JSONObject reqObjdt = new JSONObject();
        try {
                for (int i = 0; i < ltaDocumentsModelArrayList.size(); i++) {
                    JSONObject reqObj = new JSONObject();
//                reqObj.put("od_duty_task_head_id", Integer.parseInt(outDoorTaskModelArrayList.get(i).getOd_duty_task_detail_id()));
                    reqObj.put("file_name", ltaDocumentsModelArrayList.get(i).getLta_filename());
                    reqObj.put("file_base64", ltaDocumentsModelArrayList.get(i).getLta_file_base64());
                    req.put(reqObj);
                }
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("lta_application_id", Integer.parseInt(lta_application_id));
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("date_from", date_from);
            DocumentElementobj.put("date_to", date_to);
            DocumentElementobj.put("total_days", Integer.parseInt(total_days));
            DocumentElementobj.put("lta_amount", Double.parseDouble(lta_amount));
            DocumentElementobj.put("approved_lta_amount", Double.parseDouble(approved_lta_amount));
            DocumentElementobj.put("description", description);
            DocumentElementobj.put("supervisor_remark", supervisor_remark);
            DocumentElementobj.put("lta_application_status", lta_application_status);
            DocumentElementobj.put("approved_by_id", Integer.parseInt(approved_by_id));
            /*DocumentElementobj.put("entry_user", LoginActivity.entry_user);
            DocumentElementobj.put("saved_from_mobile_app", 1);*/
            DocumentElementobj.put("documents", req);

            Log.d("jsontesting-=>",DocumentElementobj.toString());
            //------calling api to save data
            JsonObjectRequest request_json = null;
            String URL = Url.BASEURL()+"lta/save";
            Log.d("ltaurl-=>",URL);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //=========function to make json object and save data, code ends======

    //=====function to load data, code starts========
    public void loadData(String application_id){

//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"lta/detail/"+userSingletonModel.getCorporate_id()+"/"+ application_id;
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(LtaRequestActivity.this, "Loading", "Please wait...", true, false);
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
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                JSONObject jsonObject2 = jsonObject.getJSONObject("fields");
                tv_employee_name.setText(jsonObject2.getString("employee_name"));

                tv_lta_requisition_status.setText(jsonObject2.getString("application_status"));
                tv_lta_no.setText(jsonObject2.getString("lta_application_no"));
                tv_from_year_lta_limit.setText(jsonObject2.getString("year_from_limit"));
                tv_to_year_lta_limit.setText(jsonObject2.getString("year_to_limit"));
                tv_total_lta_amount.setText(jsonObject2.getString("lta_total_limit"));
                tv_remaining_lta_amount.setText(jsonObject2.getString("lta_used_amount"));
                ed_lta_amount.setText(jsonObject2.getString("lta_amount"));
                ed_detail.setText(jsonObject2.getString("description"));
                edt_from_date_select.setText(jsonObject2.getString("date_from"));
                edt_to_date_select.setText(jsonObject2.getString("date_to"));
                ed_approved_amount.setText(jsonObject2.getString("approved_lta_amount"));
                ed_supervisor_remark.setText(jsonObject2.getString("supervisor_remark"));
                ed_final_supervisor_remark.setText(jsonObject2.getString("final_supervisor_remark"));
                int total_days = jsonObject2.getInt("total_days");
                tv_total_days.setText(String.valueOf(total_days));

                if(jsonObject2.getString("application_status").contentEquals("Saved")){
//                    od_request_id = jsonObject2.getInt("od_request_id"); //--added on 29th May, it would be 0 for new creation

                    imgBtnCalenderTo.setVisibility(View.VISIBLE);
                    imgBtnCalenderFrom.setVisibility(View.VISIBLE);

                    edt_from_date_select.setEnabled(true);
                    edt_from_date_select.setFocusable(true);

                    edt_to_date_select.setEnabled(true);
                    edt_to_date_select.setFocusable(true);

                    ed_lta_amount.setEnabled(true);
                    ed_lta_amount.setFocusable(true);

                    ed_detail.setEnabled(true);
                    ed_detail.setFocusable(true);

                    ed_approved_amount.setEnabled(false);
                    ed_approved_amount.setFocusable(false);

                    ed_supervisor_remark.setEnabled(false);
                    ed_supervisor_remark.setFocusable(false);

                    ed_final_supervisor_remark.setEnabled(false);
                    ed_final_supervisor_remark.setFocusable(false);


                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);
                }else if(jsonObject2.getString("application_status").contentEquals("Returned")){

//                    od_request_id = jsonObject2.getInt("od_request_id"); //--added on 29th May, it would be 0 for new creation

                    imgBtnCalenderTo.setVisibility(View.VISIBLE);
                    imgBtnCalenderFrom.setVisibility(View.VISIBLE);

                    edt_from_date_select.setEnabled(true);
                    edt_from_date_select.setFocusable(true);

                    edt_to_date_select.setEnabled(true);
                    edt_to_date_select.setFocusable(true);

                    ed_lta_amount.setEnabled(true);
                    ed_lta_amount.setFocusable(true);

                    ed_detail.setEnabled(true);
                    ed_detail.setFocusable(true);

                    ed_approved_amount.setEnabled(false);
                    ed_approved_amount.setFocusable(false);

                    ed_supervisor_remark.setEnabled(false);
                    ed_supervisor_remark.setFocusable(false);

                    ed_final_supervisor_remark.setEnabled(false);
                    ed_final_supervisor_remark.setFocusable(false);


                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);


                }else if(jsonObject2.getString("application_status").contentEquals("Approved")){

//                    od_request_id = jsonObject2.getInt("od_request_id"); //--added on 29th May, it would be 0 for new creation

                    imgBtnCalenderTo.setVisibility(View.GONE);
                    imgBtnCalenderFrom.setVisibility(View.GONE);

                    edt_from_date_select.setEnabled(false);
                    edt_from_date_select.setFocusable(false);

                    edt_to_date_select.setEnabled(false);
                    edt_to_date_select.setFocusable(false);

                    ed_lta_amount.setEnabled(false);
                    ed_lta_amount.setFocusable(false);

                    ed_detail.setEnabled(false);
                    ed_detail.setFocusable(false);

                    ed_approved_amount.setEnabled(false);
                    ed_approved_amount.setFocusable(false);

                    ed_supervisor_remark.setEnabled(false);
                    ed_supervisor_remark.setFocusable(false);

                    ed_final_supervisor_remark.setEnabled(false);
                    ed_final_supervisor_remark.setFocusable(false);


                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);


                }else {
                    imgBtnCalenderTo.setVisibility(View.GONE);
                    imgBtnCalenderFrom.setVisibility(View.GONE);

                    edt_from_date_select.setEnabled(false);
                    edt_from_date_select.setFocusable(false);

                    edt_to_date_select.setEnabled(false);
                    edt_to_date_select.setFocusable(false);

                    ed_lta_amount.setEnabled(false);
                    ed_lta_amount.setFocusable(false);

                    ed_detail.setEnabled(false);
                    ed_detail.setFocusable(false);

                    ed_approved_amount.setEnabled(true);
                    ed_approved_amount.setFocusable(true);

                    ed_supervisor_remark.setEnabled(true);
                    ed_supervisor_remark.setFocusable(true);

                    ed_final_supervisor_remark.setEnabled(true);
                    ed_final_supervisor_remark.setFocusable(true);

                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

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
    //=====function to load data, code ends========
}
