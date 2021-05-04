package org.wrkplan.payroll.Reports;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.Load_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class ReportHomeListActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_view_task;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<Load_Spinner_Model> load_spinner_models = new ArrayList<>();
    ArrayList<String>arrayList=new ArrayList<>();
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
                TextView tv_button_continue = dialog.findViewById(R.id.tv_button_continue);



                AlertDialog.Builder alert = new AlertDialog.Builder(ReportHomeListActivity.this);
                alert.setView(dialog);
                alert.setCancelable(false);
                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Load_Spinner_Data(spinner_year);
                tv_button_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
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
}
