package org.wrkplan.payroll.AdvanceRequisition;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Requisition_Reason_Spinner_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvanceRequisitionEntryActivity extends AppCompatActivity {

    TextView tv_requisitionnum,tv_requisition_amount,tv_ctc,tv_reason,tv_narration,
            tv_return_period,tv_approved_amount,tv_approval_remark,tv_application_status
            ,edtv_application_status,edtv_requisition_no,tv_header,tv_employee_name,edtv_employee_name;
    EditText ed_requisition_amount,ed_ctc,ed_narration,ed_approved_amount,
            ed_approval_remark,ed_return_period;
    Spinner spinner_reason_advance;
    JSONObject my_requisition_jsonBody=new JSONObject();
    LinearLayout ll,ll_1,ll_button;

    Button btn_cancel,btn_return,btn_approve,btn_submit,btn_save,btn_back;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

    private ArrayList<String> reasonspinnerlist = new ArrayList<String>();
    ArrayList<Requisition_Reason_Spinner_Model> spinner_modelArrayList=new ArrayList<>();

    // Spinner item
    int item;
    //current date for requisition date
    String currntdate;
    public static String status_message = "";

    String ctc="";
    private String reason_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advance_requisititon_entry_activity);


        //----initialize text view start------//

        tv_application_status=findViewById(R.id.tv_application_status);
        tv_requisitionnum=findViewById(R.id.tv_requisitionnum);
        tv_requisition_amount=findViewById(R.id.tv_requisition_amount);
        tv_ctc=findViewById(R.id.tv_ctc);
        tv_reason=findViewById(R.id.tv_reason);
        tv_narration=findViewById(R.id.tv_narration);
        tv_return_period=findViewById(R.id.tv_return_period);
        tv_approved_amount=findViewById(R.id.tv_approved_amount);
        tv_approval_remark=findViewById(R.id.tv_approval_remark);
        edtv_requisition_no=findViewById(R.id.edtv_requisition_no);
        tv_header=findViewById(R.id.tv_header);
        tv_employee_name=findViewById(R.id.tv_employee_name);
        edtv_employee_name=findViewById(R.id.edtv_employee_name);

        //----initialize text view end------//
        //*********************************************************************************//
        //----initialize edit text view start------//

        ed_requisition_amount=findViewById(R.id.ed_requisition_amount);
        ed_ctc=findViewById(R.id.ed_ctc);
        edtv_application_status=findViewById(R.id.edtv_application_status);
        ed_narration=findViewById(R.id.ed_narration);
        ed_approved_amount=findViewById(R.id.ed_approved_amount);
        ed_approval_remark=findViewById(R.id.ed_approval_remark);
        ed_return_period=findViewById(R.id.ed_return_period);

        //----initialize edit text view end------//
        // *********************************************************************************//

        //----initialize Linearlayout and Spinner view start------//

        ll=findViewById(R.id.ll);
        ll_1=findViewById(R.id.ll_1);
        spinner_reason_advance=findViewById(R.id.spinner_reason_advance);
        ll_button=findViewById(R.id.ll_button);


        //----initialize Linearlayout and Spinner view end------//
        // *********************************************************************************//
        //----initialize button view start------//2

        btn_approve=findViewById(R.id.btn_approve);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_return=findViewById(R.id.btn_return);
        btn_save=findViewById(R.id.btn_save);
        btn_submit=findViewById(R.id.btn_submit);
        btn_back=findViewById(R.id.btn_back);

        //----initialize button view end------//


        //-------spinner code start---------//



