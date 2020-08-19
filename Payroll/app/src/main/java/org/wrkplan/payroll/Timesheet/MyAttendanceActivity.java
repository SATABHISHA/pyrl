package org.wrkplan.payroll.Timesheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.wrkplan.payroll.R;

public class MyAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl_button;
    TextView tv_button_subordinate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);

        rl_button = findViewById(R.id.rl_button);
        tv_button_subordinate = findViewById(R.id.tv_button_subordinate);

        rl_button.setOnClickListener(this);
        tv_button_subordinate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_button:
                startActivity(new Intent(this, SubordinateAttendanceActivity.class));
                break;
            case R.id.tv_button_subordinate:
                startActivity(new Intent(this, SubordinateAttendanceActivity.class));
                break;
        }
    }
}
