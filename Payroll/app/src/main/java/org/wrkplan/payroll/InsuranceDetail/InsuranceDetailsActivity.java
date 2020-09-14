package org.wrkplan.payroll.InsuranceDetail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.SETGET;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.SwipeGesture.OnSwipeTouchListener;

import java.util.ArrayList;


public class InsuranceDetailsActivity extends AppCompatActivity  implements View.OnClickListener  {

    TextView provider_value,holdername_value,relationship_value,policitype_value,sum_assure_value,policy_expiry_value
            ,txt_head;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    //String url="http://220.225.40.151:9018/api/insurance/payroll_713/42";
    CoordinatorLayout coordinatorLayout;
    Button bt_next,bt_privious,bt_close;
    ArrayList<SETGET> arrayList=new ArrayList<>();
    int i=0;
    LinearLayout ll_12,ll_data;
    TextView txt_msg;

   Boolean item=false;


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(InsuranceDetailsActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_details_prev);

        //------------------Initialize views-------------//

        provider_value=findViewById(R.id.provider_value);
        holdername_value=findViewById(R.id.holdername_value);
        relationship_value=findViewById(R.id.relationship_value);
        policitype_value=findViewById(R.id.policitype_value);
        sum_assure_value=findViewById(R.id.sum_assure_value);
        policy_expiry_value=findViewById(R.id.policy_expiry_value);
        txt_msg=findViewById(R.id.txt_msg);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
      //  ll_12=findViewById(R.id.ll_12);
        ll_data=findViewById(R.id.ll_data);
        bt_next=findViewById(R.id.bt_next);
        txt_head=findViewById(R.id.txt_head);
        bt_privious=findViewById(R.id.bt_privious);
        bt_close=findViewById(R.id.bt_close);
       bt_next.setOnClickListener(this);
       bt_privious.setOnClickListener(this);
       bt_close.setOnClickListener(this);
       bt_privious.setEnabled(false);


        ll_data.setVisibility(View.GONE);




        findViewById(R.id.ll_12).setOnTouchListener(new OnSwipeTouchListener(InsuranceDetailsActivity.this) {

            public void onSwipeRight() {




                    if (item == true) {

                            ll_data.setVisibility(View.VISIBLE);




                        if (i == 0) {
                            bt_privious.setEnabled(false);
                            bt_next.setEnabled(true);
                        } else {
                            i = i - 1;

                            provider_value.setText(arrayList.get(i).getProvider_name());
                            holdername_value.setText(arrayList.get(i).getHolder_name());
                            relationship_value.setText(arrayList.get(i).getHolder_relationship());
                            policitype_value.setText(arrayList.get(i).getType());
                            sum_assure_value.setText(arrayList.get(i).getAmount());
                            policy_expiry_value.setText(arrayList.get(i).getExpiry_date());
                            txt_head.setText("Policy " + (i + 1));
                        }

                        if (i != 0) {
                            bt_privious.setEnabled(true);
                        }
                        if (i == 0) {
                            bt_privious.setEnabled(false);
                            bt_next.setEnabled(true);
                        }
                        if (i != arrayList.size() - 1) {
                            bt_next.setEnabled(true);
                        } else {
                            bt_privious.setEnabled(true);
                        }
                    }

                else
                {
                    bt_next.setEnabled(false);


                                                ll_data.setVisibility(View.GONE);
                                                txt_msg.setText("No insurance detail found for this employee");




                   // ll_data.setVisibility(View.INVISIBLE);


                }

            }
            public void onSwipeLeft() {

                    if (item == true) {
                        ll_data.setVisibility(View.VISIBLE);

                        if (i < arrayList.size() - 1) {
                            i = i + 1;
                            provider_value.setText(arrayList.get(i).getProvider_name());
                            holdername_value.setText(arrayList.get(i).getHolder_name());
                            relationship_value.setText(arrayList.get(i).getHolder_relationship());
                            policitype_value.setText(arrayList.get(i).getType());
                            sum_assure_value.setText(arrayList.get(i).getAmount());
                            policy_expiry_value.setText(arrayList.get(i).getExpiry_date());
                            txt_head.setText("Policy " + (i + 1));


                        }

                        if (i == arrayList.size() - 1) {
                            bt_next.setEnabled(false);
                            bt_privious.setEnabled(true);

                        } else if (i != arrayList.size() - 1) {
                            bt_next.setEnabled(true);
                        }
                        if (i == 0) {
                            bt_privious.setEnabled(false);
                        } else {
                            bt_privious.setEnabled(true);
                        }
                    } else {
                        bt_next.setEnabled(false);
                        ll_data.setVisibility(View.GONE);
                        txt_msg.setText("No insurance detail found for this employee");

                        //ll_data.setVisibility(View.INVISIBLE);
                    }


            }

        });




        getdata();



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_next:

