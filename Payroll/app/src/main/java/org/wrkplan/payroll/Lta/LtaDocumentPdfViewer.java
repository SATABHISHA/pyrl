package org.wrkplan.payroll.Lta;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.barteksc.pdfviewer.PDFView;

import org.wrkplan.payroll.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class LtaDocumentPdfViewer extends AppCompatActivity implements View.OnClickListener {
    PDFView pdfView;
    byte[] decodedString;
    ImageView img_view_dwnld;
    BufferedWriter out = null;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_document_pdf_viewer);

        pdfView = findViewById(R.id.pdfView);
        img_view_dwnld = findViewById(R.id.img_view_dwnld);
        img_back=findViewById(R.id.img_back);

        try {
            decodedString = Base64.decode(CustomLtaDocumentsActivityAdapter.base64String, Base64.DEFAULT);
            pdfView.fromBytes(decodedString).load();
        }catch (Exception e){
            e.printStackTrace();
        }

        img_view_dwnld.setOnClickListener(this);
        img_back.setOnClickListener(this);

        //---code for file download/file access permission, starts
        ActivityCompat.requestPermissions(LtaDocumentPdfViewer.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        //---code for file download/file access permission, ends
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_view_dwnld:
//
                try {
                    byte[] decodedString = Base64.decode(CustomLtaDocumentsActivityAdapter.base64String, Base64.DEFAULT);
                    File file = Environment.getExternalStorageDirectory();
//                    File dir = new File(file.getAbsolutePath() + "/WrkPlan/Document/");
                    File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/WrkPlan/Document/");
//                    File dir = new File(file.getAbsolutePath());
                    if (!dir.exists()) {
                        dir.mkdirs();
                        if(dir.exists()) {
                            Log.d("status-=>", "Directory created");
                            Toast.makeText(getApplicationContext(),"File downloaded successfully",Toast.LENGTH_LONG).show();
                        }else{
                            Log.d("status-=>", "Directory not created");
                        }
                    }
                    File document = new File(dir, CustomLtaDocumentsActivityAdapter.filename);

                    if (document.exists()) {
                        document.delete();
                    }

                    Log.d("DocumentPath-=>",document.getPath());
                    FileOutputStream fos = new FileOutputStream(document.getPath());
                    fos.write(decodedString);
                    fos.close();
                } catch (Exception e) {
                    Log.d("Error-=>: ",e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Failed to download file",Toast.LENGTH_LONG).show();
                            /*Snackbar.make(root_doc, "Failed to download file..", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();*/
                        }
                    });
                }
                break;
            case R.id.img_back:
                super.onBackPressed();
                break;
        }
    }

    //---code for file download/file access permission, starts
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
                    Toast.makeText(LtaDocumentPdfViewer.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //---code for file download/file access permission, ends
}
