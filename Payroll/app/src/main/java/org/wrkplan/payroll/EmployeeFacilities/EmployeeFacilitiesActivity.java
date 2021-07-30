package org.wrkplan.payroll.EmployeeFacilities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.EmployeeFacilitisModel.Facilitis;
import org.wrkplan.payroll.HolidayModel.Holiday;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class EmployeeFacilitiesActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv1;
    Button btn_ok;
    ArrayList<Facilitis> arrayList=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    ImageView img_back;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EmployeeFacilitiesActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_facilities);
        lv1=findViewById(R.id.lv1);
        img_back=findViewById(R.id.img_back);
        btn_ok=findViewById(R.id.btn_ok);
        btn_ok.setVisibility(View.GONE);

        Getdata();

        img_back.setOnClickListener(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeFacilitiesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void Getdata() {
        String url= Url.BASEURL() + "facilities/"+userSingletonModel.corporate_id+"/"+userSingletonModel.employee_id;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("facilities");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);
                        String service_type=jb1.getString("service_type");
                        String value=jb1.getString("value");
                        Facilitis facilitis=new Facilitis();
                        facilitis.setService_type(service_type);
                        facilitis.setValue(value);
                        arrayList.add(facilitis);

                    }
                    lv1.setAdapter(new Nr());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployeeFacilitiesActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }

        });
        Volley.newRequestQueue(EmployeeFacilitiesActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent_home = new Intent(this, HomeActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_home);
                break;
        }
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
            View view=inflater.inflate(R.layout.empfasilities,null);
            TextView txt_service_type= view.findViewById(R.id.txt_service_type);
            TextView txt_values=view.findViewById(R.id.txt_values);
            TextView txt_inr=view.findViewById(R.id.txt_inr);

            txt_service_type.setText(arrayList.get(position).getService_type());
            txt_values.setText(arrayList.get(position).getValue()+"0");
            return  view;

        }
    }
}
