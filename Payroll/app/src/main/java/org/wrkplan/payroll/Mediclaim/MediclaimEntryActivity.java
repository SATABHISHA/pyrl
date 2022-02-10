package org.wrkplan.payroll.Mediclaim;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import org.wrkplan.payroll.Model.Mediclaim.Mediclaim_Details_Model;
import org.wrkplan.payroll.Model.Mediclaim.Model_Base64;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Mediclaim_Details_Model;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Model_Base64;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Upload_PDF_Model;
import org.wrkplan.payroll.Model.Mediclaim.Upload_PDF_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class MediclaimEntryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_check_balance, tv_mediclaim_no, edtv_mediclaim_no, tv_employee_name, edtv_employee_name, tv_mediclaim_amount,
            tv_reason, tv_documents, tv_docs, tv_views, tv_approved_amount, tv_approval_remark, tv_application_status,
            edtv_application_status, tv_mediclaim_title;
    EditText ed_mediclaim_amount, ed_reason, ed_approved_amount, ed_approval_remark;

    Button btn_cancel, btn_return, btn_approve, btn_submit, btn_save, btn_back;
    //employee mediclaim details model
    ArrayList<Mediclaim_Details_Model> details_modelArrayList = new ArrayList<>();
    //subordinate mediclaim details model
    ArrayList<Subordinate_Mediclaim_Details_Model> subordinate_details_arrayList = new ArrayList<>();


    //---------------------- popup text views and button----------------
    TextView tv_period, txt_period, tv_unutilized_amount, txt_unutilized_amount, tv_1years, tv_limit_year,
            txt_limit_year, tv_2years, tv_amount_disbursed, txt_amount_disbursed, tv_balance_available, txt_balance_available;
    Button btn_ok;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

    JSONObject my_mediclaim_jsonBody = new JSONObject();
    ArrayList<Model_Base64> arrayList = new ArrayList<>();
    ArrayList<Subordinate_Model_Base64> arrayList1 = new ArrayList<>();
    String mediclaim_status, sub_mediclaim_status;
    int document_count=0;

    //----------------------------X-------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediclaim_entry_activity);

        // -------initialize text views start--------------//
        tv_check_balance = findViewById(R.id.tv_check_balance);
        tv_mediclaim_no = findViewById(R.id.tv_mediclaim_no);
        edtv_mediclaim_no = findViewById(R.id.edtv_mediclaim_no);
        tv_employee_name = findViewById(R.id.tv_employee_name);
        edtv_employee_name = findViewById(R.id.edtv_employee_name);
        tv_mediclaim_amount = findViewById(R.id.tv_mediclaim_amount);
        tv_reason = findViewById(R.id.tv_reason);
        tv_mediclaim_title = findViewById(R.id.tv_mediclaim_title);
        tv_documents = findViewById(R.id.tv_documents);
        tv_docs = findViewById(R.id.tv_docs);
        tv_views = findViewById(R.id.tv_views);
        tv_approved_amount = findViewById(R.id.tv_approved_amount);
        tv_approval_remark = findViewById(R.id.tv_approval_remark);
        tv_application_status = findViewById(R.id.tv_application_status);
        edtv_application_status = findViewById(R.id.edtv_application_status);
        // -------initialize text views end--------------//
        //-------------------------------------------------------------------------------//
        // -------initialize edit text start--------------//
        ed_mediclaim_amount = findViewById(R.id.ed_mediclaim_amount);
        ed_reason = findViewById(R.id.ed_reason);
        ed_approved_amount = findViewById(R.id.ed_approved_amount);
        ed_approval_remark = findViewById(R.id.ed_approval_remark);
        // -------initialize edit text start--------------//

        //----------------------------------------------------------------------------------//


        // -------initialize Button end--------------//

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_return = findViewById(R.id.btn_return);
        btn_approve = findViewById(R.id.btn_approve);
        btn_submit = findViewById(R.id.btn_submit);
        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);
        // -------initialize Button end--------------//


        btn_cancel.setOnClickListener(this);
        btn_approve.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        // Clear the ArrayList Here
        MediclaimDocumentsActivity.pdf_modelArrayList.clear();
        MediclaimDocumentsActivity.subordinate_arraylist.clear();
        // Toast.makeText(this, "Size: "+ MediclaimDocumentsActivity.pdf_modelArrayList.size(), Toast.LENGTH_SHORT).show();

        //------------------------Open Check Balance POPUP-----------------------------------------//
        tv_check_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MediclaimEntryActivity.this);
                builder.setTitle("");
                final View custom = getLayoutInflater().inflate(R.layout.popup_account_balance_activity, null);


                tv_period = custom.findViewById(R.id.tv_period);
                txt_period = custom.findViewById(R.id.txt_period);
                tv_unutilized_amount = custom.findViewById(R.id.tv_unutilized_amount);
                txt_unutilized_amount = custom.findViewById(R.id.txt_unutilized_amount);
                tv_1years = custom.findViewById(R.id.tv_1years);
                tv_limit_year = custom.findViewById(R.id.tv_limit_year);
                txt_limit_year = custom.findViewById(R.id.txt_limit_year);
                tv_2years = custom.findViewById(R.id.tv_2years);
                tv_amount_disbursed = custom.findViewById(R.id.tv_amount_disbursed);
                txt_amount_disbursed = custom.findViewById(R.id.txt_amount_disbursed);
                tv_balance_available = custom.findViewById(R.id.tv_balance_available);
                txt_balance_available = custom.findViewById(R.id.txt_balance_available);
                btn_ok = custom.findViewById(R.id.btn_ok);


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
        if (Url.isMyMediclaim == true) {
            tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");

        }
        if (Url.isNewEntryMediclaim == true) {

            tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");


        }

        if (Url.isSubordinateMediclaim == true) {
            tv_docs.setText(MediclaimDocumentsActivity.arraylistSize + " Doc(s)");
        }





        tv_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Url.isNewEntryMediclaim == true) {
                    Url.isMyMediclaim = false;
                    Url.isSubordinateMediclaim = false;
                    Intent intent = new Intent(MediclaimEntryActivity.this, MediclaimDocumentsActivity.class);

                    startActivity(intent);
                }
                if (Url.isMyMediclaim == true) {
                    Url.isNewEntryMediclaim = false;
                    Url.isSubordinateMediclaim = false;
                    Intent intent = new Intent(MediclaimEntryActivity.this, MediclaimDocumentsActivity.class);
                    intent.putExtra("employee_status=>", mediclaim_status);
                    startActivity(intent);
                }
                if (Url.isSubordinateMediclaim == true) {
                    Url.isNewEntryMediclaim = false;
                    Url.isMyMediclaim = false;
                    Intent intent = new Intent(MediclaimEntryActivity.this, MediclaimDocumentsActivity.class);
                    intent.putExtra("subordinate_mediclaim_status=>", sub_mediclaim_status);
                    startActivity(intent);
                }


            }
        });

        if (Url.isNewEntryMediclaim == true) {
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


        if (Url.isNewEntryMediclaim == false) {
            EditEmployeeMode();
            tv_mediclaim_title.setText("My Mediclaim Detail");
            edtv_employee_name.setText(userSingletonModel.getFull_employee_name());
        }

        if (Url.isNewEntryMediclaim == false && Url.isMyMediclaim == false) {
            EditSubordinateMediclaim();
            tv_mediclaim_title.setText("Subordinate Mediclaim Detail");
            edtv_employee_name.setText(CustomSubordinateMediclaimListAdapter.employee_name);
        }


    }

    private void EditSubordinateMediclaim() {
        if (Url.isSubordinateMediclaim == true) {

            LoadSubordinateData();


            if (CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Submitted")) {
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.VISIBLE);
                btn_approve.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);

                ed_mediclaim_amount.setEnabled(false);
                ed_reason.setEnabled(false);
            }

            if (CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Approved") || CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Returned") || CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Payment done")) {

                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                ed_approval_remark.setEnabled(false);
                ed_approved_amount.setEnabled(false);
            }
            if (CustomSubordinateMediclaimListAdapter.mediclaim_status.contentEquals("Canceled")) {
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

    private void LoadSubordinateData() {
        String url = Url.BASEURL() + "mediclaim/detail/" + userSingletonModel.getCorporate_id() + "/" + CustomSubordinateMediclaimListAdapter.mediclaim_id;
        Log.d("my_sub_mediclaim_url=>", url);
        final ProgressDialog loading = ProgressDialog.show(MediclaimEntryActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (!subordinate_details_arrayList.isEmpty()) {
                        subordinate_details_arrayList.clear();
                        // MediclaimDocumentsActivity.subordinate_arraylist.clear();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jb1 = jsonObject.getJSONObject("fields");
                    String mediclaim_no = jb1.getString("mediclaim_no");
                    String mediclaim_amount = jb1.getString("mediclaim_amount");
                    String description = jb1.getString("description");
                    sub_mediclaim_status = jb1.getString("mediclaim_status");
                    String supervisor_remark = jb1.getString("supervisor_remark");
                    String approved_mediclaim_amount = jb1.getString("approved_mediclaim_amount");


                    //

                    JSONArray jsonArray = jsonObject.getJSONArray("documents");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Subordinate_Upload_PDF_Model subordinate_upload_pdf_model = new Subordinate_Upload_PDF_Model();

                        subordinate_upload_pdf_model.setFile_path(object.getString("file_base64"));
                        subordinate_upload_pdf_model.setFile_path(object.getString("file_name"));
                        subordinate_upload_pdf_model.setFile_path(object.getString("file_path"));
                        subordinate_upload_pdf_model.setFile_path(object.getString("mediclaim_id"));

                        MediclaimDocumentsActivity.subordinate_arraylist.add(subordinate_upload_pdf_model);
                    }




                    //


                    Subordinate_Mediclaim_Details_Model model = new Subordinate_Mediclaim_Details_Model();
                    model.setMediclaim_no(mediclaim_no);
                    model.setMediclaim_amount(mediclaim_amount);
                    model.setDescription(description);
                    model.setMediclaim_status(sub_mediclaim_status);
                    model.setSupervisor_remark(supervisor_remark);
                    model.setMediclaim_amount(approved_mediclaim_amount);

                    edtv_mediclaim_no.setText(mediclaim_no);
                    ed_mediclaim_amount.setText(mediclaim_amount);
                    ed_reason.setText(description);
                    edtv_application_status.setText(sub_mediclaim_status);
                    ed_approval_remark.setText(supervisor_remark);
                    ed_approved_amount.setText(approved_mediclaim_amount);



                    if (Url.isSubordinateMediclaim == true) {
                        tv_docs.setText(MediclaimDocumentsActivity.subordinate_arraylist.size() + " Doc(s)");



                    }



                    loading.dismiss();

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Toast.makeText(MediclaimEntryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MediclaimEntryActivity.this, "could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(MediclaimEntryActivity.this).add(stringRequest);

    }


    private void EditEmployeeMode() {
        // open edit mode

        if (Url.isMyMediclaim == true) {
            LoadEmployeeData();


            if (CustomMediclaimListAdapter.mediclaim_status.contentEquals("Saved")) {

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
            if (CustomMediclaimListAdapter.mediclaim_status.contentEquals("Submitted") || CustomMediclaimListAdapter.mediclaim_status.contentEquals("Approved")) {
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

            if (CustomMediclaimListAdapter.mediclaim_status.contentEquals("Returned")) {
                btn_save.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);
                ed_approved_amount.setEnabled(false);
                ed_approval_remark.setEnabled(false);
            }

            if (CustomMediclaimListAdapter.mediclaim_status.contentEquals("Canceled")) {
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

    }


    private void LoadEmployeeData() {


        String url = Url.BASEURL() + "mediclaim/detail/" + userSingletonModel.getCorporate_id() + "/" + CustomMediclaimListAdapter.mediclaim_id;
        Log.d("my_mediclaim_url=>", url);
        final ProgressDialog loading = ProgressDialog.show(MediclaimEntryActivity.this, "Loading", "Please wait...", true, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (!details_modelArrayList.isEmpty()) {
                        details_modelArrayList.clear();
                    }
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jb1 = jsonObject.getJSONObject("fields");
                    String mediclaim_no = jb1.getString("mediclaim_no");
                    String mediclaim_amount = jb1.getString("mediclaim_amount");
                    String description = jb1.getString("description");
                    mediclaim_status = jb1.getString("mediclaim_status");
                    String supervisor_remark = jb1.getString("supervisor_remark");
                    String approved_mediclaim_amount = jb1.getString("approved_mediclaim_amount");

                    //

                    JSONArray jsonArray = jsonObject.getJSONArray("documents");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Upload_PDF_Model upload_pdf_model = new Upload_PDF_Model();

                        upload_pdf_model.setFile_base64(object.getString("file_base64"));
                        upload_pdf_model.setFile_name(object.getString("file_name"));
                        upload_pdf_model.setFile_path(object.getString("file_path"));
                        upload_pdf_model.setMediclaim_id(object.getString("mediclaim_id"));

                        MediclaimDocumentsActivity.pdf_modelArrayList.add(upload_pdf_model);
                    }


                    //


                    Mediclaim_Details_Model model = new Mediclaim_Details_Model();

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
                    ed_approved_amount.setText(approved_mediclaim_amount);
                    ed_approval_remark.setText(supervisor_remark);

                    //
                    if (Url.isMyMediclaim == true) {
                        tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");

                        document_count=MediclaimDocumentsActivity.pdf_modelArrayList.size();


                    }
                    if (Url.isNewEntryMediclaim == true) {

                        tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");

                    }



                    loading.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(MediclaimEntryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:

                if(ed_mediclaim_amount.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_reason.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else
                {
                    String title="Save";
                    String message=" Do you really want to save?";

                    AlertDialog.Builder save=new AlertDialog.Builder(this);
                    save.setMessage(message);
                    save.setCancelable(true);
                    save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String corp_id = userSingletonModel.getCorporate_id();
                            String mediclaim_id = CustomMediclaimListAdapter.mediclaim_id;
                            String mediclaim_no = CustomMediclaimListAdapter.mediclaim_no;
                            int employee_id = Integer.parseInt(userSingletonModel.getEmployee_id());
                            double mediclaim_amount = Double.parseDouble(ed_mediclaim_amount.getText().toString());
                            double approved_mediclaim_amount = 0;
                            String description = ed_reason.getText().toString();
                            String supervisor_remark = "";
                            String mediclaim_status = "Saved";
                            int approved_by_id = 0;
                            String approved_by_name = "";
                            String payment_remark = "";
                            int payment_amount = 0;
                            SaveMyMediclaimData(corp_id, mediclaim_id, mediclaim_no, employee_id, mediclaim_amount, approved_mediclaim_amount, description, supervisor_remark,
                                    mediclaim_status, approved_by_id, approved_by_name, payment_remark, payment_amount);

                            //Toast.makeText(context, "Delet item successfully of Position => "+position, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    save.setTitle(title);
                    save.show();


                }




                break;
            case R.id.btn_cancel:
                if (Url.isSubordinateMediclaim == true) {

//                    if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_mediclaim_amount.getText().toString())))
//                    {
//                        //Toast.makeText(this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
//                        AlertBox1();
//                    }
                    if (ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if (ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else {
                        String title="Cancel";
                        String message=" Do you really want to cancel this claim?";

                        AlertDialog.Builder approved=new AlertDialog.Builder(this);
                        approved.setMessage(message);
                        approved.setCancelable(true);
                        approved.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String corp_id = userSingletonModel.getCorporate_id();
                                String mediclaim_id = CustomSubordinateMediclaimListAdapter.mediclaim_id;
                                String mediclaim_no = CustomSubordinateMediclaimListAdapter.mediclaim_no;
                                int employee_id = Integer.parseInt(userSingletonModel.getEmployee_id());
                                double mediclaim_amount = Double.parseDouble(ed_mediclaim_amount.getText().toString());
                                double approved_mediclaim_amount = 0;
                                String description = ed_reason.getText().toString();
                                String supervisor_remark = ed_approval_remark.getText().toString();
                                mediclaim_status = "Canceled";
                                int approved_by_id = 0;
                                String approved_by_name = userSingletonModel.getFull_employee_name();
                                String payment_remark = "";
                                int payment_amount = 0;
                                SaveMyMediclaimData(corp_id, mediclaim_id, mediclaim_no, employee_id, mediclaim_amount, approved_mediclaim_amount, description, supervisor_remark,
                                        mediclaim_status, approved_by_id, approved_by_name, payment_remark, payment_amount);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        approved.setTitle(title);
                        approved.show();

                    }


                }

                break;
            case R.id.btn_submit:

                if(ed_mediclaim_amount.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_reason.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else
                {
                    String title="Submit";
                    String message=" Do you really want to submit now?";

                    AlertDialog.Builder save=new AlertDialog.Builder(this);
                    save.setMessage(message);
                    save.setCancelable(true);
                    save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String corp_id = userSingletonModel.getCorporate_id();
                            String mediclaim_id = CustomMediclaimListAdapter.mediclaim_id;
                            String mediclaim_no = CustomMediclaimListAdapter.mediclaim_no;
                            int employee_id = Integer.parseInt(userSingletonModel.getEmployee_id());
                            double mediclaim_amount = Double.parseDouble(ed_mediclaim_amount.getText().toString());
                            double approved_mediclaim_amount = 0;
                            String description = ed_reason.getText().toString();
                            String supervisor_remark = "";
                            String mediclaim_status = "Submitted";
                            int approved_by_id = 0;
                            String approved_by_name = "";
                            String payment_remark = "";
                            int payment_amount = 0;
                            SaveMyMediclaimData(corp_id, mediclaim_id, mediclaim_no, employee_id, mediclaim_amount, approved_mediclaim_amount, description, supervisor_remark,
                                    mediclaim_status, approved_by_id, approved_by_name, payment_remark, payment_amount);

                            //Toast.makeText(context, "Delet item successfully of Position => "+position, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    save.setTitle(title);
                    save.show();


                }




                break;
            case R.id.btn_approve:

                if (Url.isSubordinateMediclaim == true) {
                    if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_mediclaim_amount.getText().toString())))
                    {
                        // Toast.makeText(this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
                        AlertBox1();
                    }
                    else if (ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if (ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else {

                        String title="Approve";
                        String message=" Do you want to approve this claim?";

                        AlertDialog.Builder approved=new AlertDialog.Builder(this);
                        approved.setMessage(message);
                        approved.setCancelable(true);
                        approved.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String corp_id = userSingletonModel.getCorporate_id();
                                String mediclaim_id = CustomSubordinateMediclaimListAdapter.mediclaim_id;
                                String mediclaim_no = CustomSubordinateMediclaimListAdapter.mediclaim_no;
                                int employee_id = Integer.parseInt(userSingletonModel.getEmployee_id());
                                double mediclaim_amount = Double.parseDouble(ed_mediclaim_amount.getText().toString());
                                double approved_mediclaim_amount = Double.parseDouble(ed_approved_amount.getText().toString());
                                String description = ed_reason.getText().toString();
                                String supervisor_remark = ed_approval_remark.getText().toString();
                                mediclaim_status = "Approved";
                                int approved_by_id = 0;
                                String approved_by_name = userSingletonModel.getFull_employee_name();
                                String payment_remark = "";
                                int payment_amount = 0;
                                SaveMyMediclaimData(corp_id, mediclaim_id, mediclaim_no, employee_id, mediclaim_amount, approved_mediclaim_amount, description, supervisor_remark,
                                        mediclaim_status, approved_by_id, approved_by_name, payment_remark, payment_amount);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        approved.setTitle(title);
                        approved.show();

                    }

                }


                break;
            case R.id.btn_return:
                if (Url.isSubordinateMediclaim == true) {

//                    if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_mediclaim_amount.getText().toString())))
//                    {
//                        //Toast.makeText(this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
//                        AlertBox1();
//                    }
                    if (ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if (ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else
                    {

                        String title="Return";
                        String message=" Do you really want to return this claim?";

                        AlertDialog.Builder approved=new AlertDialog.Builder(this);
                        approved.setMessage(message);
                        approved.setCancelable(true);
                        approved.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String corp_id = userSingletonModel.getCorporate_id();
                                String mediclaim_id = CustomSubordinateMediclaimListAdapter.mediclaim_id;
                                String mediclaim_no = CustomSubordinateMediclaimListAdapter.mediclaim_no;
                                int employee_id = Integer.parseInt(userSingletonModel.getEmployee_id());
                                double mediclaim_amount = Double.parseDouble(ed_mediclaim_amount.getText().toString());
                                double approved_mediclaim_amount = 0;
                                String description = ed_reason.getText().toString();
                                String supervisor_remark = ed_approval_remark.getText().toString();
                                mediclaim_status = "Returned";
                                int approved_by_id = 0;
                                String approved_by_name = userSingletonModel.getFull_employee_name();
                                String payment_remark = "";
                                int payment_amount = 0;
                                SaveMyMediclaimData(corp_id, mediclaim_id, mediclaim_no, employee_id, mediclaim_amount, approved_mediclaim_amount, description, supervisor_remark,
                                        mediclaim_status, approved_by_id, approved_by_name, payment_remark, payment_amount);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        approved.setTitle(title);
                        approved.show();

                    }



                }

                break;
            case R.id.btn_back:

                /*if (Url.isSubordinateMediclaim == true) {
                    onBackPressed();
                }
                if (Url.isMyMediclaim == true) {
                    onBackPressed();
                }
                if (Url.isNewEntryMediclaim == true) {
                    onBackPressed();
                }*/ //commented on 6-Aug-2021

                //---added on 06-Aug-2021, code starts--
                if (Url.isSubordinateMediclaim == true) {

                    if(sub_mediclaim_status.contentEquals("Submitted"))
                    {
                        AlertBack();
                    }
                    else
                    {
                        onBackPressed();
                    }


                }
                if (Url.isMyMediclaim == true) {

                    if(mediclaim_status.contentEquals("Saved") || mediclaim_status.contentEquals("Returned"))
                    {
                        AlertBack();
                    }
                    else
                    {
                        onBackPressed();

                    }

                }
                if (Url.isNewEntryMediclaim == true) {

                    AlertBack();
                    //onBackPressed();
                }
                //---added on 06-Aug-2021, code ends--
                break;
            default:
                break;
        }
    }

    //-----function for Back button logic for Alert, code starts (added on 6-Aug-2021)---
    private void AlertBack() {

        String message="You may lost any unsaved data. Do you really want to go back?";

        AlertDialog.Builder builder=new AlertDialog.Builder(MediclaimEntryActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    //-----function for Back button logic for Alert, code ends (added on 6-Aug-2021)---

    private void AlertBox1() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Approved amount cannot be more than claim amount");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                //Toast.makeText(context, "Delet item successfully of Position => "+position, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void AlertBox() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Fields cannot be left blank");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                //Toast.makeText(context, "Delet item successfully of Position => "+position, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    private void SaveMyMediclaimData(String corp_id, String mediclaim_id, String mediclaim_no, int employee_id, double mediclaim_amount, double approved_mediclaim_amount, String description, String supervisor_remark, String mediclaim_status, int approved_by_id, String approved_by_name, String payment_remark, int payment_amount) {

        try {

            my_mediclaim_jsonBody.put("corp_id", corp_id);
            if (Url.isNewEntryMediclaim == true) {
                my_mediclaim_jsonBody.put("mediclaim_id", 0);
                my_mediclaim_jsonBody.put("mediclaim_no", "");
            } else {
                my_mediclaim_jsonBody.put("mediclaim_id", mediclaim_id);
                my_mediclaim_jsonBody.put("mediclaim_no", mediclaim_no);
            }

            my_mediclaim_jsonBody.put("employee_id", employee_id);
            my_mediclaim_jsonBody.put("mediclaim_amount", mediclaim_amount);
            my_mediclaim_jsonBody.put("approved_mediclaim_amount", approved_mediclaim_amount);
            my_mediclaim_jsonBody.put("description", description);
            my_mediclaim_jsonBody.put("supervisor_remark", supervisor_remark);
            my_mediclaim_jsonBody.put("mediclaim_status", mediclaim_status);
            my_mediclaim_jsonBody.put("approved_by_id", approved_by_id);
            my_mediclaim_jsonBody.put("approved_by_name", approved_by_name);
            my_mediclaim_jsonBody.put("payment_remark", payment_remark);
            my_mediclaim_jsonBody.put("payment_amount", payment_amount);

//                for (int i=0;i<MediclaimDocumentsActivity.pdf_modelArrayList.size();i++)
//                {
//                    String name=MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_name();
//                    String base=MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_base64();
//
//                    arrayList.add(new Model_Base64(name,base));
//
//                }
//                my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));


            if (Url.isSubordinateMediclaim == true) {
                for (int i = 0; i < MediclaimDocumentsActivity.subordinate_arraylist.size(); i++) {
                    String name = MediclaimDocumentsActivity.subordinate_arraylist.get(i).getFile_name();
                    String base = MediclaimDocumentsActivity.subordinate_arraylist.get(i).getFile_base64();
                    //  arrayList1.clear();
                    arrayList1.add(new Subordinate_Model_Base64(name, base));
                    Log.d("arrayList", String.valueOf(arrayList1.size()));
                }
                my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList1)));
                my_mediclaim_jsonBody.put("deleted_documents",new JSONArray(new Gson().toJson(CustomUploadPDFlistAdapter.deleteModelArrayList)));
            } else if(Url.isMyMediclaim==true) {
                for (int i = 0; i < MediclaimDocumentsActivity.pdf_modelArrayList.size(); i++) {
                    String name = MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_name();
                    String base = MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_base64();
                    //arrayList.clear();
                    if(name.contains("/"))
                    {
                        name=name.replace("/","_");
                    }
                    else if(name.contains("+"))
                    {
                        name=name.replace("+","_");
                    }
                    else if(name.contains(" "))
                    {
                        name=name.replace(" ","_");
                    }
                    arrayList.add(new Model_Base64(name, base));

                }

                my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));
                my_mediclaim_jsonBody.put("deleted_documents",new JSONArray(new Gson().toJson(CustomUploadPDFlistAdapter.deleteModelArrayList)));

            }
            else if(Url.isNewEntryMediclaim==true)
            {
                for (int i = 0; i < MediclaimDocumentsActivity.pdf_modelArrayList.size(); i++) {
                    String name = MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_name();
                    String base = MediclaimDocumentsActivity.pdf_modelArrayList.get(i).getFile_base64();
                    // arrayList.clear();

                    if(name.contains("/"))
                    {
                        name=name.replace("/","_");
                    }
                    else if(name.contains("+"))
                    {
                        name=name.replace("+","_");
                    }
                    else if(name.contains(" "))
                    {
                        name=name.replace(" ","_");
                    }
                    arrayList.add(new Model_Base64(name, base));

                }

                my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));
                my_mediclaim_jsonBody.put("deleted_documents",new JSONArray(new Gson().toJson(CustomUploadPDFlistAdapter.deleteModelArrayList)));
                // my_mediclaim_jsonBody.put("deleted_documents",new JSONArray());

            }


//
//            my_mediclaim_jsonBody.put("documents", new JSONArray(new Gson().toJson(arrayList)));
            Log.d("GGFHHHHHHVF==", new Gson().toJson(arrayList));

        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Log.d("mediclaim=>", my_mediclaim_jsonBody.toString());
        String url = Url.BASEURL() + "mediclaim/save";
        Log.d("URL", url);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(my_mediclaim_jsonBody.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("getData-=>",response.toString());
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.d("message=>", message);
                        Toast.makeText(MediclaimEntryActivity.this, message, Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(MediclaimEntryActivity.this, MediclaimActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Url.isMyMediclaim == true) {
            tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");
        }
        if (Url.isNewEntryMediclaim == true) {
            tv_docs.setText(MediclaimDocumentsActivity.pdf_modelArrayList.size() + " Doc(s)");
        }
        if (Url.isSubordinateMediclaim == true) {
            tv_docs.setText(MediclaimDocumentsActivity.subordinate_arraylist.size() + " Doc(s)");
        }
    }
}
