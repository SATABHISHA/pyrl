package org.wrkplan.payroll.MyLeaveApplication2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.Detail_Subordinate;
import org.wrkplan.payroll.Model.Details;
import org.wrkplan.payroll.Model.LeaveType;
import org.wrkplan.payroll.Model.Load_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication.MyLeaveApplicationActivity;
import org.wrkplan.payroll.MyLeaveApplicationModel.LeaveApplication;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SubOrdinateLeaveApplication.SubordinateLeaveApplicationActivity;
import org.wrkplan.payroll.popup_Activity.POPUP;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.wrkplan.payroll.Model.Details.leave_status;


public class MyLeaveApplication2Activity extends AppCompatActivity implements View.OnClickListener {
    SimpleDateFormat currentDte=new SimpleDateFormat("dd-MMM-yyyy");
    int flag,pos;
    Date date=new Date();
    Spinner check_leave_spinner;
    String currntdate;
    String select_item;
    ImageView img_list;
    TextView leave_title,txt_total_number,tv_supervisor_remark,tv_final_supervisor_remark,tv_details1;
    RelativeLayout submit_type,rb_type,spiner_approved,txt_approved,txt_check_leave,subordinate_super,leave_super
            ,subordinate_final,leave_final,supervisor_details,leave_details,rl_to_sub,rl_to,rl_from_sub,rl_from;

    Button btn_cancel,btn_save,btn_cancel_sub,btn_save_sub;
    AlertDialog.Builder builder;
    RadioButton rb1,rb2;
    RadioGroup radioGroup;
    Boolean save=false,submit=false;
    TextView tv_sick_leave,tv_matarnal_leave,tv_paternal_leave,tv_comp_off;
    TextView tv_casual_leave;
    TextView tv_earn_leav ;
    ArrayList<Load_Spinner_Model> load_spinner_models=new ArrayList<>();
    ArrayList<String> spinnerlist = new ArrayList<>();

    TextView txt_from_date,txt_to_date,txt_from_date1,txt_to_date1,txt_application_status;
    ImageView cal1,cal2;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar c = Calendar.getInstance();
    // final Calendar c2 = Calendar.getInstance();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    //  SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");

    //ArrayList<LeaveType> arrayList=new ArrayList<>();
    ArrayList<LeaveType> arrayList=new ArrayList<>();
    ArrayList<Details> arrayList1=new ArrayList<>();
    ArrayList<Detail_Subordinate> arrayList2=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    Spinner spinner1,spinner2;
    Date SecondDate,firstDate;

    String leaveID;
    TextView txt_emp_name,txt_leave_status1;
    EditText ed_details,ed_supervisor_remark,ed_final_supervisor_remark;
    JSONObject jsonBody=new JSONObject();
    JSONObject jsonBody_Subordinate=new JSONObject();
    Button bt_ok;
    LinearLayout  ll_save_cancel,llone_save_cancel;
    public static String status_message = "";





    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if(Url.isSubordinateLeaveApplication == true){
            Intent intent = new Intent(MyLeaveApplication2Activity.this, SubordinateLeaveApplicationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            if (txt_leave_status1.getText().toString().equals("Save") || txt_leave_status1.getText().toString().equals("Return")) {
                Url.isSubordinateLeaveApplication = false;
                builder.setMessage("You may lost any unsaved data. Do you really want to cancel?")
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Url.isSubordinateLeaveApplication = false;
                                Url.isMyLeaveApplication = true;

                                Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                            finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button

//                                Intent intent=new Intent(MyLeaveApplication2Activity.this,MyLeaveApplication2Activity.class);
//                                startActivity(intent);
//                                finish();
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Cancel!");
                alert.show();
            } else {
                Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//            finish();

            }
        }




    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leave_application_2);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ll_save_cancel=findViewById(R.id.ll_save_cancel);
        llone_save_cancel=findViewById(R.id.llone_save_cancel);


        txt_emp_name=findViewById(R.id.txt_emp_name);
        txt_leave_status1=findViewById(R.id.txt_leave_status1);
        ed_details=findViewById(R.id.ed_details);
        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        radioGroup=findViewById(R.id.radioGroup);
        tv_final_supervisor_remark=findViewById(R.id.tv_final_supervisor_remark);
        tv_supervisor_remark=findViewById(R.id.tv_supervisor_remark);
        tv_details1=findViewById(R.id.tv_details1);
        txt_application_status=findViewById(R.id.txt_application_status);



