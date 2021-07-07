package org.wrkplan.payroll.Lta;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.print.PDFPrint;
import android.print.PrintAttributes;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.wrkplan.payroll.R;
import org.wrkplan.payroll.Reports.PdfEditorActivity;
import org.wrkplan.payroll.Reports.ReportHomeListActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LtaDocumentPdfEditorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PdfEditorActivity";

    private WebView webView;
    ImageView img_view_dwnld;
    byte[] decodedString;



    public static class MyWebViewClient extends WebViewClient {

        public interface OnSourceReceived {
            void success(String html);
        }

        private final PdfEditorActivity.MyWebViewClient.OnSourceReceived onSourceReceived;

        public MyWebViewClient(PdfEditorActivity.MyWebViewClient.OnSourceReceived onSourceReceived) {
            this.onSourceReceived = onSourceReceived;
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("source://")) {
                try {
                    String html = URLDecoder.decode(url, "UTF-8").substring(9);
                    onSourceReceived.success(html);
                } catch (UnsupportedEncodingException e) {
                    Log.e("example", "failed to decode source", e);
                }
                return true;
            }
            // For all other links, let the WebView do it's normal thing
            return false;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_document_pdf_editor);
        img_view_dwnld = findViewById(R.id.img_view_dwnld);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pdf Editor");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.colorTransparentBlack)));
        }

        webView = (WebView) findViewById(R.id.webViewEditor);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new PdfEditorActivity.MyWebViewClient(new PdfEditorActivity.MyWebViewClient.OnSourceReceived() {
            @Override
            public void success(String html) {
                Log.d(TAG, "success: html: " + html);
                FileManager.getInstance().cleanTempFolder(getApplicationContext());
                // Create Temp File to save Pdf To
                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                // Generate Pdf From Html
                PDFUtil.generatePDFFromWebView(savedPDFFile, webView, new PDFPrint.OnPDFPrintListener() {
                    @Override
                    public void onSuccess(File file) {
                        // Open Pdf Viewer
                       /* Uri pdfUri = Uri.fromFile(savedPDFFile);

                        Intent intentPdfViewer = new Intent(PdfEditorActivity.this, PdfViewerActivity.class);
                        intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                        startActivity(intentPdfViewer);*/
                        File fileToPrint = file;
                        if (fileToPrint == null || !fileToPrint.exists()) {
                            Toast.makeText(LtaDocumentPdfEditorActivity.this, "Generated File is null or does not exist!", Toast.LENGTH_SHORT).show();
//                            return super.onOptionsItemSelected(item);
                        }

                        PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
                        printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                        printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

                        PDFUtil.printPdf(LtaDocumentPdfEditorActivity.this, fileToPrint, printAttributeBuilder.build());
                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        }));

//        webView.loadData("<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<body>\n" +
//                "\n" +
//                "<p contenteditable=\"true\">This is a paragraph. It is editable. Try to change this text.</p>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>\n", "text/HTML", "UTF-8");

//        webView.loadUrl("file:///android_asset/invoice.html");
//        webView.loadData(HtmlStringDemo.html_hardcoded_string_demo,"text/html",null);
//        webView.loadDataWithBaseURL(null, ReportHomeListActivity.report_html, "text/html", "utf-8", null);
        decodedString = Base64.decode(CustomLtaDocumentsActivityAdapter.base64String, Base64.DEFAULT);
        webView.loadDataWithBaseURL(null, decodedString.toString(), "application/pdf", "utf-8", null);

        img_view_dwnld.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_view_dwnld:
                webView.loadUrl("javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pdf_editor, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menuPrintPdf) {
            webView.loadUrl("javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
        }
        return super.onOptionsItemSelected(item);
    }
}