//            List<String> reason_spinner=new ArrayList<>();
//            reason_spinner.add("i) Serious and / prolonged illness in the family ('Family' means self, wife and dependent children) of the employee");
//            reason_spinner.add("ii) His / her own marriage");
//            reason_spinner.add("iii) Son’s / Daughter’s or real Sister’s marriage");
//            reason_spinner.add("iv) Rehabilitation due to natural calamity, such as flood, fire, accident etc");
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdvanceRequisitionEntryActivity.this, android.R.layout.simple_spinner_item, reason_spinner);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner_reason_advance.setAdapter(adapter);
//
//
//
//            spinner_reason_advance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    item=position+1;
//
//
//                    //Toast.makeText(AdvanceRequisitionEntryActivity.this, ""+item, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


        loadSpinner();



        //Toast.makeText(this, ""+currntdate, Toast.LENGTH_SHORT).show();

        //-------spinner code end---------//

        SimpleDateFormat currentDte=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        currntdate=(currentDte.format(date));

        if(Url.isNewEntry==true)
        {
            tv_header.setText("My Advance Requisition Detail");
            ed_approval_remark.setEnabled(false);
            ed_approved_amount.setEnabled(false);
            edtv_application_status.setEnabled(false);
            edtv_employee_name.setText(userSingletonModel.getFull_employee_name());

            ed_ctc.setText(userSingletonModel.getCtc());
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    onBackPressed();
                    AlertBack(); //added on 6-Aug-2021
                }
            });


            btn_approve.setVisibility(View.GONE);
            btn_return.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);




        }



        // save functionality

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ed_requisition_amount.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_narration.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_return_period.getText().toString().isEmpty())
                {
                    AlertBox();
                }

                else
                {

                    String title="Save";
                    String message=" Do you really want to save?";

                    AlertDialog.Builder save=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
                    save.setMessage(message);
                    save.setCancelable(true);
                    save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String corp_id=userSingletonModel.getCorporate_id();
                            String requisition_id=CustomRequisitionListAdapter.requisition_id;
                            String requisition_date=currntdate;
                            int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                            int requisition_reason=Integer.parseInt(reason_id);
                            double requisition_amount=Double.parseDouble(ed_requisition_amount.getText().toString());
                            String description=ed_narration.getText().toString();
                            double ctc_amount=Double.parseDouble(ed_ctc.getText().toString());
                            int return_period_in_months=Integer.parseInt(ed_return_period.getText().toString());
                            String requisition_status="Saved";
                            int approved_requisition_amount=0;
                            int approved_by_id=0;
                            String approved_date="";
                            String supervisor_remark="";
                            int supervisor1_id=Integer.parseInt(userSingletonModel.getSupervisor_1());
                            int supervisor2_id=Integer.parseInt(userSingletonModel.getSupervisor_2());


                            SaveMyRequisitionData(corp_id,requisition_id,requisition_date,employee_id,requisition_reason,requisition_amount,
                                    description,ctc_amount,return_period_in_months,requisition_status,
                                    approved_requisition_amount,approved_by_id,approved_date,supervisor_remark,supervisor1_id,supervisor2_id);
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
            }


        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ed_requisition_amount.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_narration.getText().toString().isEmpty())
                {
                    AlertBox();
                }
                else if(ed_return_period.getText().toString().isEmpty())
                {
                    AlertBox();
                }


                else {
                    String title="Submit";
                    String message="Do you really want to submit now?";


                    AlertDialog.Builder save=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
                    save.setMessage(message);
                    save.setCancelable(true);
                    save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String corp_id=userSingletonModel.getCorporate_id();
                            String requisition_id=CustomRequisitionListAdapter.requisition_id;
                            String requisition_date=currntdate;
                            int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                            int requisition_reason=Integer.parseInt(reason_id);
                            double requisition_amount=Double.parseDouble(ed_requisition_amount.getText().toString());
                            String description=ed_narration.getText().toString();
                            double ctc_amount=Double.parseDouble(ed_ctc.getText().toString());
                            int return_period_in_months=Integer.parseInt(ed_return_period.getText().toString());
                            String requisition_status="Submitted";
                            int approved_requisition_amount=0;
                            int approved_by_id=0;
                            String approved_date="";
                            String supervisor_remark="";
                            int supervisor1_id=Integer.parseInt(userSingletonModel.getSupervisor_1());
                            int supervisor2_id=Integer.parseInt(userSingletonModel.getSupervisor_2());


                            SaveMyRequisitionData(corp_id,requisition_id,requisition_date,employee_id,requisition_reason,requisition_amount,
                                    description,ctc_amount,return_period_in_months,requisition_status,
                                    approved_requisition_amount,approved_by_id,approved_date,supervisor_remark,supervisor1_id,supervisor2_id);
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

            }
        });



        if(Url.isNewEntry==false)
        {
            editMyRequisitionMode();
            tv_header.setText("My Advance Requisition Detail");
            edtv_employee_name.setText(userSingletonModel.getFull_employee_name());


        }
        if(Url.isNewEntry==false && Url.isMyRequisition==false)
        {
            editSubordinateRequisitionMode();
            tv_header.setText("Subordinate Advance Requisition Detail");
            edtv_employee_name.setText(CustomSubordinateRequisitionListAdapter.employee_name);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
    }

    private void loadSpinner() {
        String url=Url.BASEURL()+"advance-requisition/reasons/"+userSingletonModel.corporate_id;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(!spinner_modelArrayList.isEmpty())
                {
                    spinner_modelArrayList.clear();
                }


                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("reasons");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jb1=jsonArray.getJSONObject(i);

                        Requisition_Reason_Spinner_Model model=new Requisition_Reason_Spinner_Model();
                        model.setReason_id(jb1.getString("reason_id"));
                        model.setDescription(jb1.getString("description"));

                        spinner_modelArrayList.add(model);
                    }

                    for(int i=0;i<spinner_modelArrayList.size();i++)
                    {

                        reasonspinnerlist.add(spinner_modelArrayList.get(i).getDescription());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdvanceRequisitionEntryActivity.this, android.R.layout.simple_spinner_item, reasonspinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_reason_advance.setAdapter(adapter);

//                    if(Url.isNewEntry==true)
//                    {
//                        spinner_reason_advance.setSelection(0);
//                    }
                    if(Url.isMyRequisition==true)
                    {
                        spinner_reason_advance.setSelection(Integer.parseInt(CustomRequisitionListAdapter.reason)-1);
                    }
                    if(Url.isSubordinateRequisition==true)
                    {
                        spinner_reason_advance.setSelection(Integer.parseInt(CustomSubordinateRequisitionListAdapter.reason)-1);
                    }

                    spinner_reason_advance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            reason_id=spinner_modelArrayList.get(position).getReason_id();
                            //Toast.makeText(AdvanceRequisitionEntryActivity.this, ""+item, Toast.LENGTH_SHORT).show();
                            // Toast.makeText(AdvanceRequisitionEntryActivity.this, ""+reason_id, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdvanceRequisitionEntryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdvanceRequisitionEntryActivity.this, "could't connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(AdvanceRequisitionEntryActivity.this).add(stringRequest);



    }

    //-----function for Back button logic for Alert, code starts (added on 6-Aug-2021)---
    private void AlertBack() {

        String message="You may lost any unsaved data. Do you really want to go back?";

        AlertDialog.Builder builder=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
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


    private void editSubordinateRequisitionMode() {

        if(Url.isSubordinateRequisition==true )
        {
            ed_requisition_amount.setEnabled(false);
            ed_ctc.setEnabled(false);
            ed_narration.setEnabled(false);
            ed_return_period.setEnabled(false);

            spinner_reason_advance.setEnabled(false);



            edtv_requisition_no.setText(CustomSubordinateRequisitionListAdapter.requisition_no);
            ed_requisition_amount.setText(CustomSubordinateRequisitionListAdapter.requisition_amount);
            ed_narration.setText(CustomSubordinateRequisitionListAdapter.description);
            ed_approved_amount.setText(CustomSubordinateRequisitionListAdapter.approved_requisition_amount);
            edtv_application_status.setText(CustomSubordinateRequisitionListAdapter.requisition_status);
            ed_ctc.setText(userSingletonModel.getCtc());
            ed_return_period.setText(CustomSubordinateRequisitionListAdapter.return_period_in_months);
            ed_approval_remark.setText(CustomSubordinateRequisitionListAdapter.supervisor_remark);



            if(CustomSubordinateRequisitionListAdapter.requisition_status.contentEquals("Submitted"))
            {


                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_return.setVisibility(View.VISIBLE);
                btn_approve.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);
            }

            if(CustomSubordinateRequisitionListAdapter.requisition_status.contentEquals("Approved") || CustomSubordinateRequisitionListAdapter.requisition_status.contentEquals("Returned")|| CustomSubordinateRequisitionListAdapter.requisition_status.contentEquals("Payment done"))
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
            if(CustomSubordinateRequisitionListAdapter.requisition_status.contentEquals("Canceled"))
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





            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                        if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_requisition_amount.getText().toString())))
