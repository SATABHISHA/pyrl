package org.wrkplan.payroll.Reports;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.Load_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReportHomeListActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_view_task, btn_view_salary_slip;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<Load_Spinner_Model> load_spinner_models = new ArrayList<>();
    ArrayList<String>arrayList=new ArrayList<>();
    ArrayList<String> arrayListMonth = new ArrayList<>();
    public static String report_html = "";
    public static String year_code = "", month_name = "";
    private File pdfFile = null;
    private static LinkedList<Bitmap> pdfBitmapList = new LinkedList<>();
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_report_list);
        btn_view_task = findViewById(R.id.btn_view_task);
        img_back=findViewById(R.id.img_back);
        btn_view_salary_slip=findViewById(R.id.btn_view_salary_slip);

        btn_view_task.setOnClickListener(this);
        btn_view_salary_slip.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent(ReportHomeListActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_view_task:
                LayoutInflater li = LayoutInflater.from(ReportHomeListActivity.this);
                final View dialog = li.inflate(R.layout.activity_home_pf_report_popup, null);
                final Spinner spinner_year =  dialog.findViewById(R.id.spinner_year);
                final String[] item = new String[1];
                TextView tv_button_continue = dialog.findViewById(R.id.tv_button_continue);
                ImageView img_view_close = dialog.findViewById(R.id.img_view_close);



                AlertDialog.Builder alert = new AlertDialog.Builder(ReportHomeListActivity.this);
                alert.setView(dialog);
                alert.setCancelable(false);
                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Load_Spinner_Data(spinner_year);
                spinner_year.setSelection(1);
                tv_button_continue.setAlpha(0.4f);
                tv_button_continue.setClickable(false);
                spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        item[0] =load_spinner_models.get(position).getFinancial_year_code();
                        if(position > 0) {
                            Log.d("getdata-=>", load_spinner_models.get(position).getFinancial_year_code());
                            year_code = load_spinner_models.get(position).getFinancial_year_code();
                            tv_button_continue.setAlpha(1.0f);
                            tv_button_continue.setClickable(true);
                        }else{
                            tv_button_continue.setAlpha(0.4f);
                            tv_button_continue.setClickable(false);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
                tv_button_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        loadData(year_code);
                    }
                });

                img_view_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_view_salary_slip:
                LayoutInflater li_salary_slip = LayoutInflater.from(ReportHomeListActivity.this);
                final View dialog_salary_slip = li_salary_slip.inflate(R.layout.activity_home_salary_report_popup, null);
                final Spinner spinner_year_salary_slip =  dialog_salary_slip.findViewById(R.id.spinner_year);
                final Spinner spinner_month =  dialog_salary_slip.findViewById(R.id.spinner_month);
                final String[] item_salary_slip = new String[1];
                TextView tv_salary_slip_button_continue = dialog_salary_slip.findViewById(R.id.tv_button_continue);
                ImageView img_salary_slip_view_close = dialog_salary_slip.findViewById(R.id.img_view_close);



                AlertDialog.Builder alert_salary_slip = new AlertDialog.Builder(ReportHomeListActivity.this);
                alert_salary_slip.setView(dialog_salary_slip);
                alert_salary_slip.setCancelable(false);
                //Creating an alert dialog
                final AlertDialog alertDialogSalarySlip = alert_salary_slip.create();
                alertDialogSalarySlip.show();

                //----making spinner_month default false, code starts---
                spinner_month.setClickable(false);
                spinner_month.setAlpha(0.4f);
                //----making spinner_month default false, code ends---

                //----Spinner for year, code starts-----
                Load_Spinner_Data(spinner_year_salary_slip);
                spinner_year_salary_slip.setSelection(0);
                tv_salary_slip_button_continue.setAlpha(0.4f);
                tv_salary_slip_button_continue.setClickable(false);
                spinner_year_salary_slip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        item[0] =load_spinner_models.get(position).getFinancial_year_code();
                        if(position > -1) {
                            Log.d("getdata-=>", load_spinner_models.get(position).getFinancial_year_code());
                            year_code = load_spinner_models.get(position).getFinancial_year_code();

                            spinner_month.setClickable(true);
                            spinner_month.setAlpha(1.0f);
                            /* tv_salary_slip_button_continue.setAlpha(1.0f);
                            tv_salary_slip_button_continue.setClickable(true);*/
                        }else{
                            spinner_month.setClickable(false);
                            spinner_month.setAlpha(0.4f);
                            /*tv_salary_slip_button_continue.setAlpha(0.4f);
                            tv_salary_slip_button_continue.setClickable(false);*/
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
                //----Spinner for year, code ends-----

                //------Spinner for month, code starts-----
                LoadSpinnerDataForMonth(spinner_month);
                spinner_month.setSelection(0);
                tv_salary_slip_button_continue.setAlpha(0.4f);
                tv_salary_slip_button_continue.setClickable(false);
                spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        item[0] =load_spinner_models.get(position).getFinancial_year_code();
                        if(position > 0) {
                            Log.d("MonthSelect-=>",arrayListMonth.get(position).toString());
                            tv_salary_slip_button_continue.setAlpha(1.0f);
                            tv_salary_slip_button_continue.setClickable(true);
                            month_name = arrayListMonth.get(position).toString();
                        }else{
                            tv_salary_slip_button_continue.setAlpha(0.4f);
                            tv_salary_slip_button_continue.setClickable(false);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
                //------Spinner for month, code ends-----
                tv_salary_slip_button_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogSalarySlip.dismiss();
                        loadSalarySlipData(year_code, month_name);
                    }
                });

                img_salary_slip_view_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogSalarySlip.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }
    public File getPdfFile() {
        return this.pdfFile;
    }
    //----code to load spinner data, starts-----
    public void Load_Spinner_Data(Spinner spinner_year) {

        String url= Url.BASEURL() + "finyear/" + "list/reports/" + userSingletonModel.corporate_id+"/1";
        Log.d("urlfinyear-=>",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("fin_years");
                    arrayList.clear();
                    load_spinner_models.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {

                        JSONObject jb1=jsonArray.getJSONObject(i);
                        String financial_year_code=jb1.getString("financial_year_id");
                        String calender_year=jb1.getString("financial_year_code");
                        arrayList.add(calender_year);
                        Load_Spinner_Model spinnerModel=new Load_Spinner_Model();
                        spinnerModel.setFinancial_year_code(financial_year_code);
                        spinnerModel.setCalender_year(calender_year);
                        // arrayList.add(jb1.getString("calender_year"));
                        load_spinner_models.add(spinnerModel);
                    }
                    initSpinnerAdapter(spinner_year);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ReportHomeListActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(ReportHomeListActivity.this).add(stringRequest);
    }
    //----code to load spinner data, ends-----
    private void initSpinnerAdapter(Spinner spinner_year)
    {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(arrayAdapter);
    }

    public void LoadSpinnerDataForMonth(Spinner spinner_month){
        if(!arrayListMonth.isEmpty()){
            arrayListMonth.clear();
        }
        arrayListMonth.add("--Select Month--");
        arrayListMonth.add("January");
        arrayListMonth.add("February");
        arrayListMonth.add("March");
        arrayListMonth.add("April");
        arrayListMonth.add("May");
        arrayListMonth.add("June");
        arrayListMonth.add("July");
        arrayListMonth.add("August");
        arrayListMonth.add("September");
        arrayListMonth.add("October");
        arrayListMonth.add("November");
        arrayListMonth.add("December");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListMonth);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(arrayAdapter);
    }
    //===========Code to get financial year data w.r.t pdf generation from api using volley and load data to recycler view, starts==========
    public void loadData(String financial_year){
//        String url = "http://14.99.211.60:9018/api/reports/pf-deduction/demo_test/95/2020" ;
//        String url = Url.BASEURL() + "reports/pf-deduction/" + userSingletonModel.corporate_id + "/" + userSingletonModel.employee_id + "/" + financial_year; //--commented on 17-Aug-2021
        String url = Url.BASEURL() + "reports/pf-deduction/" + userSingletonModel.corporate_id + "/" + userSingletonModel.employee_id + "/" + financial_year + "/" + userSingletonModel.getBranch_office_id() +"/" + "ALL"; //--added on 17-Aug-2021(added two parameters)
        Log.d("url->",url);
        final ProgressDialog loading = ProgressDialog.show(ReportHomeListActivity.this, "Loading", "Please wait...", true, false);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void getResponseData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                report_html = jsonObject.getString("report_html");
                startActivity(new Intent(ReportHomeListActivity.this,PdfEditorActivity.class));
            }else if(jsonObject1.getString("status").contentEquals("false")){
                Toast.makeText(getApplicationContext(),jsonObject1.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========Code to get financial year data w.r.t pdf generation from api and load data to recycler view, ends==========
//===========Code to get salary slip w.r.t pdf generation from api using volley and load data, code starts==========
    public void loadSalarySlipData(String financial_year, String month_name){
//        String url = "http://14.99.211.60:9018/api/reports/pf-deduction/demo_test/95/2020" ;
//        String url = Url.BASEURL() + "reports/pf-deduction/" + userSingletonModel.corporate_id + "/" + userSingletonModel.employee_id + "/" + financial_year; //--commented on 17-Aug-2021
        String url = Url.BASEURL() + "reports/pay-slip/" + userSingletonModel.corporate_id + "/" + userSingletonModel.employee_id + "/" + month_name + "/" + financial_year + "/" + userSingletonModel.getBranch_office_id() +"/" + "ALL"; //--added on 17-Aug-2021(added two parameters)
        Log.d("url->",url);
        final ProgressDialog loading = ProgressDialog.show(ReportHomeListActivity.this, "Loading", "Please wait...", true, false);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void getResponseSalarySlipData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                report_html = jsonObject.getString("report_html");
                startActivity(new Intent(ReportHomeListActivity.this,PdfEditorActivity.class));
            }else if(jsonObject1.getString("status").contentEquals("false")){
                Toast.makeText(getApplicationContext(),jsonObject1.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//===========Code to get salary slip w.r.t pdf generation from api using volley and load data, code ends==========
}
