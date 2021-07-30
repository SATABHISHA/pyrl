package org.wrkplan.payroll.OutDoorDutyLog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.OutDoorDetailModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OdDutyLogDetailActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<OutDoorDetailModel> outDoorDetailModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata, tv_empname, tv_log_date_time;
    RecyclerView recycler_view;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.od_duty_log_detail_activity);

        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_empname = findViewById(R.id.tv_empname);
        tv_log_date_time = findViewById(R.id.tv_log_date_time);
        img_back = findViewById(R.id.img_back);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
        loadData();

        img_back.setOnClickListener(this);
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
        String url = Url.BASEURL()+"od/log/detail/"+userSingletonModel.getCorporate_id()+"/"+OdDutyLogListActivity.od_request_id+"/"+OdDutyLogListActivity.od_log_date;
        Log.d("detailurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(OdDutyLogDetailActivity.this, "Loading", "Please wait...", true, false);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void getResponseData(String response){
        try {
            if(!outDoorDetailModelArrayList.isEmpty()){
                outDoorDetailModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            tv_empname.setText(jsonObject.getString("employee_name"));


            //-------Date format code starts----
//            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); // again changed on 21st jan
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

            String inputText = jsonObject.getString("od_duty_log_date");

            Date date_log = null;
            try {
                date_log = inputFormat.parse(inputText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_log_date_time.setText("Time Log of "+outputFormat.format(date_log));

            //-------Date format code ends----

            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    OutDoorDetailModel outDoorDetailModel = new OutDoorDetailModel();
                    outDoorDetailModel.setOd_duty_log_id(jsonObject2.getString("od_duty_log_id"));
                    outDoorDetailModel.setLog_action(jsonObject2.getString("log_action"));
                    outDoorDetailModel.setLog_datetime(jsonObject2.getString("log_datetime"));
                    outDoorDetailModel.setLog_time(jsonObject2.getString("log_time"));
                    outDoorDetailModel.setLatitude(jsonObject2.getString("latitude"));
                    outDoorDetailModel.setLongitude(jsonObject2.getString("longitude"));
                    outDoorDetailModel.setLocation_address(jsonObject2.getString("location_address"));

                    outDoorDetailModelArrayList.add(outDoorDetailModel);

                }
                recycler_view.setAdapter(new CustomOdDutyLogDetailAdapter(OdDutyLogDetailActivity.this, outDoorDetailModelArrayList));
            }else if(jsonObject1.getString("status").contentEquals("false")){
                ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
    //===========Code to get data from api and load data to recycler view, ends==========
}
