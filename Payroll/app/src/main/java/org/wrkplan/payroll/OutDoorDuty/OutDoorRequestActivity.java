package org.wrkplan.payroll.OutDoorDuty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONArray;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class OutDoorRequestActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    TextView tv_requisition_no, tv_emp_name, tv_total_days, tv_actiontype_caption;
    EditText edt_from_date_select, edt_date_to_select, ed_reason, ed_approval;
    RadioGroup groupradio;
    Spinner spinner_rqst_status, spinner_od_duty_type;
    ImageButton imgBtnCalenderFrom, imgBtnCalenderTo;
    Button btn_save, btn_cancel;
    final Calendar myCalendarFromDate = Calendar.getInstance();
    final Calendar myCalendarToDate = Calendar.getInstance();
    RadioGroup radioGroup;
    RadioButton radia_id1, radia_id2;
    Integer od_request_id = 0; //--it would be 0 for 1st time creation and the value will change if the status is Saved
    String od_status;
    Integer flag_datefield_check = 1;
    public String from_date_select_api_data, to_date_select_api_data;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_request);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        img_back=findViewById(R.id.img_back);
        tv_emp_name = findViewById(R.id.tv_emp_name);
        tv_requisition_no = findViewById(R.id.tv_requisition_no);
        edt_from_date_select = findViewById(R.id.edt_from_date_select);
        edt_date_to_select = findViewById(R.id.edt_date_to_select);
        ed_reason = findViewById(R.id.ed_reason);
        ed_approval = findViewById(R.id.ed_approval);
        tv_total_days = findViewById(R.id.tv_total_days);
        tv_actiontype_caption = findViewById(R.id.tv_actiontype_caption);
        groupradio = findViewById(R.id.groupradio);
        imgBtnCalenderFrom = findViewById(R.id.imgBtnCalenderFrom);
        imgBtnCalenderTo = findViewById(R.id.imgBtnCalenderTo);
        spinner_rqst_status = findViewById(R.id.spinner_rqst_status);
        spinner_od_duty_type = findViewById(R.id.spinner_od_duty_type);
        radioGroup = findViewById(R.id.groupradio);
        radia_id1 = findViewById(R.id.radia_id1);
        radia_id2 = findViewById(R.id.radia_id2);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        imgBtnCalenderFrom.setOnClickListener(this);
        imgBtnCalenderTo.setOnClickListener(this);
//        img_back.setOnClickListener(this);

        if(OutdoorListActivity.new_create_yn == 0) {
            radioGroup.setOnCheckedChangeListener(this); //--added on 21st May
            loadData();
        }else if(OutdoorListActivity.new_create_yn == 1){
            radioGroup.setOnCheckedChangeListener(this);
            set_data_functionalities();
        }

        //--disabling button if date field is blank, added on 29th may
        btn_save.setClickable(false);
        btn_save.setAlpha(0.5f);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_save:
              /* if(Double.parseDouble(tv_total_days.getText().toString())>-1) {
                   saveData();
               }else{
                   Toast.makeText(getApplicationContext(), "\"To Date\" should be graeter than \"From Date\"", Toast.LENGTH_LONG).show();
               }*/

               //added on 13th jan
               SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
               Date strDate = null;
               try {
                   strDate = sdf.parse(edt_from_date_select.getText().toString());
               } catch (ParseException e) {
                   e.printStackTrace();
               }
               if (strDate.getTime() > System.currentTimeMillis()-(1000 * 60 * 60 * 24)) {
//                   Toast.makeText(getApplicationContext(),"Eureka",Toast.LENGTH_LONG).show();
                   Log.d("test","Eurwka");
                   if(Double.parseDouble(tv_total_days.getText().toString())>-1) {
                       saveData();
                   }else{
                       Toast.makeText(getApplicationContext(), "\"To Date\" should be greater than \"From Date\"", Toast.LENGTH_LONG).show();
                   }

               }
               else{
                   Toast.makeText(getApplicationContext(),"Cannot submit back dated OD Request",Toast.LENGTH_LONG).show();
                   Log.d("test","no");
               }
               break;
