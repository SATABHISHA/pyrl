package org.wrkplan.payroll.Leave_Balance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.Load_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication.MyLeaveApplicationActivity;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SubOrdinateLeaveApplication.SubordinateLeaveApplicationActivity;

import java.util.ArrayList;

public class LeaveBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_casual_leave,tv_earn_leav,tv_sick_leave,tv_matarnal_leave,tv_paternal_leave,tv_comp_off;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    //  String url="http://192.168.10.175:9018/api/leaves/payroll_713/50/2019-2020";
    ArrayList<Load_Spinner_Model> load_spinner_models = new ArrayList<>();
    ArrayList<String>arrayList=new ArrayList<>();
    Button bt_ok,btn_leave,btn_subordinate;
    String year;
    Spinner spinner1;
    ImageView img_back;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(LeaveBalanceActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);

        //------------------Initialize Views-------------------//
        img_back=findViewById(R.id.img_back);
        tv_casual_leave=findViewById(R.id.tv_casual_leave);
        tv_earn_leav=findViewById(R.id.tv_earn_leav);
        tv_sick_leave=findViewById(R.id.tv_sick_leave);
        tv_matarnal_leave=findViewById(R.id.tv_matarnal_leave);
        tv_paternal_leave=findViewById(R.id.tv_paternal_leave);
        tv_comp_off=findViewById(R.id.tv_comp_off);
        //  bt_ok=findViewById(R.id.bt_ok);
        btn_leave=findViewById(R.id.btn_leave);
        btn_subordinate=findViewById(R.id.btn_subordinate);
        spinner1 = findViewById(R.id.spinner1);
        // bt_ok.setVisibility(View.GONE);



        //------------------End Initialize Views-------------//
        img_back.setOnClickListener(this);

        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isMyLeaveApplication=true;
                Url.isSubordinateLeaveApplication=false;
                Intent intent=new Intent(LeaveBalanceActivity.this, MyLeaveApplicationActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        btn_subordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isSubordinateLeaveApplication=true;
                Url.isMyLeaveApplication=false;
                Intent intent=new Intent(LeaveBalanceActivity.this, SubordinateLeaveApplicationActivity.class);
                startActivity(intent);

            }
        });


        btn_leave.setTransformationMethod(null);
        btn_subordinate.setTransformationMethod(null); //-------commented by satabhisha on 3rd nov as per discussion for temporary case



        //  arrayList.add(String.valueOf(load_spinner_models));

//   for (int i=0;i<load_spinner_models.size();i++)
//   {
//       arrayList.add(load_spinner_models.get(i).getCalender_year());
//   }
        Load_Spinner_Data();
//        arrayList.add("2020");
//        arrayList.add("2019");
//        arrayList.add("2018");

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
                String item=load_spinner_models.get(position).getFinancial_year_code();
                //   Toast.makeText(parent.getContext(), "Selected: " + item,          Toast.LENGTH_LONG).show();
                Log.d("ghgf", String.valueOf(position));
//                String data=parent.getItemAtPosition(position).toString();
//                if(data=="2020")
//                {
//                     year="2020-2021";
//
//                }
//                else if(data=="2019")
//                {
//                     year="2019-2020";
//
//                }
//                else
//                {
//                     year="2018-2019";
//
//                }
//
                GetData(item);

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

//        bt_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(LeaveBalanceActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }}
//        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void Load_Spinner_Data() {

        String url=Url.BASEURL() + "finyear/" + "list/" + userSingletonModel.corporate_id;
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
                    initSpinnerAdapter();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LeaveBalanceActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(LeaveBalanceActivity.this).add(stringRequest);
    }
    private void initSpinnerAdapter()
    {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
    }

    private void GetData(String year ) {

        String url= Url.BASEURL() + "leave/" + "balance/" + userSingletonModel.corporate_id+"/"+userSingletonModel.employee_id+"/"+year;
        Log.d("sfgj==",url);



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

                    Log.d("case",tv_casual_leave.toString());

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(LeaveBalanceActivity.this).add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}

