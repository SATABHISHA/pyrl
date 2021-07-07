package org.wrkplan.payroll.Lta;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_document_pdf_viewer);

        pdfView = findViewById(R.id.pdfView);
        img_view_dwnld = findViewById(R.id.img_view_dwnld);

        decodedString = Base64.decode(CustomLtaDocumentsActivityAdapter.base64String, Base64.DEFAULT);
        pdfView.fromBytes(decodedString).load();

        img_view_dwnld.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_view_dwnld:
//                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                FileOutputStream fos = null;
//                File file = new File("./test.pdf");
                try {

                        fos = this.openFileOutput(CustomLtaDocumentsActivityAdapter.filename+".pdf", Context.MODE_PRIVATE);
                        byte[] decodedString = Base64.decode(CustomLtaDocumentsActivityAdapter.base64String, Base64.DEFAULT);
                        fos.write(decodedString);
                        fos.flush();
                        fos.close();

                } catch (Exception e) {

                } finally {
                    if (fos != null) {
                        fos = null;
                    }
                }
                break;
        }
    }
}
