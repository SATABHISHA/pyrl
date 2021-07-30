package org.wrkplan.payroll.Lta;

import android.content.Context;
import android.content.DialogInterface;
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
    public ImageView img_add;
    public View view_dcmnts_border_line;
    Uri uripdf=null;
    ImageView img_back;
//    public static ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayList = new ArrayList<>();
    public static LinearLayout ll_recycler, ll_button;
    public static TextView tv_nodata, tv_button_done, tv_button_cancel;
    public static RecyclerView recycler_view;
    public static CustomLtaDocumentsActivityAdapter customLtaDocumentsActivityAdapter;
    public static ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayListTemp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_documents);

        img_add = findViewById(R.id.img_add);
        ll_recycler = findViewById(R.id.ll_recycler);
        tv_nodata = findViewById(R.id.tv_nodata);
        tv_button_done = findViewById(R.id.tv_button_done);
        tv_button_cancel = findViewById(R.id.tv_button_cancel);
        img_back=findViewById(R.id.img_back);
        view_dcmnts_border_line = findViewById(R.id.view_dcmnts_border_line);
        ll_button = findViewById(R.id.ll_button);


        if (!ltaDocumentsModelArrayListTemp.isEmpty()){
            ltaDocumentsModelArrayListTemp.clear();
        }
        if (!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
            ltaDocumentsModelArrayListTemp = (ArrayList)LtaRequestActivity.ltaDocumentsModelArrayList.clone();
        }


//        customLtaDocumentsActivityAdapter = new CustomLtaDocumentsActivityAdapter(this,LtaRequestActivity.ltaDocumentsModelArrayList);
        customLtaDocumentsActivityAdapter = new CustomLtaDocumentsActivityAdapter(this,ltaDocumentsModelArrayListTemp);



        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
        img_back.setOnClickListener(this);
        img_add.setOnClickListener(this);
        tv_button_done.setOnClickListener(this);
        tv_button_cancel.setOnClickListener(this);
        load_data();
        LoadButtons();
    }

    //----function to load buttons acc to the logic, code starts
    public void LoadButtons(){


        if (LtaListActivity.EmployeeType == "Employee"){

            if (LtaListActivity.mediclaim_status.contentEquals("")){
                tv_button_done.setVisibility(View.VISIBLE);
                tv_button_cancel.setVisibility(View.VISIBLE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.VISIBLE);
                view_dcmnts_border_line.setVisibility(View.VISIBLE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.VISIBLE);


            }
            if (LtaListActivity.mediclaim_status.contentEquals("Saved")){


                tv_button_done.setVisibility(View.VISIBLE);
                tv_button_cancel.setVisibility(View.VISIBLE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.VISIBLE);
                view_dcmnts_border_line.setVisibility(View.VISIBLE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.VISIBLE);

            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Submitted")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                tv_button_done.setVisibility(View.GONE);
//                tv_button_cancel.setVisibility(View.GONE);
                tv_button_cancel.setVisibility(View.VISIBLE);
//                ll_button.setVisibility(View.GONE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.GONE);
                view_dcmnts_border_line.setVisibility(View.GONE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
            if (LtaListActivity.mediclaim_status.contentEquals("Returned")){

                tv_button_done.setVisibility(View.VISIBLE);
                tv_button_cancel.setVisibility(View.VISIBLE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.VISIBLE);
                view_dcmnts_border_line.setVisibility(View.VISIBLE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
        }
        if (LtaListActivity.EmployeeType == "Supervisor"){
//            LabelNavBarTitle.text = "Subordinate Advance Requisition"
//            btn_reason_select_type.isUserInteractionEnabled = false
//            btn_reason_select_type.alpha = 0.6
            if (LtaListActivity.mediclaim_status.contentEquals("Submitted")){
                tv_button_done.setVisibility(View.GONE);
//                tv_button_cancel.setVisibility(View.GONE);
                tv_button_cancel.setVisibility(View.VISIBLE);
//                ll_button.setVisibility(View.GONE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.GONE);
                view_dcmnts_border_line.setVisibility(View.GONE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Returned")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Cancelled"))){

                tv_button_done.setVisibility(View.GONE);
//                tv_button_cancel.setVisibility(View.GONE);
                tv_button_cancel.setVisibility(View.VISIBLE);
//                ll_button.setVisibility(View.GONE);
                ll_button.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.GONE);
                view_dcmnts_border_line.setVisibility(View.GONE);
//                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
        }


    }

    //----function to load buttons acc to the logic, code ends
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
                if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
                    LtaRequestActivity.ltaDocumentsModelArrayList.clear();
                }
                LtaRequestActivity.ltaDocumentsModelArrayList = (ArrayList)ltaDocumentsModelArrayListTemp.clone();
//                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                super.onBackPressed();
//                startActivity(new Intent(LtaDocumentsActivity.this, LtaRequestActivity.class));

                Log.d("DeleteTesting-=>",LtaRequestActivity.delete_documents_id_arraylist.toString());
                break;
            case R.id.img_back:
                if(!ltaDocumentsModelArrayListTemp.isEmpty()){
                    ltaDocumentsModelArrayListTemp.clear();
                }
                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                onBackPressed();
                break;
            case R.id.tv_button_cancel:
                /*if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
                    LtaRequestActivity.ltaDocumentsModelArrayList.clear();
                }
                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                super.onBackPressed();*/
//                startActivity(new Intent(LtaDocumentsActivity.this, LtaRequestActivity.class));
                //----added pn 26th julu, code starts------
               /* androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LtaDocumentsActivity.this);
                builder.setMessage("Your unsaved data would be lost. Are you sure you want to cancel?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
                                    LtaRequestActivity.ltaDocumentsModelArrayList.clear();
                                }
                                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                                onBackPressed();


                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();*/
                //----added pn 26th julu, code ends------
                //----added on 27th July, code starts----
                if(!ltaDocumentsModelArrayListTemp.isEmpty()){
                    ltaDocumentsModelArrayListTemp.clear();
                }
                LtaRequestActivity.tv_docs.setText(String.valueOf(LtaRequestActivity.ltaDocumentsModelArrayList.size())+" Doc(s)");
                onBackPressed();
                //----added on 27th July, code ends----
                break;
        }

    }
    //----onclick code ends-----
    public static void load_data(){
        /*ll_recycler.setVisibility(View.VISIBLE);
        tv_nodata.setVisibility(View.GONE);

        recycler_view.setAdapter(customLtaDocumentsActivityAdapter);*/
//        if(!LtaRequestActivity.ltaDocumentsModelArrayList.isEmpty()){
        if(!ltaDocumentsModelArrayListTemp.isEmpty()){
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
            ltaDocumentsModel.setLta_file_from_api_yn("No");
            ltaDocumentsModel.setLta_id("0");

//            LtaRequestActivity.ltaDocumentsModelArrayList.add(ltaDocumentsModel);
            ltaDocumentsModelArrayListTemp.add(ltaDocumentsModel);

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
