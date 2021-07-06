package org.wrkplan.payroll.Lta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.wrkplan.payroll.R;

public class LtaRequestActivity extends AppCompatActivity {
    Button btn_back, btn_cancel, btn_return, btn_approve, btn_submit, btn_save;
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

        LoadButtons();
    }

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
}