        ed_supervisor_remark=findViewById(R.id.ed_supervisor_remark);
        ed_final_supervisor_remark=findViewById(R.id.ed_final_supervisor_remark);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        img_list=findViewById(R.id.img_list);
        leave_title=findViewById(R.id.leave_title);
        txt_total_number=findViewById(R.id.txt_total_number);
        submit_type=findViewById(R.id.submit_type);
        rb_type=findViewById(R.id.rb_type);
        txt_approved=findViewById(R.id.txt_approved);
        spiner_approved=findViewById(R.id.spiner_approved);
        txt_check_leave=findViewById(R.id.txt_check_leave);
        subordinate_super=findViewById(R.id.subordinate_super);
        leave_super=findViewById(R.id.leave_super);
        subordinate_final=findViewById(R.id.subordinate_final);
        leave_final=findViewById(R.id.leave_final);

        supervisor_details=findViewById(R.id.supervisor_details);
        leave_details=findViewById(R.id.leave_details);

        btn_cancel=findViewById(R.id.btn_cancel);
        btn_save=findViewById(R.id.btn_save);
        btn_cancel_sub=findViewById(R.id.btn_cancel_sub);
        btn_save_sub=findViewById(R.id.btn_save_sub);


        txt_from_date=findViewById(R.id.txt_from_date);
        txt_to_date=findViewById(R.id.txt_to_date);
        txt_from_date1=findViewById(R.id.txt_from_date1);
        txt_to_date1=findViewById(R.id.txt_to_date1);
        cal1=findViewById(R.id.cal1);
        cal2=findViewById(R.id.cal2);

        rl_from=findViewById(R.id.rl_from);
        rl_to=findViewById(R.id.rl_to);
        rl_from_sub=findViewById(R.id.rl_from_sub);
        rl_to_sub=findViewById(R.id.rl_to_sub);

        builder = new AlertDialog.Builder(this);

        currntdate=(currentDte.format(date));
        // Toast.makeText(this, currntdate, Toast.LENGTH_SHORT).show();