//           case R.id.img_back:
           case R.id.btn_cancel:

               if(OutdoorListActivity.new_create_yn == 1){
                   if(!edt_from_date_select.getText().toString().isEmpty() || !ed_reason.getText().toString().isEmpty()){
                       androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(OutDoorRequestActivity.this);
                       builder.setMessage("All unsaved data will be lost. Still want to cancel?")
                               .setCancelable(false)
                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                       Intent intent_odlist = new Intent(OutDoorRequestActivity.this,OutdoorListActivity.class);
                                       intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent_odlist);
                                       dialog.cancel();
                                   }
                               })
                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               });
                       androidx.appcompat.app.AlertDialog alert = builder.create();
                       alert.show();
                   }else{
                       Intent intent_odlist = new Intent(this,OutdoorListActivity.class);
                       intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent_odlist);
                   }
               }else {

                   Intent intent_odlist = new Intent(this, OutdoorListActivity.class);
                   intent_odlist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent_odlist);
               }
               break;
           case R.id.imgBtnCalenderFrom:
//               edt_date_to_select.setText(""); //--told to disable this
               calendarPicker(myCalendarFromDate,edt_from_date_select, "from_date");
             /* if (calendarPicker(myCalendarFromDate,edt_from_date_select) == true){
                  edt_date_to_select.setText(edt_from_date_select.getText().toString());
              }*/

               break;

           case R.id.imgBtnCalenderTo:
               calendarPicker(myCalendarToDate,edt_date_to_select, "to_date");

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
        return dayDifference;
    }
    //---------Date Difference function, code ends-------

    //---------Calendar code starts--------
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
                    if(od_status != "Save" || od_status != "Returned") {
                        if (edt_date_to_select.getText().toString().isEmpty()) {
                            edt_date_to_select.setText(edt_from_date_select.getText().toString());
                        }
                    }

                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);

                    Log.d("date diff check-=>", get_date_difference(edt_from_date_select.getText().toString(), edt_date_to_select.getText().toString()));
                    if (!edt_from_date_select.getText().toString().isEmpty()) {
//                    edt_date_to_select.setText(edt_from_date_select.getText().toString()); //added on 29th May
                        tv_total_days.setText(get_date_difference(edt_from_date_select.getText().toString(), edt_date_to_select.getText().toString()));
                    } else if (edt_from_date_select.getText().toString().isEmpty()) {
                        tv_total_days.setText("");
                    }
                }

            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(OutDoorRequestActivity.this, date, myCalendar
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


    //------random function, starts----
    public int getRandom(){
        int min = 1;
        int max = 999999;
        //Generate random double value from 1 to 999999
        int random_int = (int)(Math.random() * (max - min + 1) + min);
        return random_int;
    }
    //------random function, ends----
    //=========code to set_data_functionalities, starts==========
    public void set_data_functionalities(){

        //------Spinner code starts------
        List<String> spinnerList_od_duty_type = new ArrayList<>();
        spinnerList_od_duty_type.add("Work From Home");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_od_duty_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_od_duty_type.setAdapter(adapter);
        spinner_od_duty_type.setSelection(1);
        //------Spinner code ends-----

        tv_requisition_no.setText("OD/"+Calendar.getInstance().get(Calendar.YEAR)+"/"+getRandom());
        tv_emp_name.setText(userSingletonModel.getFull_employee_name());
        radia_id2.setChecked(true);

        ed_approval.setEnabled(false);
        ed_approval.setFocusable(false);
    }
    //=========code to set_data_functionalities, ends==========



    //===========Code to get data from api using volley and load data, starts==========
    public void loadData(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"od/request/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomOutdoorListAdapter.od_request_id+"/1";
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(OutDoorRequestActivity.this, "Loading", "Please wait...", true, false);
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
                tv_emp_name.setText(jsonObject2.getString("employee_name"));

                od_status = jsonObject2.getString("od_status");

                if(jsonObject2.getString("od_status").contentEquals("Save")){
                    od_request_id = jsonObject2.getInt("od_request_id"); //--added on 29th May, it would be 0 for new creation

                    imgBtnCalenderTo.setVisibility(View.VISIBLE);
                    imgBtnCalenderFrom.setVisibility(View.VISIBLE);

                    edt_from_date_select.setEnabled(true);
                    edt_from_date_select.setFocusable(true);

                    edt_date_to_select.setEnabled(true);
                    edt_date_to_select.setFocusable(true);

                    ed_reason.setEnabled(true);
                    ed_reason.setFocusable(true);

                    ed_approval.setEnabled(false);
                    ed_approval.setFocusable(false);

                    tv_actiontype_caption.setVisibility(View.VISIBLE);
                    groupradio.setVisibility(View.VISIBLE);
                    radia_id1.setChecked(true); //--added on 21st may

                    //------Spinner code starts(added on 21st may)------
                    List<String> spinnerList_od_duty_type = new ArrayList<>();
                    spinnerList_od_duty_type.add("Work From Home");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_od_duty_type);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_od_duty_type.setAdapter(adapter);
                    spinner_od_duty_type.setSelection(1);
                    //------Spinner code ends(added on 21st may)-----

                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);
                }else if(jsonObject2.getString("od_status").contentEquals("Return")){

                    od_request_id = jsonObject2.getInt("od_request_id"); //--added on 29th May, it would be 0 for new creation

                    imgBtnCalenderTo.setVisibility(View.VISIBLE);
                    imgBtnCalenderFrom.setVisibility(View.VISIBLE);

                    edt_from_date_select.setEnabled(true);
                    edt_from_date_select.setFocusable(true);

                    edt_date_to_select.setEnabled(true);
                    edt_date_to_select.setFocusable(true);

                    ed_reason.setEnabled(true);
                    ed_reason.setFocusable(true);

                    ed_approval.setEnabled(false);
                    ed_approval.setFocusable(false);

                    tv_actiontype_caption.setVisibility(View.VISIBLE);
                    groupradio.setVisibility(View.VISIBLE);
                    radia_id1.setChecked(false);
                    radia_id1.setClickable(false);
                    radia_id2.setChecked(true); //--added on 21st may

                    //------Spinner code starts(added on 21st may)------
                    List<String> spinnerList_od_duty_type = new ArrayList<>();
                    spinnerList_od_duty_type.add("Work From Home");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_od_duty_type);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_od_duty_type.setAdapter(adapter);
                    spinner_od_duty_type.setSelection(1);
                    //------Spinner code ends(added on 21st may)-----

                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);

                    //--------Spinner code starts------
                    List<String> spinnerList_request_status = new ArrayList<>();
                    spinnerList_request_status.add(jsonObject2.getString("od_status"));

                    ArrayAdapter<String> adapter_rqst_status = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_request_status);
                    adapter_rqst_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_rqst_status.setAdapter(adapter_rqst_status);
                    spinner_rqst_status.setSelection(1);
                    spinner_rqst_status.setEnabled(false);
                    //--------Spinner code ends------
                }else {
                    imgBtnCalenderTo.setVisibility(View.GONE);
                    imgBtnCalenderFrom.setVisibility(View.GONE);

                    edt_from_date_select.setEnabled(false);
                    edt_from_date_select.setFocusable(false);

                    edt_date_to_select.setEnabled(false);
                    edt_date_to_select.setFocusable(false);

                    ed_reason.setEnabled(false);
                    ed_reason.setFocusable(false);

                    ed_approval.setEnabled(false);
                    ed_approval.setFocusable(false);

                    tv_actiontype_caption.setVisibility(View.GONE);
                    groupradio.setVisibility(View.GONE);

                    btn_save.setClickable(false);
                    btn_save.setAlpha(0.7f);

                    //--------Spinner code starts------
                    List<String> spinnerList_request_status = new ArrayList<>();
                    spinnerList_request_status.add(jsonObject2.getString("od_status"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_request_status);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_rqst_status.setAdapter(adapter);
                    spinner_rqst_status.setSelection(1);
                    spinner_rqst_status.setEnabled(false);
                    //--------Spinner code ends------

                    //------Spinner code starts(added on 25th may)------
                    List<String> spinnerList_od_duty_type = new ArrayList<>();
                    spinnerList_od_duty_type.add("Work From Home");

                    ArrayAdapter<String> adapter_od_duty_type = new ArrayAdapter<String>(OutDoorRequestActivity.this, android.R.layout.simple_spinner_item, spinnerList_od_duty_type);
                    adapter_od_duty_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_od_duty_type.setAdapter(adapter_od_duty_type);
                    spinner_od_duty_type.setSelection(1);
                    spinner_od_duty_type.setEnabled(false);
                    //------Spinner code ends(added on 25th may)-----
                }

                //---from/to date code starts----
//                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //again changed pn 21st jan
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
                from_date_select_api_data = outputTextFromDate;

                edt_date_to_select.setText(outputTextToDate);
                to_date_select_api_data = outputTextToDate;

                //---from/to date code ends----

//                tv_total_days.setText(jsonObject2.getString("total_days")); //---commenting on 25th June
                int total_days = jsonObject2.getInt("total_days");
                tv_total_days.setText(String.valueOf(total_days));
                ed_reason.setText(jsonObject2.getString("description"));

                ed_approval.setText(jsonObject2.getString("supervisor_remark"));
//                ed_approval.setTextColor(Color.parseColor("#b2b2b2"));


                /*tv_actiontype_caption.setVisibility(View.GONE);
                groupradio.setVisibility(View.GONE);*/


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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    //===========code to save data, starts==============
    public void saveData(){
        final JSONObject DocumentElementobj = new JSONObject();
        try {
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
//            DocumentElementobj.put("od_request_id", 0);
            DocumentElementobj.put("od_request_id", od_request_id);
            DocumentElementobj.put("od_request_no", tv_requisition_no.getText().toString());
            DocumentElementobj.put("employee_id", userSingletonModel.getEmployee_id());
//            DocumentElementobj.put("employee_id", 52);
            DocumentElementobj.put("from_date", edt_from_date_select.getText().toString());
            DocumentElementobj.put("to_date", edt_date_to_select.getText().toString());
            DocumentElementobj.put("total_days", tv_total_days.getText().toString());
            DocumentElementobj.put("description", ed_reason.getText().toString());
//            DocumentElementobj.put("supervisor_remark", ""); //--commented on 4th June
            DocumentElementobj.put("supervisor_remark", ed_approval.getText().toString());

            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId!=-1) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                DocumentElementobj.put("od_status", radioButton.getText().toString());
            }
            DocumentElementobj.put("approved_by_id", 0);
            DocumentElementobj.put("approved_date", "");

            Log.d("testing-=>",DocumentElementobj.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }


        //------calling api to save data
        JsonObjectRequest request_json = null;
        String URL = Url.BASEURL()+"od/request/save";
        Log.d("testsaveurl-=>",URL);
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
//                                       Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                        int selectedId = radioGroup.getCheckedRadioButtonId();
                                        if(selectedId!=-1) {
                                            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                                            DocumentElementobj.put("od_status", radioButton.getText().toString());
                                            if(radioButton.getText().toString().contentEquals("Save")){
                                                Toast.makeText(getApplicationContext(),"Saved OD Duty Request",Toast.LENGTH_LONG).show();
                                            }else if(radioButton.getText().toString().contentEquals("Submit")){
                                                Toast.makeText(getApplicationContext(),"Submitted OD Duty Request",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        Intent intent = new Intent(OutDoorRequestActivity.this, OutdoorListActivity.class);
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
