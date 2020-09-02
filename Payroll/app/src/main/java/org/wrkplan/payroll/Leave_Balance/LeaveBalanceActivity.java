package org.wrkplan.payroll.Leave_Balance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication.MyLeaveApplicationActivity;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SubOrdinateLeaveApplication.SubordinateLeaveApplicationActivity;

import java.util.ArrayList;

public class LeaveBalanceActivity extends AppCompatActivity {

    TextView tv_casual_leave,tv_earn_leav,tv_sick_leave,tv_matarnal_leave,tv_paternal_leave,tv_comp_off;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    //  String url="http://192.168.10.175:9018/api/leaves/payroll_713/50/2019-2020";
    ArrayList<String> arrayList = new ArrayList<>();
    Button bt_ok,btn_leave,btn_subordinate;
    String year;

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
        tv_casual_leave=findViewById(R.id.tv_casual_leave);
        tv_earn_leav=findViewById(R.id.tv_earn_leav);
        tv_sick_leave=findViewById(R.id.tv_sick_leave);
        tv_matarnal_leave=findViewById(R.id.tv_matarnal_leave);
        tv_paternal_leave=findViewById(R.id.tv_paternal_leave);
        tv_comp_off=findViewById(R.id.tv_comp_off);
        //  bt_ok=findViewById(R.id.bt_ok);
        btn_leave=findViewById(R.id.btn_leave);
        btn_subordinate=findViewById(R.id.btn_subordinate);
        final Spinner spinner1 = findViewById(R.id.spinner1);
        // bt_ok.setVisibility(View.GONE);



        //------------------End Initialize Views-------------//

        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isMyLeaveApplication=true;
                Intent intent=new Intent(LeaveBalanceActivity.this, MyLeaveApplicationActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        btn_subordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isSubordinateLeaveApplication=true;
                Intent intent=new Intent(LeaveBalanceActivity.this, SubordinateLeaveApplicationActivity.class);
                startActivity(intent);

            }
        });


        btn_leave.setTransformationMethod(null);
        btn_subordinate.setTransformationMethod(null);

        arrayList.add("2020");
        arrayList.add("2019");
        arrayList.add("2018");




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //  Toast.makeText(parent.getContext(), "Selected: " + item,          Toast.LENGTH_LONG).show();

                if(item=="2020")
                {
                    year="2020-2021";
                }
                else if(item=="2019")
                {
                    year="2019-2020";
                }
                else
                {
                    year="2018-2019";
                }

                GetData(year);

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

    private void GetData(String year ) {

        String url= Url.BASEURL + "leave/" + "balance/" + userSingletonModel.corporate_id+"/"+userSingletonModel.employee_id+"/"+year;
        Log.d("sfgj==",url);



        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jb1=jsonObject.getJSONObject("leaves");
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


}