                if(i<arrayList.size()-1)
                {
                    i=i+1;
                    provider_value.setText(arrayList.get(i).getProvider_name());
                    holdername_value.setText(arrayList.get(i).getHolder_name());
                    relationship_value.setText(arrayList.get(i).getHolder_relationship());
                    policitype_value.setText(arrayList.get(i).getType());
                    sum_assure_value.setText(arrayList.get(i).getAmount());
                    policy_expiry_value.setText(arrayList.get(i).getExpiry_date());
                    txt_head.setText("Policy "+(i+1));


                }

                if(i==arrayList.size()-1)
                {
                    bt_next.setEnabled(false);
                    bt_privious.setEnabled(true);

                }
                else if(i!=arrayList.size()-1)

                {
                    bt_next.setEnabled(true);
                }
                if (i==0)
                {
                    bt_privious.setEnabled(false);
                }
                else
                {
                    bt_privious.setEnabled(true);
                }


                if(i==arrayList.size()-1)
                {
                    bt_next.setEnabled(false);

                }

                break;

            case  R.id.bt_privious:
                if(i==0)
                {
                    bt_privious.setEnabled(false);
                    bt_next.setEnabled(true);
                }
                else {
                    i=i-1;

                    provider_value.setText(arrayList.get(i).getProvider_name());
                    holdername_value.setText(arrayList.get(i).getHolder_name());
                    relationship_value.setText(arrayList.get(i).getHolder_relationship());
                    policitype_value.setText(arrayList.get(i).getType());
                    sum_assure_value.setText(arrayList.get(i).getAmount());
                    policy_expiry_value.setText(arrayList.get(i).getExpiry_date());
                    txt_head.setText("Policy "+(i+1));
                }

                if (i!=0)
                {
                    bt_privious.setEnabled(true);
                }
                if (i==0)
                {
                    bt_privious.setEnabled(false);
                    bt_next.setEnabled(true);
                }
                if(i!=arrayList.size()-1)

                {
                    bt_next.setEnabled(true);
                }
                else
                {
                    bt_privious.setEnabled(true);
                }


                break;

            case R.id.bt_close:
                Intent intent=new Intent(InsuranceDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

                break;





        }
    }
    private void getdata () {

        String url= Url.BASEURL() + "insurance/"+userSingletonModel.corporate_id+"/"+userSingletonModel.user_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data-=>", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray js_policy = jsonObject.getJSONArray("policies");
                    for (int i = 0; i < js_policy.length(); i++) {
                        JSONObject js_poly = js_policy.getJSONObject(i);
                        String provider_name=js_poly.getString("provider_name");
                        String holder_name=js_poly.getString("holder_name");
                        String holder_relationship=js_poly.getString("holder_relationship");
                        String type=js_poly.getString("type");
                        String amount=js_poly.getString("amount");
                        String expiry_date=js_poly.getString("expiry_date");
                        SETGET setget=new SETGET();
                        setget.setProvider_name(provider_name);
                        setget.setHolder_name(holder_name);
                        setget.setHolder_relationship(holder_relationship);
                        setget.setType(type);
                        setget.setAmount(amount);
                        setget.setExpiry_date(expiry_date);
                        arrayList.add(setget);

                    }
                    JSONObject jsonObject1=jsonObject.getJSONObject("response");
                    String status=jsonObject1.getString("status");
                    if(status.equals("true"))
                    {
                        item=true;
                        ll_data.setVisibility(View.VISIBLE);
                    }
//                    JSONObject jsonObject2=jsonObject.getJSONObject("response");
//                    String status1=jsonObject2.getString("status");
//                    if(status1.equals("false"))
//                    {
//                        isitem=false;
//                  e
//                    }

                        else {
                        //ll_data.setVisibility(View.GONE);
                        txt_msg.setText("No insurance detail found for this employee");

                    }



//                        provider_value.setText(userSingletonModel.getProvider_name());
//                        holdername_value.setText(userSingletonModel.getHolder_name());
//                        Log.d("dsds", userSingletonModel.getHolder_name());
//                        relationship_value.setText(userSingletonModel.getHolder_relationship());
//                        policitype_value.setText(userSingletonModel.getType());
//                        sum_assure_value.setText(userSingletonModel.getAmount());
//                        policy_expiry_value.setText(userSingletonModel.getExpiry_date());

                    provider_value.setText(arrayList.get(i).getProvider_name());
                    holdername_value.setText(arrayList.get(i).getHolder_name());
                    relationship_value.setText(arrayList.get(i).getHolder_relationship());
                    policitype_value.setText(arrayList.get(i).getType());
                    sum_assure_value.setText(arrayList.get(i).getAmount());
                    policy_expiry_value.setText(arrayList.get(i).getExpiry_date());





                } catch (JSONException e) {
                    e.printStackTrace();
                    bt_next.setEnabled(false);
                    txt_msg.setText("No insurance detail found for this employee");



                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Could not connect to the server", Snackbar.LENGTH_INDEFINITE);
//                snackbar.show();

            }
        });
        Volley.newRequestQueue(InsuranceDetailsActivity.this).add(stringRequest);
    }


}
