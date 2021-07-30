package org.wrkplan.payroll.SubOrdinateLeaveApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import org.wrkplan.payroll.Data.SqliteDb;
import org.wrkplan.payroll.HolidayDetail.HolidayDetailActivity;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Model.SubordinateLeaveApplicationModel;
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

public class SubordinateLeaveApplicationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView entry_forms,img_list;
    TextView subordinate_title;
    ListView lv1;
    ArrayList<Subordinate_Leave> arrayList=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    EditText ed_search;
    //Nr nr;
    SqliteDb sqliteDb=new SqliteDb(this);
    ImageView img_back;


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
        img_back=findViewById(R.id.img_back);
        lv1=findViewById(R.id.lv1);
        ed_search=findViewById(R.id.ed_search);
        subordinate_title=findViewById(R.id.subordinate_title);
        subordinate_title.setText("Subordinate Leave Application");

        sqliteDb.deleteAll();

        img_back.setOnClickListener(this);


       /* lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });*/




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

        //Getdata();
        GetDataFromSqliteDB();
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                ArrayList<Subordinate_Leave> arrayList1=new ArrayList<>();
//                if(s.toString().isEmpty())
//                {
//                    arrayList1.clear();
//                    lv1.setAdapter((ListAdapter) arrayList);
//                }
//                for (int i=0;i<arrayList.size();i++)
//                {
//                    if(arrayList.get(i).getEmployee_name().toLowerCase().contains(s))
//                    {
//                        arrayList1.add(arrayList.get(i));
//                    }
//
//                }
//                nr=new Nr(arrayList1);
//                lv1.setAdapter(nr);

                ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList = sqliteDb.Seatchdata(s.toString());
                CustomAdapter customAdapter=new CustomAdapter(subordinateLeaveApplicationModelArrayList);
                lv1.setAdapter(customAdapter);

            }
        });
    }

    private void GetDataFromSqliteDB() {

        String url=Url.BASEURL() + "leave/" + "application/" + "list/"+userSingletonModel.corporate_id+"/"+2+"/"+userSingletonModel.user_id;
        Log.d("url-=>", url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response-=>",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("application_list");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String appliction_code = jsonObject1.getString("appliction_code");
                        Integer appliction_id = jsonObject1.getInt("appliction_id");
                        String approved_by = jsonObject1.getString("approved_by");
                        Integer approved_by_id = jsonObject1.getInt("approved_by_id");
                        String approved_date = jsonObject1.getString("approved_date");
                        Integer approved_level = jsonObject1.getInt("approved_level");
                        String description = jsonObject1.getString("description");
                        Integer employee_id = jsonObject1.getInt("employee_id");
                        String employee_name = jsonObject1.getString("employee_name");
                        String final_approved_by = jsonObject1.getString("final_approved_by");
                        String from_date = jsonObject1.getString("from_date");
                        String leave_name = jsonObject1.getString("leave_name");
                        String leave_status = jsonObject1.getString("leave_status");
                        Integer supervisor1_id = jsonObject1.getInt("supervisor1_id");
                        Integer supervisor2_id = jsonObject1.getInt("supervisor2_id");
                        String supervisor_remark = jsonObject1.getString("supervisor_remark");
                        Integer total_days = jsonObject1.getInt("total_days");
                        String to_date = jsonObject1.getString("to_date");


                        sqliteDb.insertData(appliction_code, appliction_id, approved_by,approved_by_id,approved_date,
                                approved_level,description,employee_id,employee_name,final_approved_by,from_date,leave_name,
                                leave_status,supervisor1_id,supervisor2_id,supervisor_remark,total_days,to_date);

                    }
                    getDatafromSql();



                }
                catch (JSONException e)
                {
                    Toast.makeText(SubordinateLeaveApplicationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SubordinateLeaveApplicationActivity.this, "could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(SubordinateLeaveApplicationActivity.this).add(stringRequest);
    }

    private void getDatafromSql() {
        ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList = sqliteDb.getAllData();

        CustomAdapter myAdapter = new CustomAdapter(subordinateLeaveApplicationModelArrayList);
        lv1.setAdapter(myAdapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubordinateLeaveApplicationModel subordinateLeaveApplicationModel=(SubordinateLeaveApplicationModel) lv1.getItemAtPosition(position);
                int app_id=subordinateLeaveApplicationModel.getAppliction_id();

                Url.islistclicked=true;
                Url.isSubordinateLeaveApplication=true;
                Url.isMyLeaveApplication=false;
                Url.LeaveType=subordinateLeaveApplicationModel.getLeave_name();
                // Url.currtent_application_id=Url.application_id.get(position);
                Url.currtent_application_id=subordinateLeaveApplicationModel.getAppliction_id()+"";
                Url.supervisor1_id=subordinateLeaveApplicationModel.getSupervisor1_id()+"";
                Url.supervisor2_id=subordinateLeaveApplicationModel.getSupervisor2_id()+"";
                Intent intent=new Intent(SubordinateLeaveApplicationActivity.this,MyLeaveApplication2Activity.class);
                intent.putExtra("application_id",app_id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                Intent intent=new Intent(SubordinateLeaveApplicationActivity.this,LeaveBalanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

//    private void Getdata() {
//        if(!arrayList.isEmpty()){
//            arrayList.clear();
//        }
//        String url= Url.BASEURL() + "leave/" + "application/" + "list/"+userSingletonModel.corporate_id+"/"+2+"/"+userSingletonModel.user_id;
//        Log.d("url-=>",url);
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.d("leave->",response);
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    JSONArray jsonArray=jsonObject.getJSONArray("application_list");
//                    for(int i=0;i<jsonArray.length();i++)
//                    {
//                        JSONObject jb1=jsonArray.getJSONObject(i);
//                        String appliction_id=jb1.getString("appliction_id");
//                        String appliction_code=jb1.getString("appliction_code");
//                        String leave_name=jb1.getString("leave_name");
//                        String employee_name=jb1.getString("employee_name");
//                        String from_date=jb1.getString("from_date");
//                        String to_date=jb1.getString("to_date");
//                        String total_days=jb1.getString("total_days");
//                        String leave_status=jb1.getString("leave_status");
//                        String supervisor1_id=jb1.getString("supervisor1_id");
//                        String supervisor2_id=jb1.getString("supervisor2_id");
//
//                        Subordinate_Leave sbv=new Subordinate_Leave();
//                        sbv.setAppliction_code(appliction_code);
//                        sbv.setLeave_name(leave_name);
//                        sbv.setEmployee_name(employee_name);
//                        sbv.setFrom_date(from_date);
//                        sbv.setTo_date(to_date);
//                        sbv.setTotal_days(total_days);
//                        sbv.setLeave_status(leave_status);
//                        sbv.setAppliction_id(appliction_id);
//                        sbv.setSupervisor1_id(supervisor1_id);
//                        sbv.setSupervisor2_id(supervisor2_id);
//                        sbv.setAppliction_id(appliction_id);
//                        Url.application_id.add(appliction_id);
//                        arrayList.add(sbv);
//                    }
//                   // lv1.setAdapter(new Nr());
//                    nr=new Nr(arrayList);
//                    lv1.setAdapter(nr);
//                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                            Url.islistclicked=true;
//                            Url.isSubordinateLeaveApplication=true;
//                            Url.isMyLeaveApplication=false;
//                            Url.LeaveType=arrayList.get(i).getLeave_name();
////                            Url.currtent_application_id=Url.application_id.get(i);
//                            Url.currtent_application_id=arrayList.get(i).getAppliction_id();
//                            Url.supervisor1_id=arrayList.get(i).getSupervisor1_id();
//                            Url.supervisor2_id=arrayList.get(i).getSupervisor2_id();
//
//                            Subordinate_Leave subordinate_leave=(Subordinate_Leave)lv1.getItemAtPosition(i);
//                            String id=subordinate_leave.getAppliction_id();
//                            Intent intent=new Intent(SubordinateLeaveApplicationActivity.this,MyLeaveApplication2Activity.class);
//                            intent.putExtra("application_id",id);
//                            startActivity(intent);
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SubordinateLeaveApplicationActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        Volley.newRequestQueue(SubordinateLeaveApplicationActivity.this).add(stringRequest);
//
//    }
//
//    class Nr extends BaseAdapter{
//        ArrayList<Subordinate_Leave> setgetArrayList=new ArrayList<>();
//        public Nr(ArrayList<Subordinate_Leave> arrayList) {
//            this.setgetArrayList=arrayList;
//        }
//
//
//        @Override
//        public int getCount() {
//            return setgetArrayList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return setgetArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater=getLayoutInflater();
//            View view=inflater.inflate(R.layout.subordinate_leave,null);
//            TextView txt_emp_name=view.findViewById(R.id.txt_emp_name);
//            TextView txt_application_code1=view.findViewById(R.id.txt_application_code1);
//
//            TextView txt_leave_name=view.findViewById(R.id.txt_leave_name);
//            TextView txt_leave_date=view.findViewById(R.id.txt_leave_date);
//            TextView txt_total_days=view.findViewById(R.id.txt_total_days);
//            TextView txt_leave_status=view.findViewById(R.id.txt_leave_status);
//
//            txt_application_code1.setText(setgetArrayList.get(position).getAppliction_code());
//            txt_emp_name.setText(setgetArrayList.get(position).getEmployee_name());
//            txt_total_days.setText(setgetArrayList.get(position).getTotal_days());
//            txt_leave_name.setText(setgetArrayList.get(position).getLeave_name());
//            if(setgetArrayList.get(position).getLeave_status().equals("Canceled"))
//            {
//                txt_leave_status.setText(setgetArrayList.get(position).getLeave_status());
//                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
//                txt_leave_status.setTextColor(getResources().getColor(R.color.cancel));
//            }
//            else if(setgetArrayList.get(position).getLeave_status().equals("Return"))
//            {
//                txt_leave_status.setText(setgetArrayList.get(position).getLeave_status());
//                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
//                txt_leave_status.setTextColor(getResources().getColor(R.color.returned));
//            }
//            else if(setgetArrayList.get(position).getLeave_status().equals("Save"))
//            {
//                txt_leave_status.setText(setgetArrayList.get(position).getLeave_status());
//                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
//                txt_leave_status.setTextColor(getResources().getColor(R.color.Save));
//            }
//            else if(setgetArrayList.get(position).getLeave_status().equals("Submit"))
//            {
//                txt_leave_status.setText(setgetArrayList.get(position).getLeave_status());
//                // ColorDrawable color = new ColorDrawable(Color.parseColor("#E0292E"));
//                txt_leave_status.setTextColor(getResources().getColor(R.color.submit));
//            }
//            else
//
//                txt_leave_status.setText(setgetArrayList.get(position).getLeave_status());
//
//
//            //  txt_leave_date.setText((arrayList.get(position).getFrom_date() + "  To  " +arrayList.get(position).getTo_date()));
////            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.ENGLISH);//--again changed on 21st jan
//            Date myDate = null;
//            Date myDate1 = null;
//            try {
//                myDate = sdf.parse(setgetArrayList.get(position).getFrom_date());
//                myDate1 = sdf.parse(setgetArrayList.get(position).getTo_date());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            sdf.applyPattern("dd-MMM-yyyy");
//            //sdf.applyPattern("d MMM YYYY");
//            String sMyDate = sdf.format(myDate);
//            String sMyDate1 = sdf.format(myDate1);
//            //txt_leave_date.setText((arrayList.get(position).getFrom_date() + "  To  " +arrayList.get(position).getTo_date()));
//            if(setgetArrayList.get(position).getTotal_days().equals("1"))
//            {
////                txt_leave_date.setText(sMyDate);
//                txt_leave_date.setText(setgetArrayList.get(position).getFrom_date()); //added on 21st jan
//            }
//            else {
////                txt_leave_date.setText(sMyDate + " To " + sMyDate1);
//                txt_leave_date.setText(setgetArrayList.get(position).getFrom_date() + " To " + setgetArrayList.get(position).getTo_date()); // added on 21st jan
//            }
//
//            return view;
//        }
//    }

    public class CustomAdapter extends BaseAdapter{
        ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList=new ArrayList<>();
        public CustomAdapter(ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList)
        {

            this.subordinateLeaveApplicationModelArrayList=subordinateLeaveApplicationModelArrayList;
        }
        @Override
        public int getCount() {
            return subordinateLeaveApplicationModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return subordinateLeaveApplicationModelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.subordinate_leave,null);

            TextView txt_application_code1;
            TextView  txt_emp_name;
            TextView   txt_leave_name;
            TextView  txt_leave_date;
            TextView txt_leave_status;
            TextView txt_total_days;

            txt_application_code1=v.findViewById(R.id.txt_application_code1);
            txt_emp_name=v.findViewById(R.id.txt_emp_name);
            txt_leave_status=v.findViewById(R.id.txt_leave_status);
            txt_leave_name=v.findViewById(R.id.txt_leave_name);
            txt_leave_date=v.findViewById(R.id.txt_leave_date);
            txt_total_days=v.findViewById(R.id.txt_total_days);

            txt_application_code1.setText(subordinateLeaveApplicationModelArrayList.get(position).getAppliction_code());
            txt_emp_name.setText(subordinateLeaveApplicationModelArrayList.get(position).getEmployee_name());
//            txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
            txt_leave_name.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_name());
            txt_leave_date.setText(subordinateLeaveApplicationModelArrayList.get(position).getFrom_date()+ " to "+subordinateLeaveApplicationModelArrayList.get(position).getTo_date());
            txt_total_days.setText(""+subordinateLeaveApplicationModelArrayList.get(position).getTotal_days());


            if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Approved")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#1e9547"));
            }else if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Canceled")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#ed1c24"));
            }else if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Returned")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#b04d0b"));
            }else if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Returned")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#fe52ce"));
            }else if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Returned")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#2196ed"));
            }else if(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status().equals("Submit")){
                txt_leave_status.setText(subordinateLeaveApplicationModelArrayList.get(position).getLeave_status());
                txt_leave_status.setTextColor(Color.parseColor("#fe52ce"));
            }
            return  v;
        }
    }
}
