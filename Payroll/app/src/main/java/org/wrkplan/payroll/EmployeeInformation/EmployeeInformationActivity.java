package org.wrkplan.payroll.EmployeeInformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

public class EmployeeInformationActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    TextView emp_code_value,esi_no_value,pf_no_value,uan_no_value,supervisor1_value,supervisor2_value,emp_name_value;
    Button btn_ok;
    ImageView img_back;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EmployeeInformationActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information);


        //----------------INITIALIZE VIEWS-----------------//
        img_back=findViewById(R.id.img_back);
        emp_code_value=findViewById(R.id.emp_code_value);
        esi_no_value=findViewById(R.id.esi_no_value);
        emp_name_value=findViewById(R.id.emp_name_value);
        pf_no_value=findViewById(R.id.pf_no_value);
        uan_no_value=findViewById(R.id.uan_no_value);
        supervisor1_value=findViewById(R.id.supervisor1_value);
        supervisor2_value=findViewById(R.id.supervisor2_value);
        btn_ok=findViewById(R.id.btn_ok);

        //--------------INITIALIZE END----------------//
        btn_ok.setVisibility(View.GONE);

        img_back.setOnClickListener(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeInformationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        emp_name_value.setText(userSingletonModel.getFull_employee_name());
        emp_code_value.setText(userSingletonModel.getEmployee_code());
        esi_no_value.setText(userSingletonModel.getEsi_no());
        pf_no_value.setText(userSingletonModel.getEmployee_pf_no());
        uan_no_value.setText(userSingletonModel.getUan_no());
        supervisor1_value.setText(userSingletonModel.getSupervisor_1_name());
        supervisor2_value.setText(userSingletonModel.getSupervisor_2_name());

        if(userSingletonModel.getEmployee_pf_no().contentEquals(""))
        {
            pf_no_value.setText("NIL");

        }
        if(userSingletonModel.getEsi_no().isEmpty())
        {
            esi_no_value.setText("NIL");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent=new Intent(EmployeeInformationActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
