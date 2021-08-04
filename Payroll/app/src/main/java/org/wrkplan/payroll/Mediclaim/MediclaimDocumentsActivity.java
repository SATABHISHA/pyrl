package org.wrkplan.payroll.Mediclaim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Upload_PDF_Model;
import org.wrkplan.payroll.Model.Mediclaim.Upload_PDF_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MediclaimDocumentsActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Upload_PDF_Model> pdf_modelArrayList = new ArrayList<>();
    public static CustomUploadPDFlistAdapter adapter;
    public ArrayList<String> posArrayList = new ArrayList<>();
    public static ArrayList<Subordinate_Upload_PDF_Model> subordinate_arraylist = new ArrayList<>();
    public static CustomSubordinateUploadPDFlistAdapter subordinate_adapter;
    public static int arraylistSize;
    ImageView img_add;
    RecyclerView recycler_pdf,subordinate_recycler_pdf;
    Button btn_cancel, btn_done;
    Uri uripdf = null;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    String encodedPdf = "";
    public  static  String med_status="";
    String sub_med_status="";
    public static boolean flag=false;
    public static boolean sub_flag=false;
    boolean iscancel=false;
    boolean isdone=false;
    ImageView img_back;

    RelativeLayout rl1,rl2;

    public static String getStringPDFsIZE(Context context, Uri uri) {
        String filesize = "";
        String final_pdf_size = "";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // getfile size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    filesize = cursor.getString(sizeIndex);
                    int a = Integer.parseInt(filesize) / 1024;
                    if (a > 1024) {
                        int b = a / 1024;
                        final_pdf_size = b + " MB";
                    } else {
                        final_pdf_size = a + " KB";
                    }


                }
            }
        } finally {
            cursor.close();
        }
        return final_pdf_size;

    }

    public static String getfileName(Context context, Uri uri) {
        String filename = "";

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // getfile name
                filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }
        } finally {
            cursor.close();
        }
        return filename;

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        if(isdone)
        {
            super.onBackPressed();
        }
        else
        {

            if(Url.isNewEntryMediclaim==true)
            {
                super.onBackPressed();
                pdf_modelArrayList.clear();
            }
            else if(Url.isMyMediclaim==true)
            {
                for (int i=0;i<posArrayList.size(); i++)
                {
                    int pos = Integer.parseInt(posArrayList.get(i));
                    pdf_modelArrayList.remove(pos-i);
                }
                super.onBackPressed();
            } else if(Url.isSubordinateMediclaim==true)
            {
                super.onBackPressed();
            }

        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediclaim_documents_activity);

        //-----------------initialize view start--------------------//

        img_back=findViewById(R.id.img_back);
        img_add = findViewById(R.id.img_add);
        recycler_pdf = findViewById(R.id.recycler_pdf);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_done = findViewById(R.id.btn_done);
        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);
        subordinate_recycler_pdf = findViewById(R.id.subordinate_recycler_pdf);


        //-----------------initialize view end--------------------//
        recycler_pdf.setHasFixedSize(true);
        recycler_pdf.setLayoutManager(new LinearLayoutManager(this));

        subordinate_recycler_pdf.setHasFixedSize(true);
        subordinate_recycler_pdf.setLayoutManager(new LinearLayoutManager(this));

        btn_cancel.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        img_add.setOnClickListener(this);
        img_back.setOnClickListener(this);

//        if (!pdf_modelArrayList.isEmpty()) {
//            pdf_modelArrayList.clear();
//            Toast.makeText(this, "ArrayList cleared", Toast.LENGTH_SHORT).show();
//        }

        adapter = new CustomUploadPDFlistAdapter(pdf_modelArrayList, MediclaimDocumentsActivity.this);
        recycler_pdf.setAdapter(adapter);





        if(Url.isMyMediclaim==true || Url.isNewEntryMediclaim==true)
        {

            rl2.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
        }


        if(Url.isSubordinateMediclaim==true)
        {
            rl2.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.GONE);
            loadSubordinateDocuments();
        }

        //load documents for employees end
        // loadDocuments();



        med_status=getIntent().getStringExtra("employee_status=>");
        sub_med_status=getIntent().getStringExtra("subordinate_mediclaim_status=>");

