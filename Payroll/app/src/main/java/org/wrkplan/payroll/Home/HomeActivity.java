package org.wrkplan.payroll.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.AdvanceRequisition.AdvanceRequisitionActivity;
import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.EmployeeDocuments.EmployeeDocumentsActivity;
import org.wrkplan.payroll.EmployeeFacilities.EmployeeFacilitiesActivity;
import org.wrkplan.payroll.EmployeeInformation.EmployeeInformationActivity;
import org.wrkplan.payroll.HolidayDetail.HolidayDetailActivity;
import org.wrkplan.payroll.InsuranceDetail.InsuranceDetail1;
import org.wrkplan.payroll.Leave_Balance.LeaveBalanceActivity;
import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Lta.LtaListActivity;
import org.wrkplan.payroll.Mediclaim.MediclaimActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.OutdoorListActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;
import org.wrkplan.payroll.Reports.ReportHomeListActivity;
import org.wrkplan.payroll.Timesheet.MyAttendanceActivity;
import org.wrkplan.payroll.Timesheet.MyAttendanceActivity_v2;
import org.wrkplan.payroll.Timesheet.MyAttendanceActivity_v3;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageView img_emp_information, img_leave_balance, img_emp_facilities, img_emp_documents, img_company_documents, img_insurance_details, img_holiday_details, img_od_duty, img_info, img_timesheet, img_reports, img_advance, img_lta, img_mediclaim;
    TextView tv_emp_information, tv_leave_balance, tv_emp_facilitis, tv_emp_documemts, tv_cmp_documents,
            tv_insurance_details, tv_holiday_details;
    TextView tv_fullname,tv_companynam;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();
    LinearLayout Linear, ll_od_request, ll_timesheet, ll_reports, ll_mediclaim;
    RelativeLayout ll_od_duty, rl_advance, rl_lta;
    CoordinatorLayout coordinatorLayout;
    Context context;
    //  EditText ed_userpassword;

    //------variable for version update, code starts
    private AppUpdateManager mAppUpdateManager;
    private int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;
    //------variable for version update, code ends



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_temporary); //commented on 12th jan
        setContentView(R.layout.activity_home);
        Linear=findViewById(R.id.Linear);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        context = HomeActivity.this;

        //-----added on 7th July, code starts-------
        // Get token
        // [START retrieve_current_token]