        if(Url.isNew==true)
        {
            rb1.setChecked(true);
        }
        else
        {
            rb1.setChecked(false);
        }
        if(Url.isSubordinateLeaveApplication==true)
        {


            leave_title.setText("Subordinate Leave Application");
            submit_type.setVisibility(View.VISIBLE);
            rb_type.setVisibility(View.GONE);
            spiner_approved.setVisibility(View.VISIBLE);
            txt_approved.setVisibility(View.GONE);
            txt_check_leave.setVisibility(View.GONE);
            ll_save_cancel.setVisibility(View.GONE);
            llone_save_cancel.setVisibility(View.VISIBLE);

            subordinate_super.setVisibility(View.VISIBLE);
            leave_super.setVisibility(View.GONE);

            subordinate_final.setVisibility(View.VISIBLE);
            leave_final.setVisibility(View.GONE);
            supervisor_details.setVisibility(View.VISIBLE);
            leave_details.setVisibility(View.GONE);

            rl_from_sub.setVisibility(View.VISIBLE);
            rl_from.setVisibility(View.GONE);
            rl_to_sub.setVisibility(View.VISIBLE);
            rl_to.setVisibility(View.GONE);
            GetEditForm2();
            arrayList2.clear();



        }
        /*else if(Url.isSubordinateLeaveApplication == false){
            leave_title.setText("My Leave Application");
            rb_type.setVisibility(View.VISIBLE);
            submit_type.setVisibility(View.GONE);
            spiner_approved.setVisibility(View.GONE);
            txt_approved.setVisibility(View.VISIBLE);
            txt_check_leave.setVisibility(View.VISIBLE);
            leave_details.setVisibility(View.VISIBLE);

            subordinate_super.setVisibility(View.VISIBLE); //--changed by sr
            leave_super.setVisibility(View.VISIBLE);
            subordinate_final.setVisibility(View.GONE);
            leave_final.setVisibility(View.VISIBLE);
//            rl_from_sub.setVisibility(View.GONE);
            rl_from_sub.setVisibility(View.VISIBLE); //--changed by sr
            rl_from.setVisibility(View.VISIBLE);
//            rl_to_sub.setVisibility(View.GONE);
            rl_to_sub.setVisibility(View.VISIBLE); //--changed by sr
            rl_to.setVisibility(View.VISIBLE);
            ll_save_cancel.setVisibility(View.VISIBLE);
            llone_save_cancel.setVisibility(View.GONE);

            GetEditForm();


        } */else{
            leave_title.setText("My Leave Application");
            rb_type.setVisibility(View.VISIBLE);
            submit_type.setVisibility(View.GONE);
            spiner_approved.setVisibility(View.GONE);
            txt_approved.setVisibility(View.VISIBLE);
            txt_check_leave.setVisibility(View.VISIBLE);
            leave_details.setVisibility(View.VISIBLE);

//            subordinate_super.setVisibility(View.GONE);
            subordinate_super.setVisibility(View.VISIBLE);
            leave_super.setVisibility(View.VISIBLE);
            subordinate_final.setVisibility(View.GONE);
            leave_final.setVisibility(View.VISIBLE);
            rl_from_sub.setVisibility(View.GONE);
//            rl_from_sub.setVisibility(View.VISIBLE);

            rl_from.setVisibility(View.VISIBLE);
            rl_to_sub.setVisibility(View.GONE);
//            rl_to_sub.setVisibility(View.VISIBLE);
            rl_to.setVisibility(View.VISIBLE);
            ll_save_cancel.setVisibility(View.VISIBLE);
            llone_save_cancel.setVisibility(View.GONE);

            ed_supervisor_remark.setEnabled(false); //by sr
            ed_final_supervisor_remark.setEnabled(false); // by sr

//            GetEditForm();


        }
        if(Url.isSubordinateLeaveApplication=true) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, getResources()
                    .getStringArray(R.array.Leave_Status));//setting the country_array to spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                    try {

                        select_item =arg0.getItemAtPosition(position).toString();
                        // Toast.makeText(MyLeaveApplication2Activity.this, select_item, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
        txt_emp_name.setText(userSingletonModel.full_employee_name);


        cal1.setOnClickListener(this);
        cal2.setOnClickListener(this);

        GetLeave();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arrayList);

        if(Url.islistclicked) {
            GetEditForm();
            Url.islistclicked=false;
            arrayList1.clear();
        }



        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save=true;

            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit=true;
            }
        });



        btn_save_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MyLeaveApplication2Activity.this, "IT IS SAVE", Toast.LENGTH_SHORT).show();
                try {

                    jsonBody_Subordinate.put("corp_id", userSingletonModel.corporate_id);
                    jsonBody_Subordinate.put("appliction_id", Url.currtent_application_id);
//                    jsonBody_Subordinate.put("leave_id", leaveID); //--commented by satabhisha
                    jsonBody_Subordinate.put("leave_id", Integer.parseInt(leaveID));
//                    jsonBody_Subordinate.put("employee_id", userSingletonModel.employee_id);
                    jsonBody_Subordinate.put("employee_id", 0);
                    jsonBody_Subordinate.put("from_date", txt_from_date1.getText().toString());
                    jsonBody_Subordinate.put("to_date", txt_to_date1.getText().toString());
                    jsonBody_Subordinate.put("total_days", txt_total_number.getText().toString());
                    jsonBody_Subordinate.put("description", tv_details1.getText().toString());
                    jsonBody_Subordinate.put("supervisor_remark", ed_supervisor_remark.getText().toString());
                    jsonBody_Subordinate.put("leave_status", select_item);
                    jsonBody_Subordinate.put("approved_by_id",userSingletonModel.user_id);
                    jsonBody_Subordinate.put("approved_date", currntdate);
                    jsonBody_Subordinate.put("supervisor1_id",Url.supervisor1_id );
                    jsonBody_Subordinate.put("supervisor2_id",Url.supervisor2_id);

                } catch (Exception e) {
                    Toast.makeText(MyLeaveApplication2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                Log.d("jsonObjecttest-=>",jsonBody_Subordinate.toString());
                String url = Url.BASEURL() + "leave/application/save";
                try {
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url,
                            new JSONObject(jsonBody_Subordinate.toString()), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message=response.getString("message");
                                status_message = "Leave application "+select_item;
                                Toast.makeText(MyLeaveApplication2Activity.this, message, Toast.LENGTH_SHORT).show();
                                Log.d("sds",message);
                                Url.isSubordinateLeaveApplication=true;
                                Intent intent=new Intent(MyLeaveApplication2Activity.this,SubordinateLeaveApplicationActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        txt_check_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyLeaveApplication2Activity.this);
                builder.setTitle("");
                final  View custom=getLayoutInflater().inflate(R.layout.popup,null);


                tv_casual_leave =  custom.findViewById(R.id.tv_casual_leave);
                tv_earn_leav =  custom.findViewById(R.id.tv_earn_leav);
                tv_sick_leave= custom.findViewById(R.id.tv_sick_leave);
                tv_matarnal_leave= custom.findViewById(R.id.tv_matarnal_leave);
                tv_paternal_leave= custom.findViewById(R.id.tv_paternal_leave);
                tv_comp_off=custom.findViewById(R.id.tv_comp_off);
                bt_ok=custom.findViewById(R.id.bt_ok);
                check_leave_spinner=custom.findViewById(R.id.spinner1);


                builder.setView(custom);
                final AlertDialog dialog = builder.create();
                dialog.show();

//                arrayList.add("2020 - 2021");
//                arrayList.add("2019 - 2020");
//                arrayList.add("2018 - 2019");
                Load_Spinner_Data();


                check_leave_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // String item = parent.getItemAtPosition(position).toString();
                        //  Toast.makeText(parent.getContext(), "Selected: " + item,          Toast.LENGTH_LONG).show();

                        //  GetData(item.replace(" ", ""));

                        String item=load_spinner_models.get(position).getFinancial_year_code();
                        GetData(item);

                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
            private void Load_Spinner_Data() {

                String url=Url.BASEURL() + "finyear/" + "list/" + userSingletonModel.corporate_id;
                StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("fin_years");
                            spinnerlist.clear();
                            load_spinner_models.clear();
                            for(int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jb1=jsonArray.getJSONObject(i);
                                String financial_year_code=jb1.getString("financial_year_code");
                                String calender_year=jb1.getString("calender_year");
                                spinnerlist.add(calender_year);
                                Load_Spinner_Model spinnerModel=new Load_Spinner_Model();
                                spinnerModel.setFinancial_year_code(financial_year_code);
                                spinnerModel.setCalender_year(calender_year);
                                // arrayList.add(jb1.getString("calender_year"));
                                load_spinner_models.add(spinnerModel);


                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MyLeaveApplication2Activity.this,android.R.layout.simple_spinner_item, spinnerlist);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            check_leave_spinner.setAdapter(arrayAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MyLeaveApplication2Activity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();

                    }
                });
                Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);
            }


            private void GetData(String year_code) {
                String url= Url.BASEURL() + "leave/" + "balance/" + userSingletonModel.corporate_id+"/"+userSingletonModel.employee_id+"/"+year_code;




                StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject jb1=jsonObject.getJSONObject("leave_balance");
                            userSingletonModel.setCasual_leave(jb1.getString("casual_leave"));
                            userSingletonModel.setEarn_leave(jb1.getString("earn_leave"));
                            userSingletonModel.setSick_leave(jb1.getString("sick_leave"));
                            userSingletonModel.setComp_off(jb1.getString("comp_off"));
                            userSingletonModel.setMaternal_leave(jb1.getString("maternal_leave"));
                            userSingletonModel.setPaternal_leave(jb1.getString("paternal_leave"));

                            tv_casual_leave.setText(userSingletonModel.getCasual_leave());
                            tv_earn_leav.setText(userSingletonModel.getEarn_leave());
                            tv_sick_leave.setText(userSingletonModel.getSick_leave());
                            tv_comp_off.setText(userSingletonModel.getComp_off());
                            tv_matarnal_leave.setText(userSingletonModel.getMaternal_leave());
                            tv_paternal_leave.setText(userSingletonModel.getPaternal_leave());



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);
            }



        });




        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (rb1.isChecked() == true || rb2.isChecked() == true) {


                try {

                    jsonBody.put("corp_id", userSingletonModel.corporate_id);

                    // Log.d("sdss",jsonBody.getString("corp_id"));
                    if (Url.isNew == true) {
                        jsonBody.put("appliction_id", 0);


                    } else {
                        jsonBody.put("appliction_id", Url.currtent_application_id);


                    }

//                    jsonBody.put("leave_id", leaveID); //commented by satabhisha
                    jsonBody.put("leave_id", Integer.parseInt(leaveID));
//                    jsonBody.put("employee_id", userSingletonModel.employee_id); //commented by satabhisha on 9th April
                    jsonBody.put("employee_id", Integer.parseInt(userSingletonModel.employee_id));

                    //---from/to date code starts----
                    DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String inputTextFromDate = txt_from_date.getText().toString();
                    String inputTextToDate = txt_to_date.getText().toString();
                    Date fromDate = null, toDate = null;
                    try {
                        fromDate = inputFormat.parse(inputTextFromDate);
                        toDate = inputFormat.parse(inputTextToDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outputTextFromDate = outputFormat.format(fromDate);
                    String outputTextToDate = outputFormat.format(toDate);

//                edt_date_to_select.setTextColor(Color.parseColor("#b2b2b2"));
                    //---from/to date code ends----

                    jsonBody.put("from_date", outputTextFromDate);
                    jsonBody.put("to_date", outputTextToDate);
//                    jsonBody.put("total_days", txt_total_number.getText().toString()); //commented by sr
                    jsonBody.put("total_days", Double.parseDouble(txt_total_number.getText().toString()));
                    jsonBody.put("description", ed_details.getText().toString());
                    jsonBody.put("supervisor_remark", ed_supervisor_remark.getText().toString());
//                if (save == true) {
//                    jsonBody.put("leave_status", rb1.getText().toString());
//                }
                    jsonBody.put("leave_status",rb1.getText().toString());

                    if (submit == true) {
                        jsonBody.put("leave_status", rb2.getText().toString());
                        status_message = "Submitted";
                    }else{
                        status_message = "Saved";
                    }
                    jsonBody.put("approved_by_id", 0);
                    jsonBody.put("approved_date", "");
                    jsonBody.put("supervisor1_id", 0);
                    jsonBody.put("supervisor2_id", 0);

                    Log.d("from_date", jsonBody.getString("from_date"));
                    Log.d("leave_status", jsonBody.getString("leave_status"));
                    Log.d("supervisor2_id", jsonBody.getString("supervisor2_id"));

                    if (jsonBody == null) {
                        Toast.makeText(MyLeaveApplication2Activity.this, "OBject is null", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.toString();

                }

                Log.d("jsonbody-=>",jsonBody.toString());
                String url = Url.BASEURL() + "leave/application/save";

                try {
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonBody.toString())
                            , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // JSONObject jsonObject=new JSONObject(String.valueOf(response));

                                String message = response.getString("message");
//                                Toast.makeText(MyLeaveApplication2Activity.this, message, Toast.LENGTH_SHORT).show(); //as per discussion on 13jan 21
//                                Toast.makeText(getApplicationContext(), status_message, Toast.LENGTH_LONG).show();
                                Log.d("hgsdfhg",status_message);
                                Log.d("responseData",response.toString());
                                Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                finish(); // commented by sr
                                Toast.makeText(getApplicationContext(),"Leave application "+status_message,Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MyLeaveApplication2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MyLeaveApplication2Activity.this, "Couldnot connect to the server", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//        }
//        else {
//            //Toast.makeText(MyLeaveApplication2Activity.this, "Please choice the application type", Toast.LENGTH_SHORT).show();
//            builder.setMessage("Please select the application type!")
//                    .setCancelable(false)
//
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    });
//
//            //Creating dialog box
//            AlertDialog alert = builder.create();
//            //Setting the title manually
//            alert.setTitle("Alert!");
//            alert.show();
                // }
            }


        });





        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Url.isSubordinateLeaveApplication == true)
                {
                    Intent intent=new Intent(MyLeaveApplication2Activity.this, SubordinateLeaveApplicationActivity.class);
                    startActivity(intent);

                }
                else {


                    Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);

                    startActivity(intent);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_leave_status1.getText().toString().equals("Save") || txt_leave_status1.getText().toString().equals("Return")
                        ||txt_leave_status1.getText().toString().equals("")) {

                    builder.setMessage("You may lost any unsaved data. Do you really want to cancel?")
                            .setCancelable(false)

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Url.isSubordinateLeaveApplication = false;
                                    Url.isMyLeaveApplication=true;

                                    Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button

//                                Intent intent=new Intent(MyLeaveApplication2Activity.this,MyLeaveApplication2Activity.class);
//                                startActivity(intent);
//                                finish();
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Cancel!");
                    alert.show();
                }

                else
                {
                    Intent intent = new Intent(MyLeaveApplication2Activity.this, MyLeaveApplicationActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

        });

        btn_cancel_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (leave_status.equals("Save") || leave_status.equals("Return")) {


                Intent intent = new Intent(MyLeaveApplication2Activity.this, SubordinateLeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                // }
            }
        });

    }

    private void GetEditForm2() {
        String url= Url.BASEURL() + "leave/" + "application/"+ "detail/"+userSingletonModel.corporate_id+"/"+Url.currtent_application_id+"/"+2;

        Log.d("urlleave->",url);
        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("leaveData-=>",response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject  jsonObject1=jsonObject.getJSONObject("fields");
                    String employee_name=jsonObject1.getString("employee_name");
                    String from_date=jsonObject1.getString("from_date");
                    String to_date=jsonObject1.getString("to_date");
                    String total_days=jsonObject1.getString("total_days");
                    String description=jsonObject1.getString("description");
                    String leave_status=jsonObject1.getString("leave_status");
                    String supervisor1_id=jsonObject1.getString("supervisor1_id");
                    String supervisor1_name=jsonObject1.getString("supervisor1_name");
                    String supervisor2_id=jsonObject1.getString("supervisor2_id");
                    String supervisor2_name=jsonObject1.getString("supervisor2_name");
                    String leave_id=jsonObject1.getString("leave_id");

                    ed_supervisor_remark.setText(jsonObject1.getString("supervisor_remark"));
                    if(leave_status.contentEquals("Approved") ||
                            leave_status.contentEquals("Return") ||
                            leave_status.contentEquals("Canceled")) {
                        ed_supervisor_remark.setEnabled(false);
                        spinner2.setEnabled(false);

                        if(leave_status.contentEquals("Approved")){
                            spinner2.setSelection(0);
                        }else if(leave_status.contentEquals("Return")){
                            spinner2.setSelection(1);
                        }else if(leave_status.contentEquals("Canceled")){
                            spinner2.setSelection(2);
                        }

                    }else if(leave_status.contentEquals("Submit")){
                        ed_supervisor_remark.setEnabled(true);
                        ed_supervisor_remark.setFocusableInTouchMode(true);
                        ed_supervisor_remark.setClickable(true);
                        spinner2.setEnabled(true);

                    }/*else{
                        ed_supervisor_remark.setEnabled(true); //by sr
                        spinner2.setEnabled(true);
                    }*/


                    Detail_Subordinate ds=new Detail_Subordinate();
                    ds.setEmployee_name(employee_name);
                    ds.setFrom_date(from_date);

                    ds.setTo_date(to_date);
                    ds.setTotal_days(total_days);
                    ds.setDescription(description);
                    ds.setLeave_status(leave_status);
                    ds.setSupervisor1_id(supervisor1_id);
                    ds.setSupervisor1_name(supervisor1_name);
                    ds.setSupervisor2_id(supervisor2_id);
                    ds.setSupervisor2_name(supervisor2_name);
                    ds.setLeave_id(leave_id);

                    arrayList2.add(ds);

                    txt_emp_name.setText(arrayList2.get(0).getEmployee_name());
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //
                    Date myDate = null;
                    Date myDate1 = null;
                    try {
                        myDate = sdf.parse(arrayList2.get(0).getFrom_date());
                        myDate1 = sdf.parse(arrayList2.get(0).getTo_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf.applyPattern("dd-MMM-yyyy");
//                    sdf.applyPattern("d MMM YYYY"); //commented by satabhisha
                    String sMyDate = sdf.format(myDate);
                    String sMyDate1 = sdf.format(myDate1);
                    txt_from_date1.setText(sMyDate); //unnecessary but not deleting for sureity
                    txt_from_date.setText(sMyDate);
                    rb1.setChecked(false);
                    txt_to_date1.setText(sMyDate1);
                    Log.d("ddff",txt_to_date1.toString());
                    txt_total_number.setText(arrayList2.get(0).getTotal_days());
                    tv_details1.setText(arrayList2.get(0).getDescription());
                    txt_application_status.setText(arrayList2.get(0).getLeave_status());
                    //tv_supervisor_remark.setText(arrayList2.get(0).getSupervisor1_name());
                    tv_supervisor_remark.setText("By "+arrayList2.get(0).getSupervisor1_name());
                    tv_final_supervisor_remark.setText("By "+arrayList2.get(0).getSupervisor2_name());

                    if(userSingletonModel.user_id.equals(supervisor1_id))
                    {

//                        ed_supervisor_remark.setEnabled(true);  //---commented by satabhisha
                        ed_final_supervisor_remark.setEnabled(false);

                    }
                    else if(userSingletonModel.user_id.equals(supervisor2_id))
                    {

//                        ed_supervisor_remark.setEnabled(false);  //---commented by satabhisha
                        ed_final_supervisor_remark.setEnabled(true);

                    }


                    if(leave_status.equals("Approved")||leave_status.equals("Canceled")||leave_status.equals("Return"))
                    {
                        Url.isSubordinateLeaveApplication=true;
                        btn_save_sub.setEnabled(false);
                    }
                    else if(leave_status.equals("Submit"))
                    {
                        Url.isSubordinateLeaveApplication=true;
                        btn_save_sub.setEnabled(true);
                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyLeaveApplication2Activity.this, "could't connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);
    }


    private void GetEditForm() {

        if(!arrayList1.isEmpty()){
            arrayList1.clear();
        }
        String url= Url.BASEURL() + "leave/" + "application/"+ "detail/"+userSingletonModel.corporate_id+"/"+Url.currtent_application_id+"/"+1;
//        Log.d("sbsv",url);
//        Log.d("sbsv", String.valueOf(Url.islistclicked));

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("leaveemptest-=>",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("fields");
                    // for (int i=0;i<jsonObject1.length();i++)

                    String employee_name=jsonObject1.getString("employee_name");
                    String leave_id=jsonObject1.getString("leave_id");
                    String from_date=jsonObject1.getString("from_date");
                    String to_date=jsonObject1.getString("to_date");
                    String total_days=jsonObject1.getString("total_days");
                    String description=jsonObject1.getString("description");
                    String leave_status=jsonObject1.getString("leave_status");

                    String supervisor1_name=jsonObject1.getString("supervisor1_name");
                    String supervisor2_name=jsonObject1.getString("supervisor2_name");

                    tv_supervisor_remark.setText("By "+supervisor1_name);
                    tv_final_supervisor_remark.setText("By "+supervisor2_name);


                    Details details=new Details();
                    details.setEmployee_name(employee_name);
                    details.setLeave_id(leave_id);
                    details.setFrom_date(from_date);
                    details.setTo_date(to_date);
                    details.setTotal_days(total_days);
                    details.setDescription(description);
                    details.setLeave_status(leave_status);
                    details.setSupervisor(jsonObject1.getString("supervisor_remark"));
                    Log.d("svcsh>>",employee_name);

                    arrayList1.add(details);






                    txt_emp_name.setText(arrayList1.get(0).getEmployee_name());
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.ENGLISH);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.ENGLISH); //--changed on 19th jan
                    Date myDate = null;
                    Date myDate1 = null;
                    try {
                        myDate = sdf.parse(arrayList1.get(0).getFrom_date());
                        myDate1 = sdf.parse(arrayList1.get(0).getTo_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf.applyPattern("dd-MMM-yyyy");
//                    sdf.applyPattern("d MMM YYYY");
                    String sMyDate = sdf.format(myDate);
                    String sMyDate1 = sdf.format(myDate1);
                    txt_from_date.setText(sMyDate);
                    txt_to_date.setText(sMyDate1);

                    txt_total_number.setText(arrayList1.get(0).getTotal_days());

                    ed_details.setText(arrayList1.get(0).getDescription());
                    ed_supervisor_remark.setEnabled(false);
                    ed_supervisor_remark.setVisibility(View.VISIBLE);
                    ed_supervisor_remark.setText(arrayList1.get(0).getSupervisor());


                    // txt_leave_status1.setText(arrayList1.get(0).getLeave_status());
                    if(arrayList1.get(0).getLeave_status().equals("Canceled"))
                    {
                        txt_leave_status1.setText(arrayList1.get(0).getLeave_status());
                        // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                        txt_leave_status1.setTextColor(getResources().getColor(R.color.cancel));
                    }
                    else if(arrayList1.get(0).getLeave_status().equals("Canceled"))
                    {
                        txt_leave_status1.setText(arrayList1.get(0).getLeave_status());
                        // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                        txt_leave_status1.setTextColor(getResources().getColor(R.color.returned));
                    }
                    else

                        txt_leave_status1.setText(arrayList1.get(0).getLeave_status());



                    if(leave_status.equals("Approved")|| leave_status.equals("Submit")||leave_status.equals("Canceled"))
                    {
                        btn_save.setEnabled(false);
                        txt_check_leave.setEnabled(false);
                      //by sr, starts
                        rb2.setChecked(true);
                        rb2.setClickable(false);
                        rb1.setClickable(false);

                        spinner1.setEnabled(false);
//                        spinner2.setEnabled(false);


                        //--by sr ends
                    }
                    else if(leave_status.equals("Return")|| leave_status.equals("Save"))
                    {
                        Url.isSubordinateLeaveApplication=false;
                        btn_save.setEnabled(true);

                        spinner1.setEnabled(true);
//                        spinner2.setEnabled(true);
                        if (leave_status.equals("Save")) {
                        rb1.setChecked(true);
                    }else if(leave_status.equals("Return")){
                            rb2.setChecked(true);
                        }
                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyLeaveApplication2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    // Log.d("ghfg",e.toString());
                }


            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyLeaveApplication2Activity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();


            }
        });
        Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);
    }

    private void DteDifference() {
        long days = TimeUnit.DAYS.convert(SecondDate.getTime() - firstDate.getTime(), TimeUnit.MILLISECONDS) + 1;
        txt_total_number.setText(String.valueOf(days));
    }

    private void GetLeave() {
        String url= Url.BASEURL() + "leave/" + "type/"+userSingletonModel.corporate_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("types");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);
                        String id=jb1.getString("id");
                        String name=jb1.getString("name");

                        LeaveType leaveType=new LeaveType();
                        leaveType.setId(id);
                        leaveType.setName(name);

                        arrayList.add(leaveType);

                    }
                    ArrayAdapter<LeaveType> arrayAdapter = new ArrayAdapter<LeaveType>(MyLeaveApplication2Activity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(arrayAdapter);
                    for(int i=0;i<arrayAdapter.getCount();i++)
                    {
                        String val= String.valueOf(arrayAdapter.getItem(i));
                        if(val.equals(Url.LeaveType))
                        {

                            flag=1;
                            pos=i;
                            break;
                        }

                    }
                    if(flag==1)
                    {
                        spinner1.setSelection(pos);


                    }

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            LeaveType leaveType = (LeaveType) parent.getSelectedItem();

////                            {
                            leaveID = getID(leaveType);
                            //Toast.makeText(MyLeaveApplication2Activity.this, "ID: " + leaveID, Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(MyLeaveApplication2Activity.this).add(stringRequest);


    }
    private String getID(LeaveType leaveType) {

        return leaveType.getId();

    }

    @Override
    public void onClick(View v) {
        if(v==cal1)
        {


            showDatePickerDialog();

        }

        if(v==cal2) {
            showDatePickerDialog1();

        }

    }

    private void showDatePickerDialog1() {

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SecondDate=c.getTime();
                        String currentDateString1 = simpleDateFormat.format(c.getTime());
                        txt_to_date.setText(currentDateString1);
                        DteDifference();



                        // txt_to_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog1.getDatePicker().setMinDate(firstDate.getTime());

        datePickerDialog1.show();

    }

    private void showDatePickerDialog() {

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        firstDate=c.getTime();
                        String currentDateString = simpleDateFormat.format(c.getTime());
                        txt_from_date.setText(currentDateString);





                        //txt_from_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
}

