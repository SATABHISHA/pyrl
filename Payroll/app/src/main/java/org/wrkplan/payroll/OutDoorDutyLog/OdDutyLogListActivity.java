package org.wrkplan.payroll.OutDoorDutyLog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OdDutyLogListActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<OutDoorLogListModel> outDoorLogListModelArrayList = new ArrayList<>();
    LinearLayout ll_recycler;
    TextView tv_nodata, tv_button_subordinate, tv_date, tv_start, tv_pause, tv_stop;
    RecyclerView recycler_view;
    RelativeLayout rl_task_status, rl_start, rl_pause, rl_stop;
    public  static Integer od_request_id_current_activity, log_task_status; //-----added by Satabhisha(log_task_status is used to identify supervisor and subordinate for task detail section)
    public String latitude = "", longitude = "", locationAddress=""; //---added by Satabhisha
    public static String od_duty_status = "";
    LocationManager locationManager;
    ImageView img_back;

    public static String od_request_id = "", od_log_date = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.od_duty_log_list_activity);

        img_back=findViewById(R.id.img_back);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_button_subordinate = findViewById(R.id.tv_button_subordinate);
        rl_task_status = findViewById(R.id.rl_task_status);
        tv_date = findViewById(R.id.tv_date);
        tv_start = findViewById(R.id.tv_start);
        tv_pause = findViewById(R.id.tv_pause);
        tv_stop = findViewById(R.id.tv_stop);

        rl_start = findViewById(R.id.rl_start);
        rl_pause = findViewById(R.id.rl_pause);
        rl_stop = findViewById(R.id.rl_stop);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======

        load_data_check_od_duty();
        getLocation();
        loadData();

        tv_button_subordinate.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_pause.setOnClickListener(this);
        tv_stop.setOnClickListener(this);
        img_back.setOnClickListener(this);

        statusCheck(); //--added on 1st sept

    }

    @Override
    protected void onResume() {
        super.onResume();
        statusCheck();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        statusCheck();
        recreate();
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.img_back:
               Intent intent_home = new Intent(this, HomeActivity.class);
               intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent_home);
               break;
           case R.id.tv_button_subordinate:
               startActivity(new Intent(this,SubordinateOdDutyLogListActivity.class));
               break;
           case R.id.tv_start:
               if(!latitude.contentEquals("") && !longitude.contentEquals("")) {
                   saveData("START", "Outdoor Duty Started");
               }else{
                   Toast.makeText(getApplicationContext(),"Unable to track location. Please try again.",Toast.LENGTH_LONG).show();
               }
               break;
           case R.id.tv_pause:
               if(!latitude.contentEquals("") && !longitude.contentEquals("")) {
                   saveData("PAUSE", "Outdoor Duty Paused");
               }else {
                   Toast.makeText(getApplicationContext(),"Unable to track location. Please try again.",Toast.LENGTH_LONG).show();
               }
               break;
           case R.id.tv_stop:
               androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(OdDutyLogListActivity.this);
               builder.setMessage("Do you really want to stop today's outdoor duty? Once stopped cannot be started again. Proceed?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               if(!latitude.contentEquals("") && !longitude.contentEquals("")) {
                                   saveData("STOP", "Outdoor Duty Stopped");
                               }else{
                                   Toast.makeText(getApplicationContext(),"Unable to track location. Please try again.",Toast.LENGTH_LONG).show();
                               }
                               dialog.cancel();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                           }
                       });
               androidx.appcompat.app.AlertDialog alert = builder.create();
               alert.show();
               break;
           default:
               break;
       }
    }

    //===========Code to get data from api using volley and load data to recycler view, starts==========
    public void loadData(){
        String url = Url.BASEURL()+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        Log.d("listurl-=>",url);
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/52";
        final ProgressDialog loading = ProgressDialog.show(OdDutyLogListActivity.this, "Loading", "Please wait...", true, false);
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
            if(!outDoorLogListModelArrayList.isEmpty()){
                outDoorLogListModelArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){
                ll_recycler.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    OutDoorLogListModel outDoorLogListModel = new OutDoorLogListModel();
                    outDoorLogListModel.setOd_duty_log_date(jsonObject2.getString("od_duty_log_date"));
                    outDoorLogListModel.setOd_request_id(jsonObject2.getString("od_request_id"));
                    outDoorLogListModel.setEmployee_id(jsonObject2.getString("employee_id"));
                    outDoorLogListModel.setEmployee_name(jsonObject2.getString("employee_name"));

                    outDoorLogListModelArrayList.add(outDoorLogListModel);

                }
                recycler_view.setAdapter(new CustomOdDutyLogListAdapter(OdDutyLogListActivity.this, outDoorLogListModelArrayList));
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


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent_home = new Intent(this, HomeActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_home);
    }

    //==========added on 23rd June
    //===========Code to get data to check od_duty_status from api using volley and load data(added by Satabhisha), starts==========
    public void load_data_check_od_duty(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"od/request/check-exist/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(OdDutyLogListActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get_od_duty_data(response);
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
    public void get_od_duty_data(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            od_request_id_current_activity = jsonObject.getInt("od_request_id");
            if(jsonObject.getString("status").contentEquals("true")){
                tv_date.setVisibility(View.VISIBLE);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                tv_date.setText(formattedDate);

                if(jsonObject.getString("next_action").contentEquals("START")){
                    rl_task_status.setVisibility(View.VISIBLE);

                    tv_start.setVisibility(View.VISIBLE);
                    rl_start.setVisibility(View.VISIBLE);

                    tv_pause.setVisibility(View.GONE);
                    rl_pause.setVisibility(View.GONE);

                    tv_stop.setVisibility(View.GONE);
                    rl_stop.setVisibility(View.GONE);
                }else if(jsonObject.getString("next_action").contentEquals("PAUSE")){
                    rl_task_status.setVisibility(View.VISIBLE);

                    tv_start.setVisibility(View.GONE);
                    rl_start.setVisibility(View.GONE);

                    tv_pause.setVisibility(View.VISIBLE);
                    rl_pause.setVisibility(View.VISIBLE);

                    tv_stop.setVisibility(View.VISIBLE);
                    rl_stop.setVisibility(View.VISIBLE);
                }else if(jsonObject.getString("next_action").contentEquals("STOP")){
                    od_duty_status = "STOP"; //--added on 18th June
                    rl_task_status.setVisibility(View.VISIBLE);

                    tv_start.setVisibility(View.GONE);
                    rl_start.setVisibility(View.GONE);

                    tv_pause.setVisibility(View.GONE);
                    rl_pause.setVisibility(View.GONE);

                    tv_stop.setVisibility(View.VISIBLE);
                    rl_stop.setVisibility(View.VISIBLE);
                }else if(jsonObject.getString("next_action").contentEquals("NA")){
                    od_duty_status = "NA"; //--added on 18th June
                    rl_task_status.setVisibility(View.GONE);

                    tv_start.setVisibility(View.GONE);
                    rl_start.setVisibility(View.GONE);

                    tv_pause.setVisibility(View.GONE);
                    rl_pause.setVisibility(View.GONE);

                    tv_stop.setVisibility(View.GONE);
                    rl_stop.setVisibility(View.GONE);
                }


            }else if(jsonObject.getString("status").contentEquals("false")){
                rl_task_status.setVisibility(View.GONE);

                tv_date.setVisibility(View.GONE);

                tv_start.setVisibility(View.GONE);
                rl_start.setVisibility(View.GONE);

                tv_pause.setVisibility(View.GONE);
                rl_pause.setVisibility(View.GONE);

                tv_stop.setVisibility(View.GONE);
                rl_stop.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //===========Code to get data from api and load data, ends==========
    //-------------location code starts(added by Satabhisha)--------
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            //---minTime(in millisec), minDistance(in meters)
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//        Toast.makeText(getApplicationContext(), "Latitude:" + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//            Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2), Toast.LENGTH_LONG).show();
//            String locationAddress = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2);


            latitude = String.valueOf(location.getLatitude());
//            latitude = "";
            longitude = String.valueOf(location.getLongitude());
//            longitude = "";
            locationAddress = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2);
            Log.d("Location-=>",locationAddress);
            final Context context = this;

        } catch (Exception e) {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    //-------------location code ends(added by Satabhisha)--------

    //===========code to save data for attendance, starts(added by Satabhisha)==============
    public void saveData(String log_action, final String message){
        getLocation();
        final JSONObject DocumentElementobj = new JSONObject();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf_with_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("od_duty_log_date", sdf.format(new Date()));
            DocumentElementobj.put("od_request_id", od_request_id_current_activity);
            DocumentElementobj.put("employee_id",Integer.parseInt(userSingletonModel.getEmployee_id()));
            DocumentElementobj.put("log_action", log_action);
            DocumentElementobj.put("log_datetime", sdf_with_time.format(new Date()));
            DocumentElementobj.put("latitude", latitude);
            DocumentElementobj.put("longitude", longitude);
            DocumentElementobj.put("location_address", locationAddress);
            DocumentElementobj.put("entry_user", LoginActivity.entry_user);


            Log.d("saveAttendance-=>",DocumentElementobj.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }


        //------calling api to save data
        JsonObjectRequest request_json = null;
        String URL = Url.BASEURL()+"od/log/save";
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(DocumentElementobj.toString()),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Process os success response
                                JSONObject jsonObj = null;
                                try{
                                    String responseData = response.toString();
                                    String val = "";
                                    JSONObject resobj = new JSONObject(responseData);
                                    Log.d("getData",resobj.toString());

                                    if(resobj.getString("status").contentEquals("true")){
                                       /* finish();
                                        startActivity(getIntent());
                                        load_data_check_od_duty();*/
//                                        Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(OdDutyLogListActivity.this);
                                        builder.setMessage(message)
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                       /* finish();
                                                        startActivity(getIntent());*/
                                                        load_data_check_od_duty();
                                                        loadData(); //---to refresh the list
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }else{
//                                        Toast.makeText(getApplicationContext(),resobj.getString("message"),Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OdDutyLogListActivity.this);
                                        builder.setMessage(resobj.getString("message"))
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }catch (JSONException e){
                                    //  loading.dismiss();
                                    e.printStackTrace();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========code to save data for attendance, ends==============

    //===========Code to enable gps, starts(added on 1st Sept)=========
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //-----------code to check location permission, code starts(added on 25th nov)---------
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }else if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

//                finish();
            }

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable your GPS")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        startActivity(new Intent(HomeActivity.this, FingerprintActivity.class));
                    }
                });
               /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });*/
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //===========Code to enable gps, ends(added on 1st Sept)=========
}