//        startService(MyFirebaseMessagingService.class);
        /*FirebaseApp.initializeApp(getApplicationContext());
        Toast.makeText(getApplicationContext(),FirebaseInstanceId.getInstance().getToken(),Toast.LENGTH_LONG).show();*/
        FirebaseApp.initializeApp(context);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.d("fcm token", instanceIdResult.getToken());
                    }
                });
        // [END retrieve_current_token]
        //-----added on 7th July, code ends-------
        //--------------------NAVIGATION DRAWER PORTIONS--------------------------//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        builder = new AlertDialog.Builder(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final View header = navigationView.getHeaderView(0);
        tv_fullname=header.findViewById(R.id.tv_fullname);
        tv_companynam=header.findViewById(R.id.tv_companynam);
        //  ed_userpassword=findViewById(R.id.ed_userpassword);
        coordinatorLayout=findViewById(R.id.cordinatorLayout);
        tv_fullname.setText(userSingletonModel.getFull_employee_name());
        tv_companynam.setText(userSingletonModel.getCompany_name());
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------END OF NAVIGATION DRAWER PORTIONS--------------------//


        //---------------------Initialize views-----------------//
        img_emp_information = findViewById(R.id.img_emp_information);
        img_leave_balance = findViewById(R.id.img_leave_balance);
        img_emp_facilities = findViewById(R.id.img_emp_facilities);
        img_emp_documents = findViewById(R.id.img_emp_documents);
        img_company_documents = findViewById(R.id.img_company_documents);
        img_insurance_details = findViewById(R.id.img_insurance_details);
        img_holiday_details = findViewById(R.id.img_holiday_details);
        tv_emp_information = findViewById(R.id.tv_emp_information);
        tv_leave_balance = findViewById(R.id.tv_leave_balance);
        tv_emp_facilitis = findViewById(R.id.tv_emp_facilitis);
        tv_emp_documemts = findViewById(R.id.tv_emp_documemts);
        tv_cmp_documents = findViewById(R.id.tv_cmp_documents);
        tv_insurance_details = findViewById(R.id.tv_insurance_details);
        tv_holiday_details = findViewById(R.id.tv_holiday_details);

        tv_companynam=findViewById(R.id.tv_companynam);

        ll_od_request=findViewById(R.id.ll_od_request);
        ll_od_duty=findViewById(R.id.ll_od_duty);
        img_od_duty=findViewById(R.id.img_od_duty);
        img_info=findViewById(R.id.img_info); //making temporary disable on 9th dec

        ll_timesheet=findViewById(R.id.ll_timesheet);
        img_timesheet=findViewById(R.id.img_timesheet);

        rl_advance=findViewById(R.id.rl_advance);
        img_advance=findViewById(R.id.img_advance);

        ll_reports=findViewById(R.id.ll_reports);
        img_reports=findViewById(R.id.img_reports);

        rl_lta = findViewById(R.id.rl_lta);
        img_lta=findViewById(R.id.img_lta);

        ll_mediclaim = findViewById(R.id.ll_mediclaim);
        img_mediclaim=findViewById(R.id.img_mediclaim);

        //------------------End of Initialize views--------------//

        img_emp_information.setOnClickListener(this);
        img_leave_balance.setOnClickListener(this);
        img_emp_facilities.setOnClickListener(this);
        img_emp_documents.setOnClickListener(this);
        img_company_documents.setOnClickListener(this);
        img_insurance_details.setOnClickListener(this);
        img_holiday_details.setOnClickListener(this);
        tv_emp_information.setOnClickListener(this);
        tv_leave_balance.setOnClickListener(this);
        tv_emp_facilitis.setOnClickListener(this);
        tv_emp_documemts.setOnClickListener(this);
        tv_cmp_documents.setOnClickListener(this);
        tv_insurance_details.setOnClickListener(this);
        tv_holiday_details.setOnClickListener(this);

        ll_od_request.setOnClickListener(this);
        ll_od_duty.setOnClickListener(this);
        img_od_duty.setOnClickListener(this); //temporary making disable on 9th dec

        ll_timesheet.setOnClickListener(this);
        img_timesheet.setOnClickListener(this);

        rl_advance.setOnClickListener(this);
        img_advance.setOnClickListener(this);

        ll_reports.setOnClickListener(this);
        img_reports.setOnClickListener(this);

        rl_lta.setOnClickListener(this);
        img_lta.setOnClickListener(this);

        ll_mediclaim.setOnClickListener(this);
        img_mediclaim.setOnClickListener(this);

        load_data_check_od_duty(); //--function to check weather od_duty exists or not/temporary commented on 9th dec

        //----added on 20th July for version update, starts----
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        //lambda operation used for below listener
        //For flexible update
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        //For Flexible
        inAppUpdateType = AppUpdateType.FLEXIBLE;//1
        inAppUpdate();
        //----added on 20th July for version update, ends----


    }

    //-------added on 4th Sept code for version update, starts----


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == RESULT_OK) {
                Toast.makeText(HomeActivity.this, "App download starts...", Toast.LENGTH_LONG).show();
            } else if (resultCode != RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(HomeActivity.this, "App download canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(HomeActivity.this, "App download failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onDestroy();
    }

    private void inAppUpdate() {

        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // For a flexible update, use AppUpdateType.FLEXIBLE
                            && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)) {
                        // Request the update.

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(
                                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                    appUpdateInfo,
                                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                    inAppUpdateType,
                                    // The current activity making the update request.
                                    HomeActivity.this,
                                    // Include a request code to later monitor this update request.
                                    RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void popupSnackbarForCompleteUpdate() {
        try {
            com.google.android.material.snackbar.Snackbar snackbar =
                    com.google.android.material.snackbar.Snackbar.make(
                            findViewById(R.id.cordinatorLayout),
                            "An update has just been downloaded.\nRestart to update",
                            com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("INSTALL", view -> {
                if (mAppUpdateManager != null){
                    mAppUpdateManager.completeUpdate();
                }
            });
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            snackbar.show();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }
    //-------added on 4th Sept code for version update, ends----


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_logout:

                //  finish();
                //System.exit(0);
//                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                LoginActivity.ed_userpassword.setText("");
                                editor.remove("username");
                                editor.remove("userid");
                                editor.remove("savelogin");
                                editor.clear();
                                editor.commit();
//                                a.addCategory(Intent.CATEGORY_HOME);
//                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert_logout = builder.create();
                //Setting the title manually
                alert_logout.setTitle("Logout");
                alert_logout.show();
                break;


            case R.id.emp_information:
                Intent intent = new Intent(HomeActivity.this, EmployeeInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.leave_balance:
                Intent intent1 = new Intent(HomeActivity.this, LeaveBalanceActivity.class);
                startActivity(intent1);
                break;
            case R.id.emp_facilities:
                Intent intent3 = new Intent(HomeActivity.this, EmployeeFacilitiesActivity.class);
                startActivity(intent3);
                break;
            case R.id.emp_documents:
                Intent intent4 = new Intent(HomeActivity.this, EmployeeDocumentsActivity.class);
                startActivity(intent4);
                break;
            case R.id.cmp_documents:
                Intent intent5 = new Intent(HomeActivity.this, CompanyDocumentsActivity.class);
                startActivity(intent5);
                break;
            case R.id.insurance_detail:
                Intent intent6 = new Intent(HomeActivity.this, InsuranceDetail1.class);
                startActivity(intent6);
                break;
            case R.id.holiday_detail:
                Intent intent7 = new Intent(HomeActivity.this, HolidayDetailActivity.class);
                startActivity(intent7);
                break;

            case R.id.nav_outdoor_duty_request:
                Intent intent8 = new Intent(HomeActivity.this, OutdoorListActivity.class);
                startActivity(intent8);
                break;
            case R.id.nav_outdoor_duty:
                startActivity(new Intent(this, OdDutyLogListActivity.class));
                break; //making temporary disable on 9th dec
            case R.id.nav_timesheet:
//                startActivity(new Intent(this, MyAttendanceActivity.class));
                startActivity(new Intent(this, MyAttendanceActivity_v3.class));
                break;
            case R.id.nav_change_pswd:
                //--------adding custom dialog on 14th may starts------
                LayoutInflater li2 = LayoutInflater.from(this);
                View dialog = li2.inflate(R.layout.dialog_change_password, null);
                final EditText ed_current_password = dialog.findViewById(R.id.ed_current_password);
                final EditText edt_new_password = dialog.findViewById(R.id.edt_new_password);
                final EditText edt_retype_password = dialog.findViewById(R.id.edt_retype_password);
                final TextView tv_pswd_chk = dialog.findViewById(R.id.tv_pswd_chk);
                final TextView tv_submit = dialog.findViewById(R.id.tv_submit);
                RelativeLayout rl_cancel = dialog.findViewById(R.id.rl_cancel);
                final RelativeLayout rl_submit = dialog.findViewById(R.id.rl_submit);
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
                alert.setView(dialog);
//                        alert.setCancelable(false);
                //Creating an alert dialog
                final android.app.AlertDialog alertDialog = alert.create();
                alertDialog.show();
                rl_submit.setClickable(false);
//            tv_submit.setAlpha(0.5f);
                rl_submit.setAlpha(0.5f);
                rl_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                edt_retype_password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(edt_new_password.getText().toString().contentEquals(charSequence)){
                            tv_pswd_chk.setVisibility(View.VISIBLE);
                            tv_pswd_chk.setTextColor(Color.parseColor("#00AE00"));
                            tv_pswd_chk.setText("Correct Password");
//                        tv_submit.setAlpha(1.0f);
                            rl_submit.setAlpha(1.0f);
                            rl_submit.setClickable(true);
                        }else {
                            tv_pswd_chk.setVisibility(View.VISIBLE);
                            tv_pswd_chk.setTextColor(Color.parseColor("#AE0000"));
                            rl_submit.setClickable(false);
//                        tv_submit.setAlpha(0.5f);
                            rl_submit.setAlpha(0.5f);
                            tv_pswd_chk.setText("Incorrect Password");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                rl_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ed_current_password.getText().toString().contentEquals("") || edt_retype_password.getText().toString().contentEquals("") ){
                            //----to display message in snackbar, code starts
                            String message_notf = "Field cannot be left blank";
                            int color = Color.parseColor("#FFFFFF");
                            View v1 = findViewById(R.id.cordinatorLayout);
                            new org.wrkplan.payroll.Config.Snackbar(message_notf,v1,color);
                            //----to display message in snackbar, code ends
                        }else{
//                        changePswd(ed_current_password.getText().toString(),edt_new_password.getText().toString(),ed_password_hint.getText().toString());
                            change_password(ed_current_password.getText().toString(),edt_new_password.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.nav_advance:
                startActivity(new Intent(this, AdvanceRequisitionActivity.class));
                //Toast.makeText(context, "Requisation Page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_mediclaim:
                startActivity(new Intent(this, MediclaimActivity.class));
                //Toast.makeText(context, "Requisation Page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_lta:
                startActivity(new Intent(this, LtaListActivity.class));
                //Toast.makeText(context, "Requisation Page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_reports:
                startActivity(new Intent(this, ReportHomeListActivity.class));
                break;

        }
//        return false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce)
        {
            //  super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }
        else {
            doubleBackToExitPressedOnce = true;

            Snackbar snackbar = Snackbar.make(Linear, "Press BACK once more to exit", Snackbar.LENGTH_SHORT);
            snackbar.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                }

            }, 2000);
        }


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_emp_information:
                Intent intent = new Intent(HomeActivity.this, EmployeeInformationActivity.class);
                startActivity(intent);
                finish();
                break;
            case  R.id.img_leave_balance:
                Intent intent1 = new Intent(HomeActivity.this, LeaveBalanceActivity.class);
                startActivity(intent1);
                finish();
                break;
            case  R.id.img_emp_facilities:
                Intent intent2 = new Intent(HomeActivity.this, EmployeeFacilitiesActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.img_emp_documents:
                Intent intent3 = new Intent(HomeActivity.this, EmployeeDocumentsActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.img_company_documents:
                Intent intent4 = new Intent(HomeActivity.this, CompanyDocumentsActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.img_insurance_details:
                Intent intent5 = new Intent(HomeActivity.this, InsuranceDetail1.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.img_holiday_details:
                Intent intent6 = new Intent(HomeActivity.this, HolidayDetailActivity.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.tv_emp_information:
                Intent i1 = new Intent(HomeActivity.this, EmployeeInformationActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.tv_leave_balance:
                Intent i2 = new Intent(HomeActivity.this, LeaveBalanceActivity.class);
                startActivity(i2);
                finish();
                break;
            case R.id.tv_emp_facilitis:
                Intent i3 = new Intent(HomeActivity.this, EmployeeFacilitiesActivity.class);
                startActivity(i3);
                finish();
                break;
            case R.id.tv_emp_documemts:
                Intent i4 = new Intent(HomeActivity.this, EmployeeDocumentsActivity.class);
                startActivity(i4);
                finish();
                break;
            case R.id.tv_cmp_documents:
                Intent i5 = new Intent(HomeActivity.this, CompanyDocumentsActivity.class);
                startActivity(i5);
                finish();
                break;
            case R.id.tv_insurance_details:
                Intent i6 = new Intent(HomeActivity.this, InsuranceDetail1.class);
                startActivity(i6);
                finish();
                break;
            case R.id.tv_holiday_details:
                Intent i7 = new Intent(HomeActivity.this, HolidayDetailActivity.class);
                startActivity(i7);
                finish();
                break;

            case R.id.ll_od_request:
//                Intent intent8 = new Intent(HomeActivity.this, OutDoorRequestActivity.class);
                Intent intent8 = new Intent(HomeActivity.this, OutdoorListActivity.class);
                startActivity(intent8);
                break;
            case R.id.ll_od_duty:
                startActivity(new Intent(this, OdDutyLogListActivity.class));
                break;
            case R.id.img_od_duty:
                startActivity(new Intent(this, OdDutyLogListActivity.class));
                break; //making temporary disable on 9th nov

            case R.id.ll_timesheet:
//                startActivity(new Intent(this, MyAttendanceActivity.class));
                startActivity(new Intent(this, MyAttendanceActivity_v3.class));
                break;
            case R.id.img_timesheet:
//                startActivity(new Intent(this, MyAttendanceActivity.class));
                startActivity(new Intent(this, MyAttendanceActivity_v3.class));
                break;
            case R.id.ll_reports:
                startActivity(new Intent(this, ReportHomeListActivity.class));
                break;
            case R.id.img_reports:
                startActivity(new Intent(this, ReportHomeListActivity.class));
                break;
            case R.id.rl_advance:
                startActivity(new Intent(this, AdvanceRequisitionActivity.class));
                break;
            case R.id.img_advance:
                startActivity(new Intent(this, AdvanceRequisitionActivity.class));
                break;
            case R.id.rl_lta:
                startActivity(new Intent(this, LtaListActivity.class));
                break;
            case R.id.img_lta:
                startActivity(new Intent(this, LtaListActivity.class));
                break;
            case R.id.ll_mediclaim:
                startActivity(new Intent(this, MediclaimActivity.class));
                break;
            case R.id.img_mediclaim:
                startActivity(new Intent(this, MediclaimActivity.class));
                break;
            default:
                break;
        }
    }

    //===========Code to get data to check od_duty_status from api using volley and load data(added by Satabhisha), starts==========
    public void load_data_check_od_duty(){
//        String url = Config.BaseUrlEpharma + "documents/list" ;
//        String url = Url.BASEURL+"od/request/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"od/request/check-exist/"+userSingletonModel.getCorporate_id()+"/"+userSingletonModel.getEmployee_id();
        Log.d("url-=>",url);
//        String url = Url.BASEURL+"od/request/detail/20/1/1";
        final ProgressDialog loading = ProgressDialog.show(HomeActivity.this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get_od_duty_data(response);
                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    public void get_od_duty_data(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonData-=>",jsonObject.toString());
            if(jsonObject.getString("status").contentEquals("true")){
                img_info.setVisibility(View.VISIBLE);



                if(jsonObject.getString("next_action").contentEquals("START")){
                    img_info.setVisibility(View.VISIBLE);

                }else if(jsonObject.getString("next_action").contentEquals("PAUSE")){
                    img_info.setVisibility(View.VISIBLE);

                }else if(jsonObject.getString("next_action").contentEquals("STOP")){
                    img_info.setVisibility(View.VISIBLE);

                }else if(jsonObject.getString("next_action").contentEquals("NA")){
                    img_info.setVisibility(View.GONE);

                }


            }else if(jsonObject.getString("status").contentEquals("false")){
                img_info.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //===========Code to get data from api and load data, ends==========

    //============function to change password, code starts========
    public void change_password(String old_pswd, String new_pswd){
        try {
            final JSONObject DocumentElementobj = new JSONObject();
            DocumentElementobj.put("corp_id", userSingletonModel.getCorporate_id());
            DocumentElementobj.put("employee_id", Integer.parseInt(userSingletonModel.getEmployee_id()));
           /* DocumentElementobj.put("corp_id", "demo_test");
            DocumentElementobj.put("employee_id", 1234);*/
            DocumentElementobj.put("old_pwd", old_pswd);
            DocumentElementobj.put("new_pwd", new_pswd);

            Log.d("jsonObjectTest",DocumentElementobj.toString());
            final String URL = Url.BASEURL() + "user/change-password";

            JsonObjectRequest request_json = null;
            request_json = new JsonObjectRequest(Request.Method.POST, URL,new JSONObject(DocumentElementobj.toString()),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Process os success response
                                JSONObject jsonObj = null;
                                try{
                                    String responseData = response.toString();
                                    JSONObject resobj = new JSONObject(responseData);
                                    Log.d("getData",resobj.toString());

                                    if(resobj.getString("status").contentEquals("true")){
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                        alertDialogBuilder.setMessage(resobj.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        //-----following code is commented on 6th dec to get the calender saved state data------
                                                        alertDialogBuilder.setCancelable(true);
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }else{
//                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        //---------Alert dialog code starts(added on 21st nov)--------
                                        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                        alertDialogBuilder.setMessage(resobj.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        //-----following code is commented on 6th dec to get the calender saved state data------
                                                        alertDialogBuilder.setCancelable(true);
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }


                                }catch (JSONException e){
                                    //                            loading.dismiss();
                                    e.printStackTrace();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    //============function to change password, code ends========

}
