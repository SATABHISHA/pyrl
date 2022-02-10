package org.wrkplan.payroll.Mediclaim;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfViewActivity extends AppCompatActivity {

    //    Uri uri;
    PDFView pdfView;
    //  String base64;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
//
//        uri = Uri.parse(getIntent().getStringExtra("uri"));
//        base64 = getIntent().getStringExtra("base64");

        pdfView = findViewById(R.id.pdfView);




        try {
            if (!Url.base64.equalsIgnoreCase("")) {
//            File extDir = Environment.getExternalStorageDirectory();
//            String filename = "myFile.pdf";
//            File fullFilename = new File(extDir.getAbsolutePath() + "/" + filename);
//
//            try {
//                fullFilename.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            fullFilename.setWritable(true);

                byte[] pdfAsBytes = Base64.decode(Url.base64, 0);
//            FileOutputStream os;
//
//            try {
//                os = new FileOutputStream(fullFilename, false);
//                os.write(pdfAsBytes);
//                os.flush();
//                os.close();
//            } catch (Exception exp) {
//                exp.printStackTrace();
//            }

                try {
                    pdfView.fromBytes(pdfAsBytes)
                            .defaultPage(1)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
//                                Toast.makeText(PdfViewActivity.this, "Page: " + page + 1, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .enableAnnotationRendering(true)
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {

                                }
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .spacing(10) // in dp
                            .onPageError(new OnPageErrorListener() {
                                @Override
                                public void onPageError(int page, Throwable t) {
//                                Toast.makeText(PdfViewActivity.this, "Error to show PDF", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .load();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }
            // else if (!uri.toString().isEmpty())
            else  {
                try {
                    // pdfView.fromFile()
                    pdfView.fromUri(Uri.parse(Url.uri))
                            .defaultPage(1)
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
//                                Toast.makeText(PdfViewActivity.this, "Page: " + page + 1, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .enableAnnotationRendering(true)
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {

                                }
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .spacing(10) // in dp
                            .onPageError(new OnPageErrorListener() {
                                @Override
                                public void onPageError(int page, Throwable t) {
//                                Toast.makeText(PdfViewActivity.this, "Error to show PDF", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .load();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Error",e.toString());
        }

//        else {
//            Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
//        }
    }
}