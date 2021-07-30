package org.wrkplan.payroll.EmployeeDocuments;

import android.app.DownloadManager;
import android.content.Intent;
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
import org.wrkplan.payroll.EmployeeDocumentModel.EmpDocuments;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class EmployeeDocumentsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv1;
    Button btn_ok;
    TextView tv_nodata;

    ArrayList<EmpDocuments> arrayList=new ArrayList<>();
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    ImageView img_back;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EmployeeDocumentsActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_documents);
        lv1=findViewById(R.id.lv1);
        img_back=findViewById(R.id.img_back);

        btn_ok=findViewById(R.id.btn_ok);
        tv_nodata = findViewById(R.id.tv_nodata);

        btn_ok.setVisibility(View.GONE);

        img_back.setOnClickListener(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDocumentsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getdata();

    }

    private void getdata() {
        String url= Url.BASEURL() + "employeedocs/"+"list/"+userSingletonModel.corporate_id+"/"+userSingletonModel.user_id;
        Log.d("url->",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("data-=>",response);
                    JSONObject jsonObjectResponseStatus = jsonObject.getJSONObject("response");
                    if (jsonObjectResponseStatus.getString("status").contains("true")) {
                        lv1.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("docs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);
                            String name = jb1.getString("name");
                            String file_path = jb1.getString("file_path");
                            EmpDocuments empDocuments = new EmpDocuments();
                            empDocuments.setName(name);
                            empDocuments.setFile_path(file_path);
                            arrayList.add(empDocuments);

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
        Volley.newRequestQueue(EmployeeDocumentsActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent=new Intent(EmployeeDocumentsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public  class  Nr extends BaseAdapter{
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
            View view=inflater.inflate(R.layout.empdocuments,null);
            TextView tv_name=view.findViewById(R.id.tv_name);
            ImageView img_download=view.findViewById(R.id.img_download);
            img_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(EmployeeDocumentsActivity.this, " Now Download is not working ", Toast.LENGTH_SHORT).show();
                    String fileUrl = (arrayList.get(position).getFile_path());
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
            return view;
        }
    }
}
