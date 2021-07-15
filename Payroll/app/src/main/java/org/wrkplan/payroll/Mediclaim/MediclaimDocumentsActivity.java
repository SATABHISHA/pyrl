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
import org.wrkplan.payroll.Model.Upload_PDF_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MediclaimDocumentsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img_add;
    RecyclerView recycler_pdf;
    Button btn_cancel,btn_done;
   public static   ArrayList<Upload_PDF_Model> pdf_modelArrayList=new ArrayList<>();
   public static CustomUploadPDFlistAdapter adapter;
    Uri uripdf=null;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    public static int arraylistSize;
    String encodedPdf="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediclaim_documents_activity);

        //-----------------initialize view start--------------------//

        img_add=findViewById(R.id.img_add);
        recycler_pdf=findViewById(R.id.recycler_pdf);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_done=findViewById(R.id.btn_done);


        //-----------------initialize view end--------------------//
        recycler_pdf.setHasFixedSize(true);
        recycler_pdf.setLayoutManager(new LinearLayoutManager(this));

        btn_cancel.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        img_add.setOnClickListener(this);

//        img_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPDF();
//            }
//        });
        loadDocuments();

       getStatus();

       if(Url.isSubordinateMediclaim==true)
       {
           img_add.setVisibility(View.INVISIBLE);
           btn_done.setVisibility(View.GONE);
       }

    }

    private void getStatus() {

    }

    private void selectPDF() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF file"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {

        uripdf=data.getData();
//            if(!pdf_modelArrayList.isEmpty())
//            {
//                pdf_modelArrayList.clear();
//            }
//            Upload_PDF_Model model=new Upload_PDF_Model(getBase64(),getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
            Upload_PDF_Model model=new Upload_PDF_Model(ConvertToString(uripdf),getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);


            for(int i=0;i<pdf_modelArrayList.size();i++)
            {
                if(pdf_modelArrayList.get(i).getFile_name().contentEquals(getfileName(getApplicationContext(),uripdf)))
                {
                    Toast.makeText(this, "Same file name Not allowed", Toast.LENGTH_SHORT).show();
                }
            }

            pdf_modelArrayList.add(model);
            adapter=new CustomUploadPDFlistAdapter(pdf_modelArrayList,MediclaimDocumentsActivity.this);
            recycler_pdf.setAdapter(adapter);
            arraylistSize=pdf_modelArrayList.size();
            Toast.makeText(MediclaimDocumentsActivity.this, ""+arraylistSize, Toast.LENGTH_SHORT).show();
        // Eta run kore dakh ekabaar
//          loadDocuments();
        }
    }
    //KP
    public String ConvertToString(Uri uri){
        String Document = null;
       String uriString = uri.toString();
        Log.d("data", "onActivityResult: uri"+uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes=getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);
            Document=Base64.encodeToString(bytes,Base64.DEFAULT);

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
        if(Url.isMyMediclaim==true )
        {
            String url= Url.BASEURL()+"mediclaim/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomMediclaimListAdapter.mediclaim_id;
            Log.d("my_mediclaim_url=>",url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimDocumentsActivity.this, "Loading", "Please wait...", true, false);
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(!pdf_modelArrayList.isEmpty())
                        {
                            pdf_modelArrayList.clear();
                        }
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("documents");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jb1=jsonArray.getJSONObject(i);
                            // Upload_PDF_Model model=new Upload_PDF_Model("1",getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
                            Upload_PDF_Model model=new Upload_PDF_Model(jb1.getString("file_base64"),
                                    jb1.getString("file_name"),
                                    jb1.getString("file_path"),
                                    jb1.getString("mediclaim_id"));
                            pdf_modelArrayList.add(model);
                            adapter=new CustomUploadPDFlistAdapter(pdf_modelArrayList,MediclaimDocumentsActivity.this);
                            recycler_pdf.setAdapter(adapter);
                            arraylistSize=pdf_modelArrayList.size();
                            Toast.makeText(MediclaimDocumentsActivity.this, ""+arraylistSize, Toast.LENGTH_SHORT).show();

                        }

                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(MediclaimDocumentsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        if(Url.isSubordinateMediclaim==true)
        {
            String url= Url.BASEURL()+"mediclaim/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomSubordinateMediclaimListAdapter.mediclaim_id;
            Log.d("my_mediclaim_url=>",url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimDocumentsActivity.this, "Loading", "Please wait...", true, false);
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(!pdf_modelArrayList.isEmpty())
                        {
                            pdf_modelArrayList.clear();
                        }
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("documents");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jb1=jsonArray.getJSONObject(i);
                            // Upload_PDF_Model model=new Upload_PDF_Model("1",getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
                            Upload_PDF_Model model=new Upload_PDF_Model(jb1.getString("file_base64"),
                                    jb1.getString("file_name"),
                                    jb1.getString("file_path"),
                                    jb1.getString("mediclaim_id"));
                            pdf_modelArrayList.add(model);
                            adapter=new CustomUploadPDFlistAdapter(pdf_modelArrayList,MediclaimDocumentsActivity.this);
                            recycler_pdf.setAdapter(adapter);
                            arraylistSize=pdf_modelArrayList.size();
                            Toast.makeText(MediclaimDocumentsActivity.this, ""+arraylistSize, Toast.LENGTH_SHORT).show();

                        }

                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(MediclaimDocumentsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public String getStringPdf (Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

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

    public static String getStringPDFsIZE(Context context,Uri uri)
    {
       String filesize="";
       String final_pdf_size="";
        Cursor cursor=context.getContentResolver().query(uri,null,null,null,null,null);
        try {
            if(cursor!=null && cursor.moveToFirst())
            {
                // getfile size
                int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
                if(!cursor.isNull(sizeIndex))
                {
                    filesize=cursor.getString(sizeIndex);
                   int a=Integer.parseInt(filesize)/1024 ;
                   if(a>1024)
                   {
                       int b=a/1024;
                       final_pdf_size=b+" MB";
                   }
                   else
                   {
                       final_pdf_size=a+" KB";
                   }


                }
            }
        }
        finally {
            cursor.close();
        }
        return final_pdf_size;

    }
    public static String getfileName(Context context,Uri uri)
    {
        String filename="";

        Cursor cursor=context.getContentResolver().query(uri,null,null,null,null,null);
        try {
            if(cursor!=null && cursor.moveToFirst())
            {
                // getfile name
                filename=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }
        }
        finally {
            cursor.close();
        }
        return filename;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:


                break;
            case  R.id.btn_done:

               Intent intent=new Intent(MediclaimDocumentsActivity.this,MediclaimEntryActivity.class);
               startActivity(intent);

               break;

            case  R.id.img_add:

                    selectPDF();
                break;
            default:
                break;
        }
    }
public  String getBase64()
{
    try {
        InputStream inputStream=MediclaimDocumentsActivity.this.getContentResolver().openInputStream(uripdf);
        byte[] bpdfbytes=new byte[inputStream.available()];
        inputStream.read(bpdfbytes);
        encodedPdf=Base64.encodeToString(bpdfbytes,Base64.DEFAULT);
        Toast.makeText(this, ""+encodedPdf, Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return encodedPdf;
}

}
