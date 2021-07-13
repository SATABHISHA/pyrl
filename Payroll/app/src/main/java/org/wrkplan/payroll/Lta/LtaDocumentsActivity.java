package org.wrkplan.payroll.Lta;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.LtaDocumentsModel;
import org.wrkplan.payroll.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LtaDocumentsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_add;
    Uri uripdf=null;
//    public static ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayList = new ArrayList<>();
    public static LinearLayout ll_recycler;
    public static TextView tv_nodata, tv_button_done, tv_button_cancel;
    public static RecyclerView recycler_view;
    public static CustomLtaDocumentsActivityAdapter customLtaDocumentsActivityAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_documents);

        img_add = findViewById(R.id.img_add);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_button_done = findViewById(R.id.tv_button_done);
        tv_button_cancel = findViewById(R.id.tv_button_cancel);

        customLtaDocumentsActivityAdapter = new CustomLtaDocumentsActivityAdapter(this,LtaRequestActivity.ltaDocumentsModelArrayList);



        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
        img_add.setOnClickListener(this);
        tv_button_done.setOnClickListener(this);
        tv_button_cancel.setOnClickListener(this);
        load_data();
    }

    //----onclick code starts-----
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add:
                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select PDF file"),1);
                break;
            case R.id.tv_button_done:
                startActivity(new Intent(LtaDocumentsActivity.this, LtaRequestActivity.class));
                break;
            case R.id.tv_button_cancel:
                if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
                    LtaRequestActivity.ltaDocumentsModelArrayList.clear();
                }
                startActivity(new Intent(LtaDocumentsActivity.this, LtaRequestActivity.class));
                break;
        }

    }
    //----onclick code ends-----
    public static void load_data(){
        /*ll_recycler.setVisibility(View.VISIBLE);
        tv_nodata.setVisibility(View.GONE);

        recycler_view.setAdapter(customLtaDocumentsActivityAdapter);*/
        if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
            ll_recycler.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);

            recycler_view.setAdapter(customLtaDocumentsActivityAdapter);
        }else{
            ll_recycler.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            //getStringPdf(data.getData());
//            Uri uri=data.getData();
//            String uristring=uri.toString();
            //final  String timestamp= ""+ System.currentTimeMillis();
            uripdf=data.getData();

            /*Upload_PDF_Model model=new Upload_PDF_Model("1",getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
            pdf_modelArrayList.add(model);
            adapter=new CustomUploadPDFlistAdapter(pdf_modelArrayList,MediclaimDocumentsActivity.this);
            recycler_pdf.setAdapter(adapter);
            arraylistSize=pdf_modelArrayList.size();
            Toast.makeText(MediclaimDocumentsActivity.this, ""+arraylistSize, Toast.LENGTH_SHORT).show();*/
//            Log.d("base64-=>",getStringPdf(uripdf));
            Log.d("FileSize-=>",getStringPDFsIZE(getApplicationContext(),uripdf));

            LtaDocumentsModel ltaDocumentsModel = new LtaDocumentsModel();
            ltaDocumentsModel.setLta_file_base64(getStringPdf(uripdf));
            ltaDocumentsModel.setLta_filename(getfileName(getApplicationContext(),uripdf));
            ltaDocumentsModel.setLta_file_size(getStringPDFsIZE(getApplicationContext(),uripdf));

            LtaRequestActivity.ltaDocumentsModelArrayList.add(ltaDocumentsModel);

//          loadDocuments();
            load_data();
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

    public static String getStringPDFsIZE(Context context, Uri uri)
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
}
