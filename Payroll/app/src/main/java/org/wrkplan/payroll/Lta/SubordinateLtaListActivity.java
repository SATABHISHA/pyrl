package org.wrkplan.payroll.Lta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.OutDoorListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.CustomSubordinateOutdoorListAdapter;
import org.wrkplan.payroll.OutDoorDuty.SubordinateOutdoorListActivity;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class SubordinateLtaListActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<LTAModel> ltaModelArrayList = new ArrayList<>();
    ArrayList<LTAModel> filteredData = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata;
    RecyclerView recycler_view;
    EditText ed_search;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_lta_list);

        img_back=findViewById(R.id.img_back);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        ed_search = findViewById(R.id.ed_search);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//             loadData(ed_search.getText().toString());
                display_filtered_data(ed_search.getText().toString());
            }
        });
        loadData();
        display_filtered_data("");

        img_back.setOnClickListener(this);

    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
        String fin_year_id;
        if (userSingletonModel.getFin_year_id().contains("null")) {
            fin_year_id = "0";
        }else{
            fin_year_id = userSingletonModel.getFin_year_id();
        }
        String url = Url.BASEURL()+"lta/list/"+userSingletonModel.getCorporate_id()+"/Subordinate/"+userSingletonModel.getEmployee_id()+"/"+userSingletonModel.getBranch_office_id()+"/0/"+fin_year_id;
        Log.d("urltesting-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/20";
        final ProgressDialog loading = ProgressDialog.show(SubordinateLtaListActivity.this, "Loading", "Please wait...", true, false);
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
            if(!ltaModelArrayList.isEmpty()){
                ltaModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("lta_list");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    LTAModel ltaModel = new LTAModel();
                    ltaModel.setLta_application_id(jsonObject2.getString("lta_application_id"));
                    ltaModel.setLta_application_no(jsonObject2.getString("lta_application_no"));
                    ltaModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    ltaModel.setDate_from(jsonObject2.getString("date_from"));
                    ltaModel.setEmployee_name(jsonObject2.getString("employee_name"));
                    ltaModel.setDate_to(jsonObject2.getString("date_to"));
                    ltaModel.setLta_amount(String.valueOf(jsonObject2.getDouble("lta_amount")));
                    ltaModel.setLta_application_status(jsonObject2.getString("lta_application_status"));

                    ltaModelArrayList.add(ltaModel);

                }
                display_filtered_data("");
//                recycler_view.setAdapter(new CustomSubordinateLTAListAdapter(SubordinateLtaListActivity.this, ltaModelArrayList));
            }else if(jsonObject1.getString("status").contentEquals("false")){
                ll_recycler.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText(jsonObject1.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code to get data from api and load data to recycler view, ends==========

    //===========code to filter data and display in list, code starts=======
    public void display_filtered_data(String employeename){
        if (!filteredData.isEmpty()){
            filteredData.clear();
        }
        if(ltaModelArrayList.size()>0) {
            for (int i = 0; i < ltaModelArrayList.size(); i++){
                if(ltaModelArrayList.get(i).getEmployee_name().toLowerCase().trim().contains(employeename.toLowerCase())){
                    LTAModel ltaModel = new LTAModel();
                    ltaModel.setLta_application_id(ltaModelArrayList.get(i).getLta_application_id());
                    ltaModel.setLta_application_no(ltaModelArrayList.get(i).getLta_application_no());
                    ltaModel.setEmployee_id(ltaModelArrayList.get(i).getEmployee_id());
                    ltaModel.setDate_from(ltaModelArrayList.get(i).getDate_from());
                    ltaModel.setEmployee_name(ltaModelArrayList.get(i).getEmployee_name());
                    ltaModel.setDate_to(ltaModelArrayList.get(i).getDate_to());
                    ltaModel.setLta_amount(String.valueOf(ltaModelArrayList.get(i).getLta_amount()));
                    ltaModel.setLta_application_status(ltaModelArrayList.get(i).getLta_application_status());
                    filteredData.add(ltaModel);

                }
            }
            recycler_view.setAdapter(new CustomSubordinateLTAListAdapter(SubordinateLtaListActivity.this, filteredData));
        }/*else{
            ll_recycler.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
            tv_nodata.setText(jsonObject1.getString("message"));
        }*/
    }
    //===========code to filter data and display in list, code ends=======


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(SubordinateLtaListActivity.this, LtaListActivity.class));
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
