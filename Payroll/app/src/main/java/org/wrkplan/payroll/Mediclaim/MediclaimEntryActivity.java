package org.wrkplan.payroll.Mediclaim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Mediclaim_Details_Model;
import org.wrkplan.payroll.Model.Model_Base64;
import org.wrkplan.payroll.Model.Upload_PDF_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class MediclaimEntryActivity extends AppCompatActivity implements  View.OnClickListener{
    TextView tv_check_balance,tv_mediclaim_no,edtv_mediclaim_no,tv_employee_name,edtv_employee_name,tv_mediclaim_amount,
            tv_reason,tv_documents,tv_docs,tv_views,tv_approved_amount,tv_approval_remark,tv_application_status,
            edtv_application_status,tv_mediclaim_title;
    EditText ed_mediclaim_amount,ed_reason,ed_approved_amount,ed_approval_remark;

    Button btn_cancel,btn_return,btn_approve,btn_submit,btn_save,btn_back;
    ArrayList<Mediclaim_Details_Model> details_modelArrayList=new ArrayList<>();

    //---------------------- popup text views and button----------------
    TextView tv_period,txt_period,tv_unutilized_amount,txt_unutilized_amount,tv_1years,tv_limit_year,
            txt_limit_year,tv_2years,tv_amount_disbursed,txt_amount_disbursed,tv_balance_available,txt_balance_available;
    Button btn_ok;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

    JSONObject my_mediclaim_jsonBody=new JSONObject();
    ArrayList<Model_Base64> arrayList=new ArrayList<>();
    //----------------------------X-------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediclaim_entry_activity);

        // -------initialize text views start--------------//
        tv_check_balance=findViewById(R.id.tv_check_balance);
        tv_mediclaim_no=findViewById(R.id.tv_mediclaim_no);
        edtv_mediclaim_no=findViewById(R.id.edtv_mediclaim_no);
        tv_employee_name=findViewById(R.id.tv_employee_name);
        edtv_employee_name=findViewById(R.id.edtv_employee_name);
        tv_mediclaim_amount=findViewById(R.id.tv_mediclaim_amount);
        tv_reason=findViewById(R.id.tv_reason);
        tv_mediclaim_title=findViewById(R.id.tv_mediclaim_title);
        tv_documents=findViewById(R.id.tv_documents);
        tv_docs=findViewById(R.id.tv_docs);
        tv_views=findViewById(R.id.tv_views);
        tv_approved_amount=findViewById(R.id.tv_approved_amount);
        tv_approval_remark=findViewById(R.id.tv_approval_remark);
        tv_application_status=findViewById(R.id.tv_application_status);
        edtv_application_status=findViewById(R.id.edtv_application_status);
        // -------initialize text views end--------------//
    //-------------------------------------------------------------------------------//
        // -------initialize edit text start--------------//
        ed_mediclaim_amount=findViewById(R.id.ed_mediclaim_amount);
        ed_reason=findViewById(R.id.ed_reason);
        ed_approved_amount=findViewById(R.id.ed_approved_amount);
        ed_approval_remark=findViewById(R.id.ed_approval_remark);
        // -------initialize edit text start--------------//

        //----------------------------------------------------------------------------------//


        // -------initialize Button end--------------//

        btn_cancel=findViewById(R.id.btn_cancel);
        btn_return=findViewById(R.id.btn_return);
        btn_approve=findViewById(R.id.btn_approve);
        btn_submit=findViewById(R.id.btn_submit);
        btn_save=findViewById(R.id.btn_save);
        btn_back=findViewById(R.id.btn_back);
        // -------initialize Button end--------------//



        btn_cancel.setOnClickListener(this);
        btn_approve.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        if(! MediclaimDocumentsActivity.pdf_modelArrayList.isEmpty())
        {
            MediclaimDocumentsActivity.pdf_modelArrayList.clear();
        }


        //------------------------Open Check Balance POPUP-----------------------------------------//
            tv_check_balance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MediclaimEntryActivity.this);
                    builder.setTitle("");
                    final  View custom=getLayoutInflater().inflate(R.layout.popup_account_balance_activity,null);



                    tv_period=custom.findViewById(R.id.tv_period);
                    txt_period=custom.findViewById(R.id.txt_period);
                    tv_unutilized_amount=custom.findViewById(R.id.tv_unutilized_amount);
                    txt_unutilized_amount=custom.findViewById(R.id.txt_unutilized_amount);
                    tv_1years=custom.findViewById(R.id.tv_1years);
                    tv_limit_year=custom.findViewById(R.id.tv_limit_year);
                    txt_limit_year=custom.findViewById(R.id.txt_limit_year);
                    tv_2years=custom.findViewById(R.id.tv_2years);
                    tv_amount_disbursed=custom.findViewById(R.id.tv_amount_disbursed);
                    txt_amount_disbursed=custom.findViewById(R.id.txt_amount_disbursed);
                    tv_balance_available=custom.findViewById(R.id.tv_balance_available);
                    txt_balance_available=custom.findViewById(R.id.txt_balance_available);
                    btn_ok=custom.findViewById(R.id.btn_ok);




                    builder.setView(custom);
                    final AlertDialog dialog = builder.create();
                    dialog.show();


                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }

                    });

                }
            });
        //------------------------Close Check Balance POPUP-----------------------------------------//
  if(Url.isMyMediclaim==true)
  {
      tv_docs.setText(MediclaimDocumentsActivity.arraylistSize+" Doc(s)");
  }
        if(Url.isNewEntryMediclaim==true)
        {

           tv_docs.setText(MediclaimDocumentsActivity.arraylistSize+" Doc(s)");


        }


        tv_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Url.isNewEntryMediclaim==true)
                {
                    Url.isMyMediclaim=false;
                    Url.isSubordinateMediclaim=false;
                    Intent intent=new Intent(MediclaimEntryActivity.this,MediclaimDocumentsActivity.class);
                    startActivity(intent);
                }
                if(Url.isMyMediclaim==true)
                {
                    Url.isNewEntryMediclaim=false;
                    Url.isSubordinateMediclaim=false;
                    Intent intent=new Intent(MediclaimEntryActivity.this,MediclaimDocumentsActivity.class);
                    startActivity(intent);
                }
                if(Url.isSubordinateMediclaim==true)
                {
                    Url.isNewEntryMediclaim=false;
                    Url.isMyMediclaim=false;
                    Intent intent=new Intent(MediclaimEntryActivity.this,MediclaimDocumentsActivity.class);
                    startActivity(intent);
                }

                //finish();
            }
        });

        if(Url.isNewEntryMediclaim==true)
        {
            ed_approval_remark.setEnabled(false);
            ed_approved_amount.setEnabled(false);
            edtv_application_status.setEnabled(false);
            edtv_employee_name.setText(userSingletonModel.getFull_employee_name());


            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
            btn_back.setVisibility(View.VISIBLE);


        }


        if(Url.isNewEntryMediclaim==false && Url.isSubordinateMediclaim==false)
        {
            EditEmployeeMode();
            tv_mediclaim_title.setText("My Mediclaim Detail");
            edtv_employee_name.setText(userSingletonModel.getFull_employee_name());
        }
        
        if(Url.isNewEntryMediclaim==false && Url.isMyMediclaim==false)
        {
            EditSubordinateMediclaim();
            tv_mediclaim_title.setText("Subordinate Mediclaim Detail");
            edtv_employee_name.setText(CustomSubordinateMediclaimListAdapter.employee_name);
        }


    }

    private void EditSubordinateMediclaim() {
        if(Url.isSubordinateMediclaim==true)
        {
            LoadEmployeeData();

            //LoadSubordinateData();
            if(CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Submitted"))
            {
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.VISIBLE);
                btn_approve.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);

                ed_mediclaim_amount.setEnabled(false);
                ed_reason.setEnabled(false);
            }

            if(CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Approved") || CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Returned")|| CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Payment done"))
            {

                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                ed_approval_remark.setEnabled(false);
                ed_approved_amount.setEnabled(false);
            }
            if(CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Canceled"))
            {
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                ed_approval_remark.setEnabled(false);
                ed_approved_amount.setEnabled(false);

            }

        }

    }



    private void EditEmployeeMode() {
       // open edit mode
        LoadEmployeeData();
       // loadDocuments();


        if(CustomMediclaimListAdapter.mediclaim_status.contentEquals("Saved"))

        {

            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);

            ed_approved_amount.setEnabled(false);
            ed_approval_remark.setEnabled(false);
            edtv_application_status.setEnabled(false);

        }
        if(CustomMediclaimListAdapter.mediclaim_status.contentEquals("Submitted") ||CustomMediclaimListAdapter.mediclaim_status.contentEquals("Approved"))

        {
            btn_submit.setVisibility(View.GONE);
            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_back.setVisibility(View.VISIBLE);


            ed_mediclaim_amount.setEnabled(false);
            ed_approved_amount.setEnabled(false);
            ed_approval_remark.setEnabled(false);
            ed_reason.setEnabled(false);
            ed_reason.setEnabled(false);

        }

        if(CustomMediclaimListAdapter.mediclaim_status.contentEquals("Returned"))
        {
            btn_save.setVisibility(View.GONE);
            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);
        }

        if(CustomMediclaimListAdapter.mediclaim_status.contentEquals("Canceled"))
        {
            btn_submit.setVisibility(View.GONE);
            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_back.setVisibility(View.VISIBLE);


            ed_mediclaim_amount.setEnabled(false);
            ed_approved_amount.setEnabled(false);
            ed_approval_remark.setEnabled(false);
            ed_reason.setEnabled(false);
            ed_reason.setEnabled(false);



        }
    }

    private void loadDocuments() {
        if(Url.isMyMediclaim==true)
        {
            Url.isNewEntryMediclaim=false;
            String url= Url.BASEURL()+"mediclaim/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomMediclaimListAdapter.mediclaim_id;
            Log.d("my_mediclaim_url=>",url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimEntryActivity.this, "Loading", "Please wait...", true, false);
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(! MediclaimDocumentsActivity.pdf_modelArrayList.isEmpty())
                        {
                            MediclaimDocumentsActivity.pdf_modelArrayList.clear();
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
                            MediclaimDocumentsActivity.pdf_modelArrayList.add(model);

                            MediclaimDocumentsActivity.arraylistSize=   MediclaimDocumentsActivity.pdf_modelArrayList.size();


                        }
                        tv_docs.setText(MediclaimDocumentsActivity.arraylistSize+" Doc(s)");

                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(MediclaimEntryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(MediclaimEntryActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(MediclaimEntryActivity.this).add(stringRequest);
        }

    }

    private void LoadEmployeeData() {

        if(Url.isMyMediclaim==true)
        {
            Url.isSubordinateMediclaim=false;
            String url=Url.BASEURL()+"mediclaim/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomMediclaimListAdapter.mediclaim_id;
            Log.d("my_mediclaim_url=>",url);
            final ProgressDialog loading = ProgressDialog.show(MediclaimEntryActivity.this, "Loading", "Please wait...", true, false);

            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        if(!details_modelArrayList.isEmpty())
                        {
                            details_modelArrayList.clear();
                        }
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject jb1=jsonObject.getJSONObject("fields");

                        String mediclaim_no=jb1.getString("mediclaim_no");
                        String mediclaim_amount=jb1.getString("mediclaim_amount");
                        String description=jb1.getString("description");
                        String mediclaim_status=jb1.getString("mediclaim_status");
                        String supervisor_remark=jb1.getString("supervisor_remark");
                        String approved_mediclaim_amount=jb1.getString("approved_mediclaim_amount");



//                        JSONArray jsonArray=jsonObject.getJSONArray("documents");
//                        for (int i=0;i<jsonArray.length();i++)
//                        {
//                            JSONObject jb2=jsonArray.getJSONObject(i);
//                            // Upload_PDF_Model model=new Upload_PDF_Model("1",getfileName(getApplicationContext(),uripdf),getStringPdf(uripdf),CustomMediclaimListAdapter.mediclaim_id);
//                            Upload_PDF_Model model=new Upload_PDF_Model(jb1.getString("file_base64"),
//                                    jb2.getString("file_name"),
//                                    jb2.getString("file_path"),
//                                    jb2.getString("mediclaim_id"));
//                            MediclaimDocumentsActivity.pdf_modelArrayList.add(model);
//
//                            MediclaimDocumentsActivity.arraylistSize=   MediclaimDocumentsActivity.pdf_modelArrayList.size();
//
//
//                        }
//                        Toast.makeText(MediclaimEntryActivity.this, ""+ MediclaimDocumentsActivity.arraylistSize, Toast.LENGTH_SHORT).show();
//                        tv_docs.setText(MediclaimDocumentsActivity.arraylistSize+" Doc(s)");



                        Mediclaim_Details_Model model=new Mediclaim_Details_Model();
                        model.setMediclaim_no(mediclaim_no);
                        model.setMediclaim_amount(mediclaim_amount);
                        model.setDescription(description);
                        model.setMediclaim_status(mediclaim_status);
                        model.setApproved_mediclaim_amount(approved_mediclaim_amount);
                        model.setSupervisor_remark(supervisor_remark);


                        details_modelArrayList.add(model);

                        edtv_mediclaim_no.setText(mediclaim_no);
                        ed_mediclaim_amount.setText(mediclaim_amount);
                        ed_reason.setText(description);
                        edtv_application_status.setText(mediclaim_status);
                        ed_mediclaim_amount.setText(approved_mediclaim_amount);
                        ed_approval_remark.setText(supervisor_remark);



                        loading.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(MediclaimEntryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(MediclaimEntryActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(MediclaimEntryActivity.this).add(stringRequest);
        }

if(Url.isSubordinateMediclaim==true)
{
    Url.isMyMediclaim=false;
    String url=Url.BASEURL()+"mediclaim/detail/"+userSingletonModel.getCorporate_id()+"/"+CustomSubordinateMediclaimListAdapter.mediclaim_id;
    Log.d("my_mediclaim_url=>",url);
    final ProgressDialog loading = ProgressDialog.show(MediclaimEntryActivity.this, "Loading", "Please wait...", true, false);

    StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                if(!details_modelArrayList.isEmpty())
                {
                    details_modelArrayList.clear();
                }
                JSONObject jsonObject=new JSONObject(response);
                JSONObject jb1=jsonObject.getJSONObject("fields");

                String mediclaim_no=jb1.getString("mediclaim_no");
                String mediclaim_amount=jb1.getString("mediclaim_amount");
                String description=jb1.getString("description");
                String mediclaim_status=jb1.getString("mediclaim_status");
                String supervisor_remark=jb1.getString("supervisor_remark");
                String approved_mediclaim_amount=jb1.getString("approved_mediclaim_amount");



                Mediclaim_Details_Model model=new Mediclaim_Details_Model();
                model.setMediclaim_no(mediclaim_no);
                model.setMediclaim_amount(mediclaim_amount);
                model.setDescription(description);
                model.setMediclaim_status(mediclaim_status);
                model.setApproved_mediclaim_amount(approved_mediclaim_amount);
                model.setSupervisor_remark(supervisor_remark);

                details_modelArrayList.add(model);

                edtv_mediclaim_no.setText(mediclaim_no);
                ed_mediclaim_amount.setText(mediclaim_amount);
                ed_reason.setText(description);
                edtv_application_status.setText(mediclaim_status);
                ed_mediclaim_amount.setText(approved_mediclaim_amount);
                ed_approval_remark.setText(supervisor_remark);


                loading.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                loading.dismiss();
                Toast.makeText(MediclaimEntryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loading.dismiss();
            Toast.makeText(MediclaimEntryActivity.this, "Could't connect to the server", Toast.LENGTH_SHORT).show();
        }
    });

    Volley.newRequestQueue(MediclaimEntryActivity.this).add(stringRequest);
}

        }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                    String corp_id=userSingletonModel.getCorporate_id();
                    String mediclaim_id=CustomMediclaimListAdapter.mediclaim_id;
                    String mediclaim_no=CustomMediclaimListAdapter.mediclaim_no;
                     int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                    double mediclaim_amount=Double.parseDouble(ed_mediclaim_amount.getText().toString());
                    double approved_mediclaim_amount=0;
                    String description=ed_reason.getText().toString();
                    String supervisor_remark="";
                    String mediclaim_status="Saved";
                    int approved_by_id=0;
                    String approved_by_name="";
                    String payment_remark="";
                    int payment_amount=0;
                SaveMyMediclaimData(corp_id,mediclaim_id,mediclaim_no,employee_id,mediclaim_amount,approved_mediclaim_amount,description,supervisor_remark,
                        mediclaim_status,approved_by_id,approved_by_name,payment_remark,payment_amount);

                break;
            case  R.id.btn_cancel:

                break;
            case  R.id.btn_submit:

                corp_id=userSingletonModel.getCorporate_id();
                mediclaim_id=CustomMediclaimListAdapter.mediclaim_id;
                 mediclaim_no=CustomMediclaimListAdapter.mediclaim_no;
                 employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                 mediclaim_amount=Double.parseDouble(ed_mediclaim_amount.getText().toString());
                 approved_mediclaim_amount=0;
                description=ed_reason.getText().toString();
                supervisor_remark="";
                 mediclaim_status="Submitted";
                 approved_by_id=0;
                approved_by_name="";
                 payment_remark="";
                payment_amount=0;
                SaveMyMediclaimData(corp_id,mediclaim_id,mediclaim_no,employee_id,mediclaim_amount,approved_mediclaim_amount,description,supervisor_remark,
                        mediclaim_status,approved_by_id,approved_by_name,payment_remark,payment_amount);




                break;
            case  R.id.btn_approve:

                if(Url.isSubordinateMediclaim==true)
                {
                    corp_id=userSingletonModel.getCorporate_id();
                    mediclaim_id=CustomSubordinateMediclaimListAdapter.mediclaim_id;
                    mediclaim_no=CustomSubordinateMediclaimListAdapter.mediclaim_no;
                    employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                    mediclaim_amount=Double.parseDouble(ed_mediclaim_amount.getText().toString());
                    approved_mediclaim_amount=Double.parseDouble(ed_mediclaim_amount.getText().toString());
                    description=ed_reason.getText().toString();
                    supervisor_remark=ed_approval_remark.getText().toString();
                    mediclaim_status="Approved";
                    approved_by_id=0;
                    approved_by_name=userSingletonModel.getFull_employee_name();
                    payment_remark="";
                    payment_amount=0;
                    SaveMyMediclaimData(corp_id,mediclaim_id,mediclaim_no,employee_id,mediclaim_amount,approved_mediclaim_amount,description,supervisor_remark,
                            mediclaim_status,approved_by_id,approved_by_name,payment_remark,payment_amount);
                }



                break;
            case  R.id.btn_return:

                break;
            case  R.id.btn_back:

                break;
            default:
                break;
        }
    }



    private void SaveMyMediclaimData(String corp_id, String mediclaim_id, String mediclaim_no, int employee_id, double mediclaim_amount, double approved_mediclaim_amount, String description, String supervisor_remark, String mediclaim_status, int approved_by_id, String approved_by_name, String payment_remark, int payment_amount) {

        try {

            my_mediclaim_jsonBody.put("corp_id",corp_id);
            if(Url.isNewEntryMediclaim==true)
            {
                my_mediclaim_jsonBody.put("mediclaim_id",0);
                my_mediclaim_jsonBody.put("mediclaim_no","");
            }
            else
            {
                my_mediclaim_jsonBody.put("mediclaim_id",mediclaim_id);
                my_mediclaim_jsonBody.put("mediclaim_no",mediclaim_no);
            }

            my_mediclaim_jsonBody.put("employee_id",employee_id);
            my_mediclaim_jsonBody.put("mediclaim_amount",mediclaim_amount);
            my_mediclaim_jsonBody.put("approved_mediclaim_amount",approved_mediclaim_amount);
            my_mediclaim_jsonBody.put("description",description);
            my_mediclaim_jsonBody.put("supervisor_remark",supervisor_remark);
            my_mediclaim_jsonBody.put("mediclaim_status",mediclaim_status);
            my_mediclaim_jsonBody.put("approved_by_id",approved_by_id);
            my_mediclaim_jsonBody.put("approved_by_name",approved_by_name);
            my_mediclaim_jsonBody.put("payment_remark",payment_remark);
            my_mediclaim_jsonBody.put("payment_amount",payment_amount);


            for (int i=0;i<MediclaimDocumentsActivity.pdf_modelArrayList.size();i++)
            {
                String name=MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_name();
                String base=MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_base64();

                arrayList.add(new Model_Base64(name,base));

            }
            my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));


//
//            my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));
//         Log.d("GGFHHHHHHVF==", new Gson().toJson(arrayList));

        }
        catch (Exception e)
        {
            e.getMessage();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Log.d("mediclaim=>",my_mediclaim_jsonBody.toString());
        String url = Url.BASEURL() + "mediclaim/save";

        try {
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(my_mediclaim_jsonBody.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String status=response.getString("status");
                        String message=response.getString("message");
                        Log.d("message=>",message);
                        Toast.makeText(MediclaimEntryActivity.this, message, Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(MediclaimEntryActivity.this, MediclaimActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                                //finish(); // commented by sr
                    //    Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();

//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MediclaimEntryActivity.this, "could't connect to the server", Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(MediclaimEntryActivity.this).add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            e.getMessage();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

  }

}