//check for employee based on status
        if(Url.isMyMediclaim==true)
        {
            if(med_status.equals("Submitted"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                flag=true;
            }
            if(med_status.equals("Approved"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                flag=true;
            }
            if(med_status.equals("Canceled"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                flag=true;
            }
            if(med_status.equals("Returned"))
            {
                img_add.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.VISIBLE);
            }
        }
//check for subordinate based on status
        if(Url.isSubordinateMediclaim==true)
        {
            if(sub_med_status.equals("Submitted"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                sub_flag=true;

            }
            if(sub_med_status.equals("Approved"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                sub_flag=true;

            }
            if(sub_med_status.equals("Canceled"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                sub_flag=true;
            }
            if(sub_med_status.equals("Returned"))
            {
                img_add.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                sub_flag=true;
            }
        }


    }

    private void loadSubordinateDocuments() {
        if(Url.isSubordinateMediclaim==true)
        {
            String url = Url.BASEURL() + "mediclaim/detail/" + userSingletonModel.getCorporate_id() + "/" + CustomSubordinateMediclaimListAdapter.mediclaim_id;
            Log.d("my_sub_mediclaim_url=>", url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimDocumentsActivity.this, "Loading", "Please wait...", true, false);

            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        if (!subordinate_arraylist.isEmpty()) {
                            subordinate_arraylist.clear();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("documents");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);

//                            Subordinate_Upload_PDF_Model model1 = new Subordinate_Upload_PDF_Model(jb1.getString("file_base64"),
//                                    jb1.getString("file_name"),
//                                    jb1.getString("file_path"),
//                                    jb1.getString("mediclaim_id"));
                            Subordinate_Upload_PDF_Model model=new Subordinate_Upload_PDF_Model();
                            model.setFile_base64(jb1.getString("file_base64"));
                            model.setFile_name(jb1.getString("file_name"));
                            model.setFile_path(jb1.getString("file_path"));
                            model.setFile_path(jb1.getString("mediclaim_id"));


                            subordinate_arraylist.add(model);
                            subordinate_adapter = new CustomSubordinateUploadPDFlistAdapter(subordinate_arraylist, MediclaimDocumentsActivity.this);
                            subordinate_recycler_pdf.setAdapter(subordinate_adapter);
                            arraylistSize = subordinate_arraylist.size();
//                            Toast.makeText(MediclaimDocumentsActivity.this, "" + arraylistSize, Toast.LENGTH_SHORT).show();

                        }
                        loading.dismiss();
                    } catch (JSONException e) {
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(MediclaimDocumentsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(MediclaimDocumentsActivity.this, "could't connect to the server", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(MediclaimDocumentsActivity.this).add(stringRequest);
        }

    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uripdf = data.getData();

//            Upload_PDF_Model model=new Upload_PDF_Model(getBase64(),getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
            Upload_PDF_Model model = new Upload_PDF_Model(ConvertToString(uripdf), getfileName(getApplicationContext(), uripdf), getStringPdf(uripdf), CustomMediclaimListAdapter.mediclaim_id,uripdf);
            model.setFromapi_yn(false);
            //  Upload_PDF_Model model = new Upload_PDF_Model(ConvertToString(uripdf), getfileName(getApplicationContext(), uripdf), getStringPdf(uripdf), CustomMediclaimListAdapter.mediclaim_id);
            boolean isAlreadyIn = false;

            for (int i = 0; i < pdf_modelArrayList.size(); i++) {
                if (pdf_modelArrayList.get(i).getFile_name().equalsIgnoreCase(model.getFile_name())) {
                    isAlreadyIn = true;
                    break;
                }
            }
            if (isAlreadyIn) {
                Toast.makeText(this, model.getFile_name() + " file is already selected", Toast.LENGTH_SHORT).show();
            } else {
                posArrayList.add(String.valueOf(pdf_modelArrayList.size()));
                pdf_modelArrayList.add(model);
                adapter = new CustomUploadPDFlistAdapter(pdf_modelArrayList, MediclaimDocumentsActivity.this);
                recycler_pdf.setAdapter(adapter);
                arraylistSize = pdf_modelArrayList.size();
//                Toast.makeText(this, ""+pdf_modelArrayList.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String ConvertToString(Uri uri) {
        String Document = null;
        String uriString = uri.toString();
        Log.d("data", "onActivityResult: uri" + uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.d("data", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            Document = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
        return Document;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void loadDocuments() {

        if (Url.isMyMediclaim == true) {
            String url = Url.BASEURL() + "mediclaim/detail/" + userSingletonModel.getCorporate_id() + "/" + CustomMediclaimListAdapter.mediclaim_id;
            Log.d("my_mediclaim_url=>", url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimDocumentsActivity.this, "Loading", "Please wait...", true, false);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (!pdf_modelArrayList.isEmpty()) {
                            pdf_modelArrayList.clear();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("documents");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb1 = jsonArray.getJSONObject(i);

//                            Upload_PDF_Model model = new Upload_PDF_Model(jb1.getString("file_base64"),
//                                    jb1.getString("file_name"),
//                                    jb1.getString("file_path"),
//                                    jb1.getString("mediclaim_id"));

                            Upload_PDF_Model model = new Upload_PDF_Model();
                            model.setFile_base64(jb1.getString("file_base64"));
                            model.setFile_name(jb1.getString("file_name"));
                            model.setFile_path(jb1.getString("file_path"));
                            model.setFile_path(jb1.getString("mediclaim_id"));

                            pdf_modelArrayList.add(model);
                            adapter = new CustomUploadPDFlistAdapter(pdf_modelArrayList, MediclaimDocumentsActivity.this);
                            recycler_pdf.setAdapter(adapter);
                            arraylistSize = pdf_modelArrayList.size();
                            Toast.makeText(MediclaimDocumentsActivity.this, "" + arraylistSize, Toast.LENGTH_SHORT).show();

                        }

                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(MediclaimDocumentsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(MediclaimDocumentsActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(MediclaimDocumentsActivity.this).add(stringRequest);
        }

    }

    public String getStringPdf(Uri filepath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                iscancel=true;
                onBackPressed();
                break;
            case R.id.btn_cancel:

                iscancel=true;
                onBackPressed();

                break;
            case R.id.btn_done:
//
//                Intent intent = new Intent(MediclaimDocumentsActivity.this, MediclaimEntryActivity.class);
//                startActivity(intent);

                isdone=true;
                onBackPressed();


                break;

            case R.id.img_add:

                selectPDF();
                break;
            default:
                break;
        }
    }

    public String getBase64() {
        try {
            InputStream inputStream = MediclaimDocumentsActivity.this.getContentResolver().openInputStream(uripdf);
            byte[] bpdfbytes = new byte[inputStream.available()];
            inputStream.read(bpdfbytes);
            encodedPdf = Base64.encodeToString(bpdfbytes, Base64.DEFAULT);
            Toast.makeText(this, "" + encodedPdf, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedPdf;
    }

}
