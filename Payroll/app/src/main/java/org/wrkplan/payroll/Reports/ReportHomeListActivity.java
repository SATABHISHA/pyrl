package org.wrkplan.payroll.Reports;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.Load_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReportHomeListActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_view_task;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<Load_Spinner_Model> load_spinner_models = new ArrayList<>();
    ArrayList<String>arrayList=new ArrayList<>();
    public static String report_html = "";
    public static String year_code = "";
    private File pdfFile = null;
    private static LinkedList<Bitmap> pdfBitmapList = new LinkedList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_report_list);
        btn_view_task = findViewById(R.id.btn_view_task);

        btn_view_task.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_task:
                LayoutInflater li = LayoutInflater.from(ReportHomeListActivity.this);
                final View dialog = li.inflate(R.layout.activity_home_pf_report_popup, null);
                final Spinner spinner_year =  dialog.findViewById(R.id.spinner_year);
                final String[] item = new String[1];
                TextView tv_button_continue = dialog.findViewById(R.id.tv_button_continue);



                AlertDialog.Builder alert = new AlertDialog.Builder(ReportHomeListActivity.this);
                alert.setView(dialog);
                alert.setCancelable(false);
                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Load_Spinner_Data(spinner_year);
                spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        item[0] =load_spinner_models.get(position).getFinancial_year_code();
                        if(position != -1) {
                            Log.d("getdata-=>", load_spinner_models.get(position).getFinancial_year_code());
                            year_code = load_spinner_models.get(position).getFinancial_year_code();
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

        String url= Url.BASEURL() + "finyear/" + "list/" + userSingletonModel.corporate_id;
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
                        String financial_year_code=jb1.getString("financial_year_code");
                        String calender_year=jb1.getString("calender_year");
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
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(arrayAdapter);
    }

    //===========Code to get financial year data w.r.t pdf generation from api using volley and load data to recycler view, starts==========
    public void loadData(String financial_year){
//        String url = "http://14.99.211.60:9018/api/reports/pf-deduction/demo_test/95/2020" ;
        String url = Url.BASEURL() + "reports/pf-deduction/" + userSingletonModel.corporate_id + "/" + userSingletonModel.employee_id + "/" + financial_year;
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

                //---pdf converter, code starts
              /*  final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
// Generate Pdf From Html
                PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, report_html, new PDFPrint.OnPDFPrintListener() {
                    @Override
                    public void onSuccess(File file) {
                        // Open Pdf Viewer
                        Uri pdfUri = Uri.fromFile(savedPDFFile);

                        Uri pdfFileUri = pdfUri;

                        if (pdfFileUri == null || pdfFileUri.getPath() == null) {
                            new IllegalStateException("pdf File Uri is null").printStackTrace();
                            finish();
                            return;
                        }

                        pdfFile = new File(pdfFileUri.getPath());

                        if (!pdfFile.exists()) {
                            new IllegalStateException("File Does Not Exist.").printStackTrace();
                            finish();
                            return;
                        }
                        try {
                            pdfBitmapList = PDFUtil.pdfToBitmap(pdfFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //---customise code, ends---

                        File fileToPrint = getPdfFile();
                        if (fileToPrint == null || !fileToPrint.exists()) {
                            Toast.makeText(ReportHomeListActivity.this, "Generated File is null or does not exist!", Toast.LENGTH_SHORT).show();
//                            return super.onOptionsItemSelected(item);
                        }

                        PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
                        printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                        printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

                        PDFUtil.printPdf(ReportHomeListActivity.this, fileToPrint, printAttributeBuilder.build());
                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }
                });*/
                //---pdf converter, code ends
            }else if(jsonObject1.getString("status").contentEquals("false")){
                Toast.makeText(getApplicationContext(),jsonObject1.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========Code to get financial year data w.r.t pdf generation from api and load data to recycler view, ends==========
}