//                        {
//                            ///Toast.makeText(AdvanceRequisitionEntryActivity.this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
//                            AlertBox1();
//                        }


                    if(ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if(ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }


                    else {

                        String title="Cancel";
                        String message="Do you really want to cancel this Advance Requisition?";


                        AlertDialog.Builder save=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
                        save.setMessage(message);
                        save.setCancelable(true);
                        save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String corp_id=userSingletonModel.getCorporate_id();
                                String requisition_id=CustomSubordinateRequisitionListAdapter.requisition_id;
                                String requisition_date=currntdate;
                                int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                                int requisition_reason=Integer.parseInt(reason_id);
                                double requisition_amount=Double.parseDouble(ed_requisition_amount.getText().toString());
                                String description=ed_narration.getText().toString();
                                double ctc_amount=Double.parseDouble(ed_ctc.getText().toString());
                                int return_period_in_months=Integer.parseInt(ed_return_period.getText().toString());
                                String requisition_status="Canceled";
                                int approved_requisition_amount=0;
                                int approved_by_id=0;
                                String approved_date="";
                                String supervisor_remark=ed_approval_remark.getText().toString();
                                int supervisor1_id=Integer.parseInt(userSingletonModel.getSupervisor_1());
                                int supervisor2_id=Integer.parseInt(userSingletonModel.getSupervisor_2());


                                SaveMyRequisitionData(corp_id,requisition_id,requisition_date,employee_id,requisition_reason,requisition_amount,
                                        description,ctc_amount,return_period_in_months,requisition_status,
                                        approved_requisition_amount,approved_by_id,approved_date,supervisor_remark,supervisor1_id,supervisor2_id);
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
                }
            });

            btn_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_requisition_amount.getText().toString())))
                    {
                        ///Toast.makeText(AdvanceRequisitionEntryActivity.this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
                        AlertBox1();
                    }

                    else if(ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if(ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }


                    else {
                        String title="Approve";
                        String message="Do you want to approve this Advance Requisition?";


                        AlertDialog.Builder save=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
                        save.setMessage(message);
                        save.setCancelable(true);
                        save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String corp_id=userSingletonModel.getCorporate_id();
                                String requisition_id=CustomSubordinateRequisitionListAdapter.requisition_id;
                                String requisition_date=currntdate;
                                int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                                int requisition_reason=Integer.parseInt(reason_id);
                                double requisition_amount=Double.parseDouble(ed_requisition_amount.getText().toString());
                                String description=ed_narration.getText().toString();
                                double ctc_amount=Double.parseDouble(ed_ctc.getText().toString());
                                int return_period_in_months=Integer.parseInt(ed_return_period.getText().toString());
                                String requisition_status="Approved";
                                int approved_requisition_amount=Integer.parseInt(ed_approved_amount.getText().toString());
                                int approved_by_id=0;
                                String approved_date="";
                                String supervisor_remark=ed_approval_remark.getText().toString();
                                int supervisor1_id=Integer.parseInt(userSingletonModel.getSupervisor_1());
                                int supervisor2_id=Integer.parseInt(userSingletonModel.getSupervisor_2());


                                SaveMyRequisitionData(corp_id,requisition_id,requisition_date,employee_id,requisition_reason,requisition_amount,
                                        description,ctc_amount,return_period_in_months,requisition_status,
                                        approved_requisition_amount,approved_by_id,approved_date,supervisor_remark,supervisor1_id,supervisor2_id);
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
                }
            });

            btn_return.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    if(!(Double.parseDouble(ed_approved_amount.getText().toString()) <Double.parseDouble(ed_requisition_amount.getText().toString())))
