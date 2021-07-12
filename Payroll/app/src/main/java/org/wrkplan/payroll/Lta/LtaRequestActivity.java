package org.wrkplan.payroll.Lta;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LtaRequestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_document_view;
    Button btn_back, btn_cancel, btn_return, btn_approve, btn_submit, btn_save;
    EditText edt_from_date_select, edt_to_date_select;
    ImageButton imgBtnCalenderFrom, imgBtnCalenderTo;
    TextView tv_total_days;
    final Calendar myCalendarFromDate = Calendar.getInstance();
    final Calendar myCalendarToDate = Calendar.getInstance();
    Integer flag_datefield_check = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lta_request);

        btn_back = findViewById(R.id.btn_back);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_return = findViewById(R.id.btn_return);
        btn_approve = findViewById(R.id.btn_approve);
        btn_submit = findViewById(R.id.btn_submit);
        btn_save = findViewById(R.id.btn_save);
        tv_document_view = findViewById(R.id.tv_document_view);
        tv_total_days = findViewById(R.id.tv_total_days);

        edt_from_date_select = findViewById(R.id.edt_from_date_select);
        edt_to_date_select = findViewById(R.id.edt_to_date_select);
        imgBtnCalenderFrom = findViewById(R.id.imgBtnCalenderFrom);
        imgBtnCalenderTo = findViewById(R.id.imgBtnCalenderTo);

        imgBtnCalenderFrom.setOnClickListener(this);
        imgBtnCalenderTo.setOnClickListener(this);

        tv_document_view.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        LoadButtons();
    }

    //=====onClick code starts=====
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_document_view:
                startActivity(new Intent(LtaRequestActivity.this,LtaDocumentsActivity.class));
                break;
            case R.id.btn_back:
                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_return:
                break;
            case R.id.btn_approve:
                break;
            case R.id.btn_submit:
                break;
            case R.id.btn_save:
                break;
            case R.id.imgBtnCalenderFrom:
                calendarPicker(myCalendarFromDate,edt_from_date_select, "from_date");
                break;
            case R.id.imgBtnCalenderTo:
                calendarPicker(myCalendarToDate,edt_to_date_select, "to_date");
                break;
        }
    }
    //=====onClick code ends=====
    //----function to load buttons acc to the logic, code starts
    public void LoadButtons(){


        if (LtaListActivity.EmployeeType == "Employee"){
//            LabelNavBarTitle.text = "My Advance Requisition"
//            btn_reason_select_type.isUserInteractionEnabled = true
//            btn_reason_select_type.alpha = 1.0
            if (LtaListActivity.mediclaim_status.contentEquals("")){
                btn_back.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);




                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true

                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/

            }
            if (LtaListActivity.mediclaim_status.contentEquals("Saved")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);



                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Submitted")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);



                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false

                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
            if (LtaListActivity.mediclaim_status.contentEquals("Returned")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);

                /*TxtMediclaimAmount.isUserInteractionEnabled = true
                TxtReason.isUserInteractionEnabled = true
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
        }
        if (LtaListActivity.EmployeeType == "Supervisor"){
//            LabelNavBarTitle.text = "Subordinate Advance Requisition"
//            btn_reason_select_type.isUserInteractionEnabled = false
//            btn_reason_select_type.alpha = 0.6
            if (LtaListActivity.mediclaim_status.contentEquals("Submitted")){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.VISIBLE);
                btn_return.setVisibility(View.VISIBLE);

                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false
                TxtApprovedAmount.isUserInteractionEnabled = true
                TxtViewApprovalRemark.isUserInteractionEnabled = true*/
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Returned")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                btn_back.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);

                btn_approve.setVisibility(View.GONE);
                btn_return.setVisibility(View.GONE);




                /*TxtMediclaimAmount.isUserInteractionEnabled = false
                TxtReason.isUserInteractionEnabled = false
                TxtApprovedAmount.isUserInteractionEnabled = false
                TxtViewApprovalRemark.isUserInteractionEnabled = false*/
            }
        }


    }

    //----function to load buttons acc to the logic, code ends

    //---------Calendar code starts--------
    //---------Date Difference function, code starts-------
    public String get_date_difference(String fromDate, String toDate){
        String dayDifference = "";
        try {
            //Dates to compare
           /* String CurrentDate=  "09/24/2015";
            String FinalDate=  "09/26/2015";*/

            Date date1;
            Date date2;

//            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");

            //Setting dates
            date1 = dates.parse(fromDate);
            date2 = dates.parse(toDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
//            long difference = date1.getTime() - date2.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            if (date2.getTime() < date1.getTime()) {
                Toast.makeText(getApplicationContext(), "To Date should be graeter than \"From Date\"", Toast.LENGTH_LONG).show();
                flag_datefield_check = 0;

//                    edt_date_to_select.setText("");
//                    edt_date_to_select.setText(edt_from_date_select.getText().toString());
//                    calendarPicker(myCalendarToDate, edt_date_to_select);
              /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please enter valid date")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                dialog.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();*/
                dayDifference = Long.toString(-(differenceDates+1));
                dayDifference = dayDifference;
            }else if(date2.getTime() >= date1.getTime()){
                //Convert long to String
                flag_datefield_check = 1;
                dayDifference = Long.toString(differenceDates+1);
            }
//            dayDifference = Long.toString(differenceDates+1);

            Log.e("HERE","HERE: " + dayDifference);


        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
        return dayDifference;
    }
    //---------Date Difference function, code ends-------
    public void calendarPicker(final Calendar myCalendar, final EditText editText, final String button_status){
        try {
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(myCalendar, editText);

                    //---for toDate checking, added on 29th May
//                    get_date_difference(edt_from_date_select.getText().toString(), edt_date_to_select.getText().toString(), button_status);
                   /* if(LtaListActivity.mediclaim_status != "Saved" || LtaListActivity.mediclaim_status != "Returned") {
                        if (edt_date_to_select.getText().toString().isEmpty()) {
                            edt_date_to_select.setText(edt_from_date_select.getText().toString());
                        }
                    }*/

                    btn_save.setClickable(true);
                    btn_save.setAlpha(1.0f);


                    if (!edt_from_date_select.getText().toString().isEmpty() && !edt_to_date_select.getText().toString().isEmpty()) {
                        Log.d("date diff check-=>", get_date_difference(edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString()));
//                    edt_date_to_select.setText(edt_from_date_select.getText().toString()); //added on 29th May
                        tv_total_days.setText(get_date_difference(edt_from_date_select.getText().toString(), edt_to_date_select.getText().toString()));
                    } else if (edt_from_date_select.getText().toString().isEmpty()) {
                        tv_total_days.setText("");
                    }
                }

            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(LtaRequestActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            // to set Max Date
//                myCalendar.set(2019, -1, 1);
            long now = System.currentTimeMillis() - 1000;
//               datePickerDialog.getDatePicker().setMaxDate(now); //---set max date

            Calendar cal = Calendar.getInstance();
//               cal.add(Calendar.MONTH, -2);
//        cal.add(Calendar.MONTH, 1);
            Date preToPreMonthDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = sdf.format(preToPreMonthDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date mDate = sdf1.parse(strDate);
                long timeInMilliseconds = mDate.getTime();
                datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);
                System.out.println("Date in milli :: " + timeInMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePickerDialog.show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Internal Error occurred",Toast.LENGTH_LONG).show();
        }
    }

    private void updateLabel(Calendar myCalendar,EditText editText) {
//        String myFormat = "MM/dd/yy"; //In which you need put here
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
//        loadLocationData(sdf.format(myCalendar.getTime()));
    }
    //---------Calendar code ends--------
}
