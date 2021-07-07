package org.wrkplan.payroll.Lta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.wrkplan.payroll.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LtaDocumentsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_add;
    Uri uripdf=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_documents);

        img_add = findViewById(R.id.img_add);
        img_add.setOnClickListener(this);
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
        }

    }
    //----onclick code ends-----

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
            Log.d("base64-=>",getStringPdf(uripdf));

//          loadDocuments();
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
}