//                    {
//                        ///Toast.makeText(AdvanceRequisitionEntryActivity.this, " approved amount cannot be more than amount", Toast.LENGTH_SHORT).show();
//                        AlertBox1();
//                    }

                    if(ed_approved_amount.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }
                    else if(ed_approval_remark.getText().toString().isEmpty())
                    {
                        AlertBox();
                    }


                    else {
                        String title="Return";
                        String message="Do you want to return  this Advance Requisition?";

                        AlertDialog.Builder save=new AlertDialog.Builder(AdvanceRequisitionEntryActivity.this);
                        save.setMessage(message);
                        save.setCancelable(true);
                        save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String corp_id=userSingletonModel.getCorporate_id();
                                String requisition_id=CustomSubordinateRequisitionListAdapter.requisition_id;
                                String requisition_date=currntdate;
                                int employee_id=Integer.parseInt(userSingletonModel.getEmployee_id());
                                int requisition_reason=Integer.parseInt(reason_id);
                                double requisition_amount=Double.parseDouble(ed_requisition_amount.getText().toString());
                                String description=ed_narration.getText().toString();
                                double ctc_amount=Double.parseDouble(ed_ctc.getText().toString());
                                int return_period_in_months=Integer.parseInt(ed_return_period.getText().toString());
                                String requisition_status="Returned";
                                int approved_requisition_amount=0;
                                int approved_by_id=0;
                                String approved_date="";
                                String supervisor_remark=ed_approval_remark.getText().toString();
                                int supervisor1_id=Integer.parseInt(userSingletonModel.getSupervisor_1());
                                int supervisor2_id=Integer.parseInt(userSingletonModel.getSupervisor_2());


                                SaveMyRequisitionData(corp_id,requisition_id,requisition_date,employee_id,requisition_reason,requisition_amount,
                                        description,ctc_amount,return_period_in_months,requisition_status,
                                        approved_requisition_amount,approved_by_id,approved_date,supervisor_remark,supervisor1_id,supervisor2_id);
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
                }
            });


        }




    }

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

    private void SaveMyRequisitionData(String corp_id, String requisition_id, String requisition_date, int employee_id,
                                       int requisition_reason, double requisition_amount,
                                       String description, double ctc_amount,
                                       int return_period_in_months, String requisition_status,
                                       int approved_requisition_amount, int approved_by_id, String approved_date,
                                       String supervisor_remark, int supervisor1_id, int supervisor2_id) {
        try {
            my_requisition_jsonBody.put("corp_id",corp_id);
            if(Url.isNewEntry==true)
            {
                my_requisition_jsonBody.put("requisition_id",0);
            }
            else
            {
                my_requisition_jsonBody.put("requisition_id",requisition_id);
            }
            my_requisition_jsonBody.put("requisition_date",requisition_date);
            my_requisition_jsonBody.put("employee_id",employee_id);
            my_requisition_jsonBody.put("requisition_reason",requisition_reason);
            my_requisition_jsonBody.put("requisition_amount",requisition_amount);
            my_requisition_jsonBody.put("description",description);
            my_requisition_jsonBody.put("ctc_amount",ctc_amount);
            my_requisition_jsonBody.put("return_period_in_months",return_period_in_months);
            my_requisition_jsonBody.put("requisition_status",requisition_status);
            my_requisition_jsonBody.put("approved_requisition_amount",approved_requisition_amount);
            my_requisition_jsonBody.put("approved_by_id",approved_by_id);
            my_requisition_jsonBody.put("approved_date",approved_date);
            my_requisition_jsonBody.put("supervisor_remark",supervisor_remark);
            my_requisition_jsonBody.put("supervisor1_id",supervisor1_id);
            my_requisition_jsonBody.put("supervisor2_id",supervisor2_id);

//

        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d("akdkd==>",my_requisition_jsonBody.toString());
        String url = Url.BASEURL() + "advance-requisition/save";

        try
        {
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(my_requisition_jsonBody.toString()), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message = response.getString("message");
                        Log.d("message=>",message);
                        Toast.makeText(AdvanceRequisitionEntryActivity.this, message, Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(AdvanceRequisitionEntryActivity.this, AdvanceRequisitionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                                finish(); // commented by sr
                        Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdvanceRequisitionEntryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(AdvanceRequisitionEntryActivity.this).add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void editMyRequisitionMode() {

        if(Url.isMyRequisition==true )
        {

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
            edtv_requisition_no.setText(CustomRequisitionListAdapter.requisition_no);
            ed_requisition_amount.setText(CustomRequisitionListAdapter.requisition_amount);
            ed_narration.setText(CustomRequisitionListAdapter.description);
            ed_approved_amount.setText(CustomRequisitionListAdapter.approved_requisition_amount);
            edtv_application_status.setText(CustomRequisitionListAdapter.requisition_status);
            ed_ctc.setText(userSingletonModel.getCtc());
            ed_return_period.setText(CustomRequisitionListAdapter.return_period_in_months);
            ed_approval_remark.setText(CustomRequisitionListAdapter.supervisor_remark);

            // spinner_reason_advance.setAdapter(CustomRequisitionListAdapter.reason);



            if(CustomRequisitionListAdapter.requisition_status.contentEquals("Saved"))
            {
                edtv_application_status.setText("Saved");
            }
            else if(CustomRequisitionListAdapter.requisition_status.contentEquals("Submitted"))
            {
                edtv_application_status.setText("Submitted");
            }

            else if(CustomRequisitionListAdapter.requisition_status.contentEquals("Returned"))
            {
                edtv_application_status.setText("Returned");
            }
            else
                edtv_application_status.setText(CustomRequisitionListAdapter.requisition_status);

            ed_approval_remark.setText(CustomRequisitionListAdapter.supervisor_remark);

            if(CustomRequisitionListAdapter.requisition_status.contentEquals("Saved"))

            {

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);

                ed_ctc.setEnabled(false);
                ed_approved_amount.setEnabled(false);
                ed_approval_remark.setEnabled(false);
                edtv_application_status.setEnabled(false);

            }
            if(CustomRequisitionListAdapter.requisition_status.contentEquals("Submitted") || CustomRequisitionListAdapter.requisition_status.contentEquals("Approved")|| CustomRequisitionListAdapter.requisition_status.contentEquals("Payment done"))

            {
                btn_submit.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);

                ed_ctc.setEnabled(false);
                edtv_requisition_no.setEnabled(false);
                ed_requisition_amount.setEnabled(false);
                //   spinner_reason_advance.setEnabled(false);
                ed_narration.setEnabled(false);
                ed_approved_amount.setEnabled(false);
                ed_approval_remark.setEnabled(false);
                ed_return_period.setEnabled(false);
                spinner_reason_advance.setEnabled(false);


            }
            if(CustomRequisitionListAdapter.requisition_status.contentEquals("Returned"))
            {
                btn_save.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);

                ed_approved_amount.setEnabled(false);
                ed_approval_remark.setEnabled(false);
            }

            if(CustomRequisitionListAdapter.requisition_status.contentEquals("Canceled"))
            {
                btn_submit.setVisibility(View.GONE);
                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);

                ed_ctc.setEnabled(false);
                edtv_requisition_no.setEnabled(false);
                ed_requisition_amount.setEnabled(false);
                //   spinner_reason_advance.setEnabled(false);
                ed_narration.setEnabled(false);
                ed_approved_amount.setEnabled(false);
                ed_approval_remark.setEnabled(false);
                ed_return_period.setEnabled(false);
                spinner_reason_advance.setEnabled(false);

            }



        }


    }
}
