package org.wrkplan.payroll.SubOrdinateLeaveApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import org.wrkplan.payroll.HolidayDetail.HolidayDetailActivity;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication.MyLeaveApplicationActivity;
import org.wrkplan.payroll.MyLeaveApplication2.MyLeaveApplication2Activity;
import org.wrkplan.payroll.MyLeaveApplicationModel.LeaveApplication;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SubordinateLeaveModel.Subordinate_Leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubordinateLeaveApplicationActivity extends AppCompatActivity {
    ImageView entry_forms,img_list;
    TextView subordinate_title;
    ListView lv1;
    ArrayList<Subordinate_Leave> arrayList=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Url.isSubordinateLeaveApplication=true;
        Intent intent=new Intent(SubordinateLeaveApplicationActivity.this, LeaveBalanceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subodinate_leave_application);
        //getSupportActionBar().setTitle("Subordinate Leave Application");
        entry_forms=findViewById(R.id.entry_forms);
        img_list=findViewById(R.id.img_list);
        lv1=findViewById(R.id.lv1);
        subordinate_title=findViewById(R.id.subordinate_title);
        subordinate_title.setText("Subordinate Leave Application");


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Url.islistclicked=true;
                Url.isSubordinateLeaveApplication=true;
                Url.isMyLeaveApplication=false;
                Url.LeaveType=arrayList.get(position).getLeave_name();
                Url.currtent_application_id=Url.application_id.get(position);
                Url.supervisor1_id=arrayList.get(position).getSupervisor1_id();
                Url.supervisor2_id=arrayList.get(position).getSupervisor2_id();
                Intent intent=new Intent(SubordinateLeaveApplicationActivity.this,MyLeaveApplication2Activity.class);
                startActivity(intent);
                finish();
            }
        });




        entry_forms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isSubordinateLeaveApplication=true;
                Intent intent=new Intent(SubordinateLeaveApplicationActivity.this, MyLeaveApplication2Activity.class);
                startActivity(intent);
            }
        });
        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(SubordinateLeaveApplicationActivity.this, SubordinateLeaveApplicationActivity.class);
                overridePendingTransition(0, 0);
                startActivity(refresh);
                overridePendingTransition(0, 0);

            }
        });

        Getdata();
    }

    private void Getdata() {
        String url= Url.BASEURL() + "leave/" + "application/" + "list/"+userSingletonModel.corporate_id+"/"+2+"/"+userSingletonModel.user_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("application_list");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);
                        String appliction_id=jb1.getString("appliction_id");
                        String appliction_code=jb1.getString("appliction_code");
                        String leave_name=jb1.getString("leave_name");
                        String employee_name=jb1.getString("employee_name");
                        String from_date=jb1.getString("from_date");
                        String to_date=jb1.getString("to_date");
                        String total_days=jb1.getString("total_days");
                        String leave_status=jb1.getString("leave_status");
                        String supervisor1_id=jb1.getString("supervisor1_id");
                        String supervisor2_id=jb1.getString("supervisor2_id");

                        Subordinate_Leave sbv=new Subordinate_Leave();
                        sbv.setAppliction_code(appliction_code);
                        sbv.setLeave_name(leave_name);
                        sbv.setEmployee_name(employee_name);
                        sbv.setFrom_date(from_date);
                        sbv.setTo_date(to_date);
                        sbv.setTotal_days(total_days);
                        sbv.setLeave_status(leave_status);
                        sbv.setAppliction_id(appliction_id);
                        sbv.setSupervisor1_id(supervisor1_id);
                        sbv.setSupervisor2_id(supervisor2_id);
                        Url.application_id.add(appliction_id);
                        arrayList.add(sbv);
                    }
                    lv1.setAdapter(new Nr());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SubordinateLeaveApplicationActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(SubordinateLeaveApplicationActivity.this).add(stringRequest);

    }

    class Nr extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.subordinate_leave,null);
            TextView txt_emp_name=view.findViewById(R.id.txt_emp_name);
            TextView txt_application_code1=view.findViewById(R.id.txt_application_code1);

            TextView txt_leave_name=view.findViewById(R.id.txt_leave_name);
            TextView txt_leave_date=view.findViewById(R.id.txt_leave_date);
            TextView txt_total_days=view.findViewById(R.id.txt_total_days);
            TextView txt_leave_status=view.findViewById(R.id.txt_leave_status);

            txt_application_code1.setText(arrayList.get(position).getAppliction_code());
            txt_emp_name.setText(arrayList.get(position).getEmployee_name());
            txt_total_days.setText(arrayList.get(position).getTotal_days());
            txt_leave_name.setText(arrayList.get(position).getLeave_name());
            if(arrayList.get(position).getLeave_status().equals("Canceled"))
            {
                txt_leave_status.setText(arrayList.get(position).getLeave_status());
                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                txt_leave_status.setTextColor(getResources().getColor(R.color.cancel));
            }
            else if(arrayList.get(position).getLeave_status().equals("Return"))
            {
                txt_leave_status.setText(arrayList.get(position).getLeave_status());
                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                txt_leave_status.setTextColor(getResources().getColor(R.color.returned));
            }
            else if(arrayList.get(position).getLeave_status().equals("Save"))
            {
                txt_leave_status.setText(arrayList.get(position).getLeave_status());
                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                txt_leave_status.setTextColor(getResources().getColor(R.color.Save));
            }
            else if(arrayList.get(position).getLeave_status().equals("Submit"))
            {
                txt_leave_status.setText(arrayList.get(position).getLeave_status());
                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
                txt_leave_status.setTextColor(getResources().getColor(R.color.Save));
            }
            else

                txt_leave_status.setText(arrayList.get(position).getLeave_status());


            //  txt_leave_date.setText((arrayList.get(position).getFrom_date() + "  To  " +arrayList.get(position).getTo_date()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
            Date myDate = null;
            Date myDate1 = null;
            try {
                myDate = sdf.parse(arrayList.get(position).getFrom_date());
                myDate1 = sdf.parse(arrayList.get(position).getTo_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf.applyPattern("dd-MMM-yyyy");
            //sdf.applyPattern("d MMM YYYY");
            String sMyDate = sdf.format(myDate);
            String sMyDate1 = sdf.format(myDate1);
            //txt_leave_date.setText((arrayList.get(position).getFrom_date() + "  To  " +arrayList.get(position).getTo_date()));
            if(arrayList.get(position).getTotal_days().equals("1"))
            {
                txt_leave_date.setText(sMyDate);
            }
            else {
                txt_leave_date.setText(sMyDate + " To " + sMyDate1);
            }

            return view;
        }
    }
}
