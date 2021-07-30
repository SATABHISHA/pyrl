package org.wrkplan.payroll.CompanyDocuments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.CompanyDocumentsModel.Documents;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.EmployeeFacilitisModel.Facilitis;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompanyDocumentsActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Documents> arrayList=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    ListView lv1;
    Button btn_ok;
    TextView tv_nodata;
    ImageView img_back;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CompanyDocumentsActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_documents);
        lv1=findViewById(R.id.lv1);
        btn_ok=findViewById(R.id.btn_ok);
        img_back=findViewById(R.id.img_back);
        tv_nodata=findViewById(R.id.tv_nodata);
        btn_ok.setVisibility(View.GONE);

        img_back.setOnClickListener(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CompanyDocumentsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getdata();


        //---code for file download/file access permission, starts (added on 30th July)
        ActivityCompat.requestPermissions(CompanyDocumentsActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        //---code for file download/file access permission, ends

    }

    private void getdata() {
        String url= Url.BASEURL() + "companydocs/"+"list/"+userSingletonModel.corporate_id;
        Log.d("CompanyDocs-=>",url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("companydata-=>",response);
                    JSONObject jsonObjectResponseStatus = jsonObject.getJSONObject("response");
                    if (jsonObjectResponseStatus.getString("status").contains("true")) {
                        lv1.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("docs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);
                            String name = jb1.getString("name");
                            String upload_datetime = jb1.getString("upload_datetime");
                            String file_name = jb1.getString("file_name");
                            Documents documents = new Documents();
                            documents.setName(name);
                            documents.setUpload_datetime(upload_datetime);
                            documents.setFile_name(file_name);

                            arrayList.add(documents);

                        }
                        lv1.setAdapter(new Nr());
                    }else if (jsonObjectResponseStatus.getString("status").contains("false")) {
                        lv1.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(jsonObjectResponseStatus.getString("message"));
                    }else{
                        lv1.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
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
        Volley.newRequestQueue(CompanyDocumentsActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent=new Intent(CompanyDocumentsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    class Nr extends BaseAdapter{
        @Override
        public int getCount() {
            return  arrayList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.cmpdocuments,null);
            TextView tv_name=view.findViewById(R.id.tv_name);
            TextView tv_date_time=view.findViewById(R.id.tv_date_time);
            ImageView img_download=view.findViewById(R.id.img_download);
            img_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(EmployeeDocumentsActivity.this, " Now Download is not working ", Toast.LENGTH_SHORT).show();
                    String fileUrl = (arrayList.get(position).getFile_name());
                    // String fileUrl = "http://192.168.10.175:9005/HrPayrollDocument/payroll_713_34973216_Wrkplan - New Modules and Features.docx";
//                    String fileName = "caplet";
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);

                    // concatinate above fileExtension to fileName
                    fileName += "." + fileExtension;

                    try {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                                .setTitle(getApplicationContext().getString(R.string.app_name))
                                .setDescription("Downloading " + fileName)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//                            .setDestinationInExternalPublicDir("/Caplet", fileName);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Unable to download file", Toast.LENGTH_LONG).show();
                    }

                }
            });


            tv_name.setText(arrayList.get(position).getName());
            //tv_date_time.setText(arrayList.get(position).getUpload_datetime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
            Date myDate = null;
            try {
                myDate = sdf.parse(arrayList.get(position).getUpload_datetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf.applyPattern(" d MMM yyyy");
            //sdf.applyPattern("d MMM YYYY");
            String sMyDate = sdf.format(myDate);
            tv_date_time.setText(sMyDate);

            return view;
        }
    }

    //---code for file download/file access permission, starts (added on 20-o7-2021)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CompanyDocumentsActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //---code for file download/file access permission, ends
}
