package org.wrkplan.payroll.Config;

import static android.app.Service.START_STICKY;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

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
import org.wrkplan.payroll.Data.SqliteDb;
import org.wrkplan.payroll.Home.CustomDashboardPendingItemsListAdapter;
import org.wrkplan.payroll.Home.DashboardActivity;
import org.wrkplan.payroll.Model.DashboardPendingItemModel;
import org.wrkplan.payroll.Model.NotificationModel;
import org.wrkplan.payroll.Model.UserSingletonModel;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class RSSPullService extends Service {
    SQLiteDatabase db, db_customers;
    SqliteDb sqliteDb = new SqliteDb(this);

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    LocationManager locationManager;

    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

//        getLocation();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }else{
            startForeground(1, new Notification());
        }


        //----Using thread to upload data after evry x secs
        final Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
//                        upload_data_delete_sqlite_data_test();
//                        new UploadData().execute(); //--commented on 26th march temporary

                        LoadNotificationData();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();



    }
    public void sendNotification(String title, String message){
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message).build();

            startForeground(1, notification);
        }else{
            startForeground(1, new Notification());
        }
    }

    //---------added on 22nd June to run service all time, code starts------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //---------added on 22nd June, code ends------
    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);


    }

    public class UploadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
//        fetch_sqlite_upload_data_to_server();
            LoadNotificationData();
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean isConnected = ConnectivityReceiver.isConnected();
            if (isConnected == true) {
//                fetch_sqlite_upload_data_to_server(); //---commented on 18th march, as it is not required for NextGen
//                upload_data_and_delete_sqlite_data(); //---not required
//                upload_data_delete_sqlite_data_test(); //---commented on 18th march, as it is not required for NextGen

//                customer_fetch_sqlite_upload_to_server_and_delete_uploaded_sqlite_data(); //---added for nextgen on 18th march
           LoadNotificationData();
            }
        }

    }

    //========///----Notification, code starts---///=======
    public void LoadNotificationData(){

        try {
            db = openOrCreateDatabase("Payroll", MODE_PRIVATE, null);
//            db.execSQL("DROP TABLE IF EXISTS NOTIFICATIONDETAILS");
            db.execSQL("CREATE TABLE IF NOT EXISTS NOTIFICATIONDETAILS(employee_id text, insertYN text, title text, notification_id text, event_name text,event_id text, event_owner_id text, event_owner text, message text)");
        }catch (Exception e){
            e.printStackTrace();
        }

//        String url = Url.BASEURL()+"pending_actions/fetch/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"notification/custom/fetch/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        Log.d("notificationurl-=>",url);
//        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponseNotificationData(response);
//                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void getResponseNotificationData(String response){
        try {

            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            if(jsonObject1.getString("status").contentEquals("true")){

                JSONArray jsonArray = jsonObject.getJSONArray("notifications");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setTitle(jsonObject2.getString("title"));

                    String[] body = jsonObject2.getString("body").split("::");

                    String notification_id_body = body[0];
                    String[] notification_id = notification_id_body.split("=");
                    Log.d("notification_id-=>", notification_id[1]);
                    notificationModel.setNotification_id(notification_id[1]);

                    String event_name_body = body[1];
                    String[] event_name = event_name_body.split("=");
                    notificationModel.setEvent_name(event_name[1]);

                    String event_id_body = body[2];
                    String[] event_id = event_id_body.split("=");
                    notificationModel.setEvent_id(event_id[1]);

                    String event_owner_body = body[3];
                    String[] event_owner = event_owner_body.split("=");
                    notificationModel.setEvent_owner(event_owner[1]);

                    String event_owner_id_body = body[4];
                    String[] event_owner_id = event_owner_id_body.split("=");
                    notificationModel.setEvent_owner_id(event_owner_id[1]);

                    String message_body = body[5];
                    String[] message = message_body.split("=");
                    notificationModel.setMessage(message[1]);

                    notificationModelArrayList.add(notificationModel);
                    CustomNotificationUpdate(Integer.parseInt(notificationModel.getNotification_id()));

                    sendNotification(jsonObject2.getString("title"), message[1]);

                }
                for(int i=0; i<notificationModelArrayList.size(); i++){
                    Log.d("EmployeeId-=>", userSingletonModel.getEmployee_id());
                    sqliteDb.insertNotificationData(userSingletonModel.getEmployee_id(),"Y",notificationModelArrayList.get(i).getTitle(),notificationModelArrayList.get(i).getNotification_id(),notificationModelArrayList.get(i).getEvent_name(),notificationModelArrayList.get(i).getEvent_id(),notificationModelArrayList.get(i).getEvent_owner_id(),notificationModelArrayList.get(i).getEvent_owner(),notificationModelArrayList.get(i).getMessage());
                }
                if (sqliteDb.countNotificationData() > 0){
                    Log.d("Count Data-=>", String.valueOf(sqliteDb.countNotificationData()));
                }
            }else if(jsonObject1.getString("status").contentEquals("false")){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void CustomNotificationUpdate(Integer notificationId){
        final JSONObject DocumentElementobj = new JSONObject();
        try {
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("notification_id", notificationId);
        }catch (Exception e){
            e.printStackTrace();
        }

        //------calling api to save data
        JsonObjectRequest request_json = null;
        String URL = Url.BASEURL()+"notification/custom/update";
        Log.d("testsaveurl-=>",URL);
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
                                    Log.d("getNotificationUpdate",resobj.toString());
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
    //========///----Notification, code ends---///=======


    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
//        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, RSSPullService.class));
        }else{
            startService(new Intent(this, RSSPullService.class));
        }
    }

    @Override
    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }












}
