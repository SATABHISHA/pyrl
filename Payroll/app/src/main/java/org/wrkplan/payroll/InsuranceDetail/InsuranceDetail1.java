package org.wrkplan.payroll.InsuranceDetail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
import org.wrkplan.payroll.Model.InsuramceModel;
import org.wrkplan.payroll.Model.SETGET;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SwipeGesture.OnSwipeTouchListener;

import java.util.ArrayList;

public class InsuranceDetail1 extends AppCompatActivity {

    TextView txt_policy_type,txt_provider_name,txt_policy_no,txt_amount,txt_expiry_date,txt_msg;
    Button bt_privious,bt_close,bt_next;
    ListView lv1;
    ArrayList<InsuramceModel> arrayList=new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    LinearLayout aaa;
    Boolean item=false;
    int i=0;
    int count=0;
    int arraySize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_details);


        //===============Initialize Views Start=====================//

             txt_policy_type=findViewById(R.id.txt_policy_type);
             txt_provider_name=findViewById(R.id.txt_provider_name);
             txt_policy_no=findViewById(R.id.txt_policy_no);
             txt_amount=findViewById(R.id.txt_amount);
             txt_expiry_date=findViewById(R.id.txt_expiry_date);
             bt_privious=findViewById(R.id.bt_privious);
             bt_close=findViewById(R.id.bt_close);
             bt_next=findViewById(R.id.bt_next);
             lv1=findViewById(R.id.lv1);
             coordinatorLayout=findViewById(R.id.coordinatorLayout);
             aaa=findViewById(R.id.aaa);
             txt_msg=findViewById(R.id.txt_msg);

        //===============Initialize Views Ends=====================//
        bt_privious.setEnabled(false);
        aaa.setVisibility(View.GONE);

        getdata(0);
        Getsize();


        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item==true) {
                    aaa.setVisibility(View.VISIBLE);


                    if (count < arraySize - 1) {
                        count = count + 1;
                        getdata(count);
                    }
                    if (count == arraySize - 1) {
                        bt_next.setEnabled(false);
                        bt_privious.setEnabled(true);
                    }
                }
                else
                {
                    bt_next.setEnabled(false);
                    aaa.setVisibility(View.GONE);
                    txt_msg.setText("No insurance detail found for this employee");

                }


            }

        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InsuranceDetail1.this,HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });
        bt_privious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (count > 0) {
                        count = count - 1;
                        getdata(count);
                    }
                    if (count != 0) {
                        bt_privious.setEnabled(true);
                        bt_next.setEnabled(true);
                    }
                    if (count == 0) {
                        bt_privious.setEnabled(false);
                        bt_next.setEnabled(true);
                    }

                }



        });
        findViewById(R.id.ll_12).setOnTouchListener(new OnSwipeTouchListener(InsuranceDetail1.this) {

            public void onSwipeRight() {
                if(item==true) {
                    aaa.setVisibility(View.VISIBLE);

                    if (count > 0) {
                        count = count - 1;
                        getdata(count);
                    }
                    if (count != 0) {
                        bt_privious.setEnabled(true);
                        bt_next.setEnabled(true);
                    }
                    if (count == 0) {
                        bt_privious.setEnabled(false);
                        bt_next.setEnabled(true);
                    }
                }
                else
                {
                    bt_next.setEnabled(false);
                    aaa.setVisibility(View.GONE);
                    txt_msg.setText("No insurance detail found for this employee");
                }



            }





            public void onSwipeLeft() {

                        if(item==true) {
                            aaa.setVisibility(View.VISIBLE);


                            if (count < arraySize - 1) {
                                count = count + 1;
                                getdata(count);
                            }
                            if (count == arraySize - 1) {
                                bt_next.setEnabled(false);
                                bt_privious.setEnabled(true);
                            }

                        }
                        else
                        {
                            bt_next.setEnabled(false);
                            aaa.setVisibility(View.GONE);
                            txt_msg.setText("No insurance detail found for this employee");
                        }


            }
        });


    }
    private void Getsize() {
        String url= Url.BASEURL + "insurance/"+userSingletonModel.corporate_id+"/"+userSingletonModel.user_id;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);


                        JSONArray jsonArray = jsonObject.getJSONArray("policies");
                        arraySize = jsonArray.length();
                    JSONObject jsonObject1=jsonObject.getJSONObject("response");
                    String status=jsonObject1.getString("status");
                   // Toast.makeText(InsuranceDetail1.this, status, Toast.LENGTH_SHORT).show();
                    if(status.equals("true"))
                    {
                        item=true;
                        aaa.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        aaa.setVisibility(View.GONE);
                        txt_msg.setText("No insurance detail found for this employee");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(InsuranceDetail1.this).add(stringRequest);
    }




    private void getdata(final int count) {
        String url= Url.BASEURL + "insurance/"+userSingletonModel.corporate_id+"/"+userSingletonModel.user_id;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                 JSONObject jsonObject=new JSONObject(response);
//
                        JSONArray jsonArray = jsonObject.getJSONArray("policies");
                        if (!arrayList.isEmpty()) {
                            arrayList.clear();
                        }
                        //                    for ( int i=count;i<jsonArray.length();i++) {
                        JSONObject jb1 = jsonArray.getJSONObject(count);
                        String provider_name = jb1.getString("provider_name");
                        String type = jb1.getString("type");
                        String no = jb1.getString("no");
                        String amount = jb1.getString("amount");
                        String expiry_date = jb1.getString("expiry_date");
                        JSONArray jb2 = jb1.getJSONArray("members");
                        for (int j = 0; j < jb2.length(); j++) {
                            JSONObject jb3 = jb2.getJSONObject(j);
                            String name = jb3.getString("name");
                            String relationship = jb3.getString("relationship");
                            InsuramceModel insuramceModel = new InsuramceModel();
                            insuramceModel.setProvider_name(provider_name);
                            insuramceModel.setType(type);
                            insuramceModel.setNo(no);
                            insuramceModel.setAmount(amount);
                            insuramceModel.setExpiry_date(expiry_date);
                            insuramceModel.setName(name);
                            insuramceModel.setRelationship(relationship);
                            arrayList.add(insuramceModel);

                        }

                        lv1.setAdapter(new NR());


                        Log.d("array>>", String.valueOf(arrayList.size()));
                        Log.d("ssff==>", String.valueOf(count));

                        //  lv1.setAdapter(new NR());

                        Log.d("ssff==>", String.valueOf(count));
                        txt_policy_type.setText(arrayList.get(i).getType());
                        txt_provider_name.setText(arrayList.get(i).getProvider_name());
                        txt_policy_no.setText(arrayList.get(i).getNo());
                        txt_amount.setText(arrayList.get(i).getAmount());
                        txt_expiry_date.setText(arrayList.get(i).getExpiry_date());



                        JSONObject jsonObject1=jsonObject.getJSONObject("response");
                        String status=jsonObject1.getString("status");
                  // Toast.makeText(InsuranceDetail1.this, status, Toast.LENGTH_SHORT).show();
                    if(status.equals("true"))
                    {
                        item=true;
                        aaa.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        aaa.setVisibility(View.GONE);
                        txt_msg.setText("No insurance detail found for this employee");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                   bt_next.setEnabled(false);
                    txt_msg.setText("No insurance detail found for this employee");
                                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(InsuranceDetail1.this).add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(InsuranceDetail1.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    class NR  extends BaseAdapter{
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
            View view=inflater.inflate(R.layout.insurance_member,null);
            TextView txt_member_name=view.findViewById(R.id.txt_member_name);
            TextView txt_relationship=view.findViewById(R.id.txt_relationship);
            txt_member_name.setText(arrayList.get(position).getName());
            txt_relationship.setText(arrayList.get(position).getRelationship());
            return view;
        }
    }





}
