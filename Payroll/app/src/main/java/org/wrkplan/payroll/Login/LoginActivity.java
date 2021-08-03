package org.wrkplan.payroll.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.AppVersionUpgradeNotifier;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Lta.LtaDocumentPdfViewer;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AppVersionUpgradeNotifier.VersionUpdateListener {
    Button btn_login;
 public static   EditText ed_userid,ed_username,ed_userpassword;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences,autofill;
    UserSingletonModel userSingletonModel=UserSingletonModel.getInstance();


    CheckBox checked_sign;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_autofill;
    Boolean savelogin;

    public static String entry_user = ""; //---added by Satabhisha on 6th May

    public static String url_check = "live"; //----as per discussion on 12th sept, Base url(live or test) distinction should be handled by using static variable
//    final  String url="http://192.168.10.175:9018/api/login/payroll_713/1/1";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login_new);


        AppVersionUpgradeNotifier.init(this,this); //---for version updateDCR
//        statusCheck(); //---added by Satabhisha on 6th MAy //--commented on 1st sept

        //----Firebase code starts

        //----Firebase code ends

//        userSingletonModel.setUrl_test_or_live_check("test"); //----as per discussion on 12th sept, Base url(live or local) distinction should be handled by using static variable
     //--------------------------Initialize views-------------------//
        btn_login=findViewById(R.id.btn_login);
        ed_userid=findViewById(R.id.ed_userid);
        ed_username=findViewById(R.id.ed_username);
        ed_userpassword=findViewById(R.id.ed_userpassword);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        checked_sign=findViewById(R.id.checked_sign);
        //-----------------------End of Initialize Views-------------------//

        //---------sharedpref code starts-----
        sharedPreferences=getSharedPreferences("loginref",MODE_PRIVATE);
        autofill=getSharedPreferences("autologin",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor_autofill=autofill.edit();
      if(!sharedPreferences.getString("username", "").isEmpty()){

          entry_user = sharedPreferences.getString("username", "");//---added by Satabhisha on 6th MAy

          userSingletonModel.setUser_id(sharedPreferences.getString("user_id", ""));
          userSingletonModel.setUser_type(sharedPreferences.getString("user_id", ""));
          userSingletonModel.setFin_year_id(sharedPreferences.getString("fin_year_id", ""));
            //============================

          userSingletonModel.setEmployee_id(sharedPreferences.getString("employee_id", ""));
          userSingletonModel.setEmployee_code(sharedPreferences.getString("employee_code", ""));
          Log.d("msg",userSingletonModel.getEmployee_code());
//
          userSingletonModel.setEmployee_fname(sharedPreferences.getString("employee_fname", ""));
          userSingletonModel.setEmployee_mname(sharedPreferences.getString("employee_mname", ""));
          userSingletonModel.setEmployee_lname(sharedPreferences.getString("employee_lname", ""));

          userSingletonModel.setDepartment_id(sharedPreferences.getString("department_id", ""));
          userSingletonModel.setDesignation_id(sharedPreferences.getString("designation_id", ""));
          userSingletonModel.setDob(sharedPreferences.getString("dob", ""));
          userSingletonModel.setDoj(sharedPreferences.getString("doj", ""));
          userSingletonModel.setDate_of_rehiring(sharedPreferences.getString("date_of_rehiring", ""));

          userSingletonModel.setMobile_no(sharedPreferences.getString("mobile_no", ""));
          userSingletonModel.setHome_no(sharedPreferences.getString("home_no", ""));
          userSingletonModel.setEmergency_no(sharedPreferences.getString("emergency_no", ""));
          userSingletonModel.setAadhar_no(sharedPreferences.getString("aadhar_no", ""));
          userSingletonModel.setEmployee_pan_no(sharedPreferences.getString("pan_no", ""));
          userSingletonModel.setPassport_no(sharedPreferences.getString("passport_no", ""));
          userSingletonModel.setGender(sharedPreferences.getString("gender", ""));
                            //=============================================
          userSingletonModel.setPhysically_challenged(sharedPreferences.getString("physically_challenged", ""));
          userSingletonModel.setMarital_status(sharedPreferences.getString("marital_status", ""));
          userSingletonModel.setPresent_address_line1(sharedPreferences.getString("present_address_line1", ""));
          userSingletonModel.setPresent_address_line2(sharedPreferences.getString("present_address_line2", ""));

          userSingletonModel.setPresent_pin_no(sharedPreferences.getString("present_pin_no", ""));
          userSingletonModel.setPresent_country_id(sharedPreferences.getString("present_country_id", ""));
          userSingletonModel.setPresent_state_id(sharedPreferences.getString("present_state_id", ""));

          userSingletonModel.setSupervisor_1(sharedPreferences.getString("supervisor_1", ""));
          userSingletonModel.setSupervisor_2(sharedPreferences.getString("supervisor_2", ""));

          userSingletonModel.setPresent_district_name(sharedPreferences.getString("present_district_name", ""));
          userSingletonModel.setSame_as_present_address(sharedPreferences.getString("same_as_present_address", ""));
          userSingletonModel.setPermanent_address_line1(sharedPreferences.getString("Permanent_address_line1", ""));
          userSingletonModel.setPermanent_address_line2(sharedPreferences.getString("permanent_address_line2", ""));



          userSingletonModel.setPermanent_pin_no(sharedPreferences.getString("permanent_pin_no", ""));
          userSingletonModel.setPermanent_country_id(sharedPreferences.getString("permanent_country_id", ""));
          userSingletonModel.setPermanent_state_id(sharedPreferences.getString("permanent_state_id", ""));

          userSingletonModel.setPermanent_district_name(sharedPreferences.getString("permanent_district_name", ""));
          userSingletonModel.setPrevious_salary_gross(sharedPreferences.getString("previous_salary_gross", ""));
          userSingletonModel.setSalary_gross(sharedPreferences.getString("salary_gross", ""));
          userSingletonModel.setSalary_effective_date(sharedPreferences.getString("salary_effective_date", ""));
          userSingletonModel.setSalary_bank_account_no(sharedPreferences.getString("salary_bank_account_no", ""));
          userSingletonModel.setDelete_yn(sharedPreferences.getString("delete_yn", ""));
          userSingletonModel.setIs_active(sharedPreferences.getString("is_active", ""));


          userSingletonModel.setPersonal_email(sharedPreferences.getString("personal_email", ""));
          userSingletonModel.setOfficial_email(sharedPreferences.getString("official_email", ""));
          userSingletonModel.setLinkedin_id(sharedPreferences.getString("linkedin_id", ""));
          userSingletonModel.setEsi(sharedPreferences.getString("esi", ""));
          userSingletonModel.setPf(sharedPreferences.getString("pf", ""));
          userSingletonModel.setLeave_master_id(sharedPreferences.getString("leave_master_id", ""));
          userSingletonModel.setPayroll_setup_id(sharedPreferences.getString("payroll_setup_id", ""));


          userSingletonModel.setEmployee_pf_no(sharedPreferences.getString("employee_no_id", ""));
          userSingletonModel.setBank_name(sharedPreferences.getString("bank_name", ""));

          ////-------------------------------------------------

          userSingletonModel.setIfsc(sharedPreferences.getString("ifsc", ""));
          userSingletonModel.setBasic_salary(sharedPreferences.getString("basic_salary", ""));
          userSingletonModel.setEmp_password(sharedPreferences.getString("emp_password", ""));
          userSingletonModel.setGratuity_paid_date(sharedPreferences.getString("gratuity_paid", ""));
          userSingletonModel.setGratuity_paid_date(sharedPreferences.getString("gratuity_paid_date", ""));

          userSingletonModel.setTerminate(sharedPreferences.getString("terminate", ""));
          userSingletonModel.setTerminate_date(sharedPreferences.getString("terminate_date", ""));
          userSingletonModel.setBranch_office_id(sharedPreferences.getString("branch_office_id", ""));
          userSingletonModel.setBlood_group_id(sharedPreferences.getString("blood_group_id", ""));
          userSingletonModel.setQualification_id(sharedPreferences.getString("qualification_id", ""));



          ///============================================
          userSingletonModel.setUan_no(sharedPreferences.getString("uan_no", ""));
          userSingletonModel.setLte(sharedPreferences.getString("lte", ""));
          userSingletonModel.setDate_of_rehiring(sharedPreferences.getString("date_of_retire", ""));
          userSingletonModel.setCtc(sharedPreferences.getString("ctc", ""));
          userSingletonModel.setEmployee_status_name_id(sharedPreferences.getString("employee_status_name_id", ""));
          userSingletonModel.setGrade_name_id(sharedPreferences.getString("grade_name_id", ""));

          userSingletonModel.setCategory_name_id(sharedPreferences.getString("category_name_id", ""));
          userSingletonModel.setFather_husband_name(sharedPreferences.getString("father_husband_name", ""));
          userSingletonModel.setProbation_period_months(sharedPreferences.getString("probation_period_months", ""));
          userSingletonModel.setConfirmation_date(sharedPreferences.getString("confirmation_date", ""));
          userSingletonModel.setSub_department_name(sharedPreferences.getString("sub_department_name", ""));
          userSingletonModel.setPf_amount(sharedPreferences.getString("pf_amount", ""));
          userSingletonModel.setGratuity_amount(sharedPreferences.getString("gratuity_amount", ""));




          userSingletonModel.setSub_id(sharedPreferences.getString("sub_id", ""));
          userSingletonModel.setNominee_name(sharedPreferences.getString("nominee_name", ""));
          userSingletonModel.setReference_status(sharedPreferences.getString("reference_status", ""));
          userSingletonModel.setLta_eligible_emp(sharedPreferences.getString("lta_eligible_emp", ""));
          userSingletonModel.setFull_employee_name(sharedPreferences.getString("full_employee_name", ""));
          userSingletonModel.setVpf_yn(sharedPreferences.getString("vpf_yn", ""));
          userSingletonModel.setVpf_pnc(sharedPreferences.getString("vpf_pnc", ""));

          userSingletonModel.setP_tax(sharedPreferences.getString("p_tax", ""));
          userSingletonModel.setArchive_date(sharedPreferences.getString("archive_date", ""));
          userSingletonModel.setNominee_relation(sharedPreferences.getString("nominee_relation", ""));
          userSingletonModel.setSalary_on_appointment(sharedPreferences.getString("salary_on_appointment", ""));
          userSingletonModel.setConfirmation_basic(sharedPreferences.getString("confirmation_basic", ""));
          userSingletonModel.setCause_of_termination_of_service(sharedPreferences.getString("cause_of_termination_of_service", ""));
          userSingletonModel.setAmount_of_gratuity_claimed(sharedPreferences.getString("amount_of_gratuity_claimed", ""));


          userSingletonModel.setAmount_of_gratuit_paid(sharedPreferences.getString("amount_of_gratuit_paid", ""));
          userSingletonModel.setDate_of_payment_of_gratuity(sharedPreferences.getString("date_of_payment_of_gratuity", ""));
          userSingletonModel.setGratuity_witness1(sharedPreferences.getString("gratuity_witness1", ""));
          userSingletonModel.setGratuity_witness2(sharedPreferences.getString("gratuity_witness2", ""));
          userSingletonModel.setGratuity_remarks(sharedPreferences.getString("gratuity_remarks", ""));
          userSingletonModel.setEmployee_image(sharedPreferences.getString("employee_image", ""));
          userSingletonModel.setEsi_no(sharedPreferences.getString("Esi_no", ""));
          userSingletonModel.setSupervisor_1_name(sharedPreferences.getString("supervisor_1_name", ""));
          userSingletonModel.setSupervisor_2_name(sharedPreferences.getString("supervisor_2_name", ""));




          //-------------------


          userSingletonModel.setCompany_id(sharedPreferences.getString("company_id", ""));
          userSingletonModel.setCompany_code(sharedPreferences.getString("company_code", ""));
          userSingletonModel.setCompany_name(sharedPreferences.getString("company_name", ""));
          userSingletonModel.setCorporate_id(sharedPreferences.getString("corporate_id", ""));
          userSingletonModel.setLogo_path(sharedPreferences.getString("logo_path", ""));
          userSingletonModel.setActive_yn(sharedPreferences.getString("active_yn", ""));
          userSingletonModel.setAddress_line1(sharedPreferences.getString("address_line1", ""));

          userSingletonModel.setAddress_line2(sharedPreferences.getString("address_line2", ""));
          userSingletonModel.setState(sharedPreferences.getString("state", ""));
          userSingletonModel.setState_id(sharedPreferences.getString("state_id", ""));
          userSingletonModel.setCountry(sharedPreferences.getString("country", ""));
          userSingletonModel.setBusiness_type(sharedPreferences.getString("business_type", ""));
          userSingletonModel.setEmail(sharedPreferences.getString("email", ""));

          userSingletonModel.setPin(sharedPreferences.getString("pin", ""));
          userSingletonModel.setPhone(sharedPreferences.getString("phone", ""));
          userSingletonModel.setWebsite(sharedPreferences.getString("website", ""));
          userSingletonModel.setGst_no(sharedPreferences.getString("gst_no", ""));
          userSingletonModel.setCompany_pan_no(sharedPreferences.getString("pan_no", ""));
          userSingletonModel.setTan_no(sharedPreferences.getString("tan_no", ""));
          userSingletonModel.setCin_no(sharedPreferences.getString("cin_no", ""));
          userSingletonModel.setPtax_no(sharedPreferences.getString("ptax_no", ""));

          userSingletonModel.setCompany_pf_no(sharedPreferences.getString("pf_no", ""));
          userSingletonModel.setEsic_no(sharedPreferences.getString("esic_no", ""));
          userSingletonModel.setAttendance_with_selfie_yn(sharedPreferences.getString("attendance_with_selfie_yn",""));
          Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
          startActivity(intent);
      }
      if(!autofill.getString("username", "").isEmpty()){
          ed_userid.setText(autofill.getString("userid",null));
          ed_username.setText(autofill.getString("username",null));
      }

        //---------sharedpref code ends-----



      /*  savelogin=sharedPreferences.getBoolean("savelogin",true);

        {
            ed_userid.setText(sharedPreferences.getString("userid",null));
            ed_username.setText(sharedPreferences.getString("username",null));

        }
        savelogin=autofill.getBoolean("savelogin",true);

        {
            ed_userid.setText(autofill.getString("userid",null));
            ed_username.setText(autofill.getString("username",null));

        }*/
       //---code for file download/file access permission, starts (added on 30th July)
       /* ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);*/
        //---code for file download/file access permission, ends
        btn_login.setOnClickListener(this);
    }


        private  void getlogin(final String userid, final String username, final String password)
        {
            final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Authenticating", "Please wait while logging", false, false);
            String url= Url.BASEURL() + "login/"+userid+"/"+username+"/"+password;
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("data-=>",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject jb_response=jsonObject.getJSONObject("response");
                        String status=jb_response.getString("status");
                        String message=jb_response.getString("message");

                        if(status.equals("true")) {
                            loading.dismiss();

                            editor_autofill.putBoolean("savelogin", true);
                            editor_autofill.putString("userid",userid);
                            editor_autofill.putString("username", username);
                            editor_autofill.commit();

                            JSONObject jb_user=jsonObject.getJSONObject("user");
                            JSONObject jb_employee=jsonObject.getJSONObject("employee");
                            JSONObject jb_company=jsonObject.getJSONObject("company");

                            if (checked_sign.isChecked()) {
                                editor.putBoolean("savelogin", true);
                                editor.putString("userid",userid);
                                editor.putString("username", username);
                                editor.putString("user_id", jb_user.getString("user_id"));
                                editor.putString("user_type", jb_user.getString("user_type"));
                                editor.putString("fin_year_id", jb_user.getString("fin_year_id"));
                                //----------------------

                                editor.putString("employee_id", jb_employee.getString("employee_id"));
                                editor.putString("employee_code", jb_employee.getString("employee_code"));
                                editor.putString("employee_fname", jb_employee.getString("employee_fname"));
                                editor.putString("employee_mname", jb_employee.getString("employee_mname"));
                                editor.putString("employee_lname", jb_employee.getString("employee_lname"));
                                editor.putString("department_id", jb_employee.getString("department_id"));
                                editor.putString("designation_id", jb_employee.getString("designation_id"));
                                editor.putString("dob", jb_employee.getString("dob"));
                                editor.putString("doj", jb_employee.getString("doj"));
                                editor.putString("date_of_rehiring", jb_employee.getString("date_of_rehiring"));



                                editor.putString("mobile_no", jb_employee.getString("mobile_no"));
                                editor.putString("home_no", jb_employee.getString("home_no"));
                                editor.putString("emergency_no", jb_employee.getString("emergency_no"));
                                editor.putString("aadhar_no", jb_employee.getString("aadhar_no"));
                                editor.putString("pan_no", jb_employee.getString("pan_no"));
                                editor.putString("passport_no", jb_employee.getString("passport_no"));
                                editor.putString("gender", jb_employee.getString("gender"));
                                editor.putString("physically_challenged", jb_employee.getString("physically_challenged"));
                                editor.putString("marital_status", jb_employee.getString("marital_status"));
                                editor.putString("present_address_line1", jb_employee.getString("present_address_line1"));


                                editor.putString("present_address_line2", jb_employee.getString("present_address_line2"));
                                editor.putString("present_pin_no", jb_employee.getString("present_pin_no"));
                                editor.putString("present_country_id", jb_employee.getString("present_country_id"));
                                editor.putString("present_state_id", jb_employee.getString("present_state_id"));
                                editor.putString("supervisor_1", jb_employee.getString("supervisor_1"));
                                editor.putString("supervisor_2", jb_employee.getString("supervisor_2"));
                                editor.putString("present_district_name", jb_employee.getString("present_district_name"));
                                editor.putString("same_as_present_address", jb_employee.getString("same_as_present_address"));
                                editor.putString("permanent_address_line1", jb_employee.getString("permanent_address_line1"));
                                editor.putString("permanent_address_line2", jb_employee.getString("permanent_address_line2"));

                                editor.putString("permanent_pin_no", jb_employee.getString("permanent_pin_no"));
                                editor.putString("permanent_country_id", jb_employee.getString("permanent_country_id"));
                                editor.putString("permanent_state_id", jb_employee.getString("permanent_state_id"));
                                editor.putString("permanent_district_name", jb_employee.getString("permanent_district_name"));
                                editor.putString("previous_salary_gross", jb_employee.getString("previous_salary_gross"));
                                editor.putString("salary_gross", jb_employee.getString("salary_gross"));
                                editor.putString("salary_effective_date", jb_employee.getString("salary_effective_date"));
                                editor.putString("salary_bank_account_no", jb_employee.getString("salary_bank_account_no"));
                                editor.putString("delete_yn", jb_employee.getString("delete_yn"));
                                editor.putString("is_active", jb_employee.getString("is_active"));


                                editor.putString("personal_email", jb_employee.getString("personal_email"));
                                editor.putString("official_email", jb_employee.getString("official_email"));
                                editor.putString("linkedin_id", jb_employee.getString("linkedin_id"));
                                editor.putString("esi", jb_employee.getString("esi"));
                                editor.putString("pf", jb_employee.getString("pf"));
                                editor.putString("leave_master_id", jb_employee.getString("leave_master_id"));
                                editor.putString("payroll_setup_id", jb_employee.getString("payroll_setup_id"));
                                editor.putString("pf_no", jb_employee.getString("pf_no"));
                                editor.putString("uan_no", jb_employee.getString("uan_no"));
                                editor.putString("lte", jb_employee.getString("lte"));


                                editor.putString("date_of_retire", jb_employee.getString("date_of_retire"));
                                editor.putString("ctc", jb_employee.getString("ctc"));
                                editor.putString("employee_status_name_id", jb_employee.getString("employee_status_name_id"));
                                editor.putString("grade_name_id", jb_employee.getString("grade_name_id"));
                                editor.putString("category_name_id", jb_employee.getString("category_name_id"));
                                editor.putString("father_husband_name", jb_employee.getString("father_husband_name"));
                                editor.putString("probation_period_months", jb_employee.getString("probation_period_months"));
                                editor.putString("confirmation_date", jb_employee.getString("confirmation_date"));
                                editor.putString("sub_department_name", jb_employee.getString("sub_department_name"));
                                editor.putString("pf_amount", jb_employee.getString("pf_amount"));
                                editor.putString("gratuity_amount", jb_employee.getString("gratuity_amount"));




                                editor.putString("sub_id", jb_employee.getString("sub_id"));
                                editor.putString("nominee_name", jb_employee.getString("nominee_name"));
                                editor.putString("reference_status", jb_employee.getString("reference_status"));
                                editor.putString("lta_eligible_emp", jb_employee.getString("lta_eligible_emp"));
                                editor.putString("full_employee_name", jb_employee.getString("full_employee_name"));
                                editor.putString("vpf_yn", jb_employee.getString("vpf_yn"));
                                editor.putString("vpf_pnc", jb_employee.getString("vpf_pnc"));
                                editor.putString("p_tax", jb_employee.getString("p_tax"));
                                editor.putString("archive_date", jb_employee.getString("archive_date"));
                                editor.putString("nominee_relation", jb_employee.getString("nominee_relation"));
                                editor.putString("branch_office_id", jb_employee.getString("branch_office_id"));


                                editor.putString("salary_on_appointment", jb_employee.getString("salary_on_appointment"));
                                editor.putString("confirmation_basic", jb_employee.getString("confirmation_basic"));
                                editor.putString("cause_of_termination_of_service", jb_employee.getString("cause_of_termination_of_service"));
                                editor.putString("amount_of_gratuity_claimed", jb_employee.getString("amount_of_gratuity_claimed"));
                                editor.putString("amount_of_gratuit_paid", jb_employee.getString("amount_of_gratuit_paid"));
                                editor.putString("date_of_payment_of_gratuity", jb_employee.getString("date_of_payment_of_gratuity"));
                                editor.putString("gratuity_witness1", jb_employee.getString("gratuity_witness1"));
                                editor.putString("gratuity_witness2", jb_employee.getString("gratuity_witness2"));
                                editor.putString("gratuity_remarks", jb_employee.getString("gratuity_remarks"));
                                editor.putString("employee_image", jb_employee.getString("employee_image"));
                                editor.putString("esi_no", jb_employee.getString("esi_no"));
                                editor.putString("supervisor_1_name", jb_employee.getString("supervisor_1_name"));
                                editor.putString("supervisor_2_name", jb_employee.getString("supervisor_2_name"));


                                //===================//

                                editor.putString("company_id", jb_company.getString("company_id"));
                                editor.putString("company_code", jb_company.getString("company_code"));
                                editor.putString("company_name", jb_company.getString("company_name"));
                                editor.putString("corporate_id", jb_company.getString("corporate_id"));
                                editor.putString("logo_path", jb_company.getString("logo_path"));
                                editor.putString("active_yn", jb_company.getString("active_yn"));
                                editor.putString("address_line1", jb_company.getString("address_line1"));
                                editor.putString("address_line2", jb_company.getString("address_line2"));


                                editor.putString("state", jb_company.getString("state"));
                                editor.putString("state_id", jb_company.getString("state_id"));
                                editor.putString("country", jb_company.getString("country"));
                                editor.putString("pin", jb_company.getString("pin"));
                                editor.putString("business_type", jb_company.getString("business_type"));
                                editor.putString("email", jb_company.getString("email"));
                                editor.putString("phone", jb_company.getString("phone"));
                                editor.putString("website", jb_company.getString("website"));

                                editor.putString("gst_no", jb_company.getString("gst_no"));
                                editor.putString("pan_no", jb_company.getString("pan_no"));
                                editor.putString("tan_no", jb_company.getString("tan_no"));
                                editor.putString("cin_no", jb_company.getString("cin_no"));
                                editor.putString("ptax_no", jb_company.getString("ptax_no"));
                                editor.putString("pf_no", jb_company.getString("pf_no"));
                                editor.putString("esic_no", jb_company.getString("esic_no"));
                                editor.putString("attendance_with_selfie_yn", jb_company.getString("attendance_with_selfie_yn"));



                                //editor.putString("password", password);
                                editor.commit();

                            }



                            userSingletonModel.setUser_id(jb_user.getString("user_id"));
                            userSingletonModel.setUser_type(jb_user.getString("user_type"));
                            userSingletonModel.setFin_year_id(jb_user.getString("fin_year_id"));
                            userSingletonModel.setBranch_office_id(jb_employee.getString("branch_office_id"));


                            userSingletonModel.setEmployee_id(jb_employee.getString("employee_id"));
                            userSingletonModel.setEmployee_code(jb_employee.getString("employee_code"));
                            Log.d("test-=>",userSingletonModel.getEmployee_code());
                            userSingletonModel.setEmployee_fname(jb_employee.getString("employee_fname"));
                            userSingletonModel.setEmployee_mname(jb_employee.getString("employee_mname"));
                            userSingletonModel.setEmployee_lname(jb_employee.getString("employee_lname"));

                            userSingletonModel.setDepartment_id(jb_employee.getString("department_id"));
                            userSingletonModel.setDesignation_id(jb_employee.getString("designation_id"));
                            userSingletonModel.setDob(jb_employee.getString("dob"));
                            userSingletonModel.setDoj(jb_employee.getString("doj"));
                            userSingletonModel.setDate_of_rehiring(jb_employee.getString("date_of_rehiring"));

                            userSingletonModel.setMobile_no(jb_employee.getString("mobile_no"));
                            userSingletonModel.setHome_no(jb_employee.getString("home_no"));
                            userSingletonModel.setEmergency_no(jb_employee.getString("emergency_no"));
                            userSingletonModel.setAadhar_no(jb_employee.getString("aadhar_no"));
                            userSingletonModel.setEmployee_pan_no(jb_employee.getString("pan_no"));
                            userSingletonModel.setPassport_no(jb_employee.getString("passport_no"));
                            userSingletonModel.setGender(jb_employee.getString("gender"));

                            userSingletonModel.setPhysically_challenged(jb_employee.getString("physically_challenged"));
                            userSingletonModel.setMarital_status(jb_employee.getString("marital_status"));
                            userSingletonModel.setPresent_address_line1(jb_employee.getString("present_address_line1"));
                            userSingletonModel.setPresent_address_line2(jb_employee.getString("present_address_line2"));

                            userSingletonModel.setPresent_pin_no(jb_employee.getString("present_pin_no"));
                            userSingletonModel.setPresent_country_id(jb_employee.getString("present_country_id"));
                            userSingletonModel.setPresent_state_id(jb_employee.getString("present_state_id"));

                            userSingletonModel.setSupervisor_1(jb_employee.getString("supervisor_1"));
                            userSingletonModel.setSupervisor_2(jb_employee.getString("supervisor_2"));

                            userSingletonModel.setPresent_district_name(jb_employee.getString("present_district_name"));
                            userSingletonModel.setSame_as_present_address(jb_employee.getString("same_as_present_address"));
                            userSingletonModel.setPermanent_address_line1(jb_employee.getString("permanent_address_line1"));
                            userSingletonModel.setPermanent_address_line2(jb_employee.getString("permanent_address_line2"));



                            userSingletonModel.setPermanent_pin_no(jb_employee.getString("permanent_pin_no"));
                            userSingletonModel.setPermanent_country_id(jb_employee.getString("permanent_country_id"));
                            userSingletonModel.setPermanent_state_id(jb_employee.getString("permanent_state_id"));

                            userSingletonModel.setPermanent_district_name(jb_employee.getString("permanent_district_name"));
                            userSingletonModel.setPrevious_salary_gross(jb_employee.getString("previous_salary_gross"));
                            userSingletonModel.setSalary_gross(jb_employee.getString("salary_gross"));
                            userSingletonModel.setSalary_effective_date(jb_employee.getString("salary_effective_date"));
                            userSingletonModel.setSalary_bank_account_no(jb_employee.getString("salary_bank_account_no"));
                            userSingletonModel.setDelete_yn(jb_employee.getString("delete_yn"));
                            userSingletonModel.setIs_active(jb_employee.getString("is_active"));




                            userSingletonModel.setPersonal_email(jb_employee.getString("personal_email"));
                            userSingletonModel.setOfficial_email(jb_employee.getString("official_email"));
                            userSingletonModel.setLinkedin_id(jb_employee.getString("linkedin_id"));
                            userSingletonModel.setEsi(jb_employee.getString("esi"));
                            userSingletonModel.setPf(jb_employee.getString("pf"));
                            userSingletonModel.setLeave_master_id(jb_employee.getString("leave_master_id"));
                            userSingletonModel.setPayroll_setup_id(jb_employee.getString("payroll_setup_id"));


                            userSingletonModel.setEmployee_pf_no(jb_employee.getString("pf_no"));
                            userSingletonModel.setUan_no(jb_employee.getString("uan_no"));
                            userSingletonModel.setLte(jb_employee.getString("lte"));
                            userSingletonModel.setDate_of_rehiring(jb_employee.getString("date_of_retire"));
                            userSingletonModel.setCtc(jb_employee.getString("ctc"));
                            userSingletonModel.setEmployee_status_name_id(jb_employee.getString("employee_status_name_id"));
                            userSingletonModel.setGrade_name_id(jb_employee.getString("grade_name_id"));

                            userSingletonModel.setCategory_name_id(jb_employee.getString("category_name_id"));
                            userSingletonModel.setFather_husband_name(jb_employee.getString("father_husband_name"));
                            userSingletonModel.setProbation_period_months(jb_employee.getString("probation_period_months"));
                            userSingletonModel.setConfirmation_date(jb_employee.getString("confirmation_date"));
                            userSingletonModel.setSub_department_name(jb_employee.getString("sub_department_name"));
                            userSingletonModel.setPf_amount(jb_employee.getString("pf_amount"));
                            userSingletonModel.setGratuity_amount(jb_employee.getString("gratuity_amount"));




                            userSingletonModel.setSub_id(jb_employee.getString("sub_id"));
                            userSingletonModel.setNominee_name(jb_employee.getString("nominee_name"));
                            userSingletonModel.setReference_status(jb_employee.getString("reference_status"));
                            userSingletonModel.setLta_eligible_emp(jb_employee.getString("lta_eligible_emp"));
                            userSingletonModel.setFull_employee_name(jb_employee.getString("full_employee_name"));
                            userSingletonModel.setVpf_yn(jb_employee.getString("vpf_yn"));
                            userSingletonModel.setVpf_pnc(jb_employee.getString("vpf_pnc"));

                            userSingletonModel.setP_tax(jb_employee.getString("p_tax"));
                            userSingletonModel.setArchive_date(jb_employee.getString("archive_date"));
                            userSingletonModel.setNominee_relation(jb_employee.getString("nominee_relation"));
                            userSingletonModel.setSalary_on_appointment(jb_employee.getString("salary_on_appointment"));
                            userSingletonModel.setConfirmation_basic(jb_employee.getString("confirmation_basic"));
                            userSingletonModel.setCause_of_termination_of_service(jb_employee.getString("cause_of_termination_of_service"));
                            userSingletonModel.setAmount_of_gratuity_claimed(jb_employee.getString("amount_of_gratuity_claimed"));


                            userSingletonModel.setAmount_of_gratuit_paid(jb_employee.getString("amount_of_gratuit_paid"));
                            userSingletonModel.setDate_of_payment_of_gratuity(jb_employee.getString("date_of_payment_of_gratuity"));
                            userSingletonModel.setGratuity_witness1(jb_employee.getString("gratuity_witness1"));
                            userSingletonModel.setGratuity_witness2(jb_employee.getString("gratuity_witness2"));
                            userSingletonModel.setGratuity_remarks(jb_employee.getString("gratuity_remarks"));
                            userSingletonModel.setEmployee_image(jb_employee.getString("employee_image"));
                            userSingletonModel.setEsi_no(jb_employee.getString("esi_no"));
                            userSingletonModel.setSupervisor_1_name(jb_employee.getString("supervisor_1_name"));
                            userSingletonModel.setSupervisor_2_name(jb_employee.getString("supervisor_2_name"));




                            //-------------------


                            userSingletonModel.setCompany_id(jb_company.getString("company_id"));
                            userSingletonModel.setCompany_code(jb_company.getString("company_code"));
                            userSingletonModel.setCompany_name(jb_company.getString("company_name"));
                            userSingletonModel.setCorporate_id(jb_company.getString("corporate_id"));
                            userSingletonModel.setLogo_path(jb_company.getString("logo_path"));
                            userSingletonModel.setActive_yn(jb_company.getString("active_yn"));
                            userSingletonModel.setAddress_line1(jb_company.getString("address_line1"));

                            userSingletonModel.setAddress_line2(jb_company.getString("address_line2"));
                            userSingletonModel.setState(jb_company.getString("state"));
                            userSingletonModel.setState_id(jb_company.getString("state_id"));
                            userSingletonModel.setGratuity_witness2(jb_company.getString("country"));
                            userSingletonModel.setCountry(jb_company.getString("pin"));
                            userSingletonModel.setBusiness_type(jb_company.getString("business_type"));
                            userSingletonModel.setEmail(jb_company.getString("email"));


                            userSingletonModel.setPhone(jb_company.getString("phone"));
                            userSingletonModel.setWebsite(jb_company.getString("website"));
                            userSingletonModel.setGst_no(jb_company.getString("gst_no"));
                            userSingletonModel.setCompany_pan_no(jb_company.getString("pan_no"));
                            userSingletonModel.setTan_no(jb_company.getString("tan_no"));
                            userSingletonModel.setCin_no(jb_company.getString("cin_no"));
                            userSingletonModel.setPtax_no(jb_company.getString("ptax_no"));

                            userSingletonModel.setCompany_pf_no(jb_company.getString("pf_no"));
                            userSingletonModel.setEsic_no(jb_company.getString("esic_no"));
                            userSingletonModel.setAttendance_with_selfie_yn(jb_company.getString("attendance_with_selfie_yn"));


                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
//                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loading.dismiss();
                            Snackbar snackbar=Snackbar.make(coordinatorLayout,"Login failure",Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            btn_login.setEnabled(true);
                            btn_login.setClickable(true);
                            btn_login.setAlpha(1.0f);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        btn_login.setEnabled(true);
                        btn_login.setClickable(true);
                        btn_login.setAlpha(1.0f);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d("error-=>",error.getMessage());
                    loading.dismiss();
                    btn_login.setEnabled(true);
                    btn_login.setClickable(true);
                    btn_login.setAlpha(1.0f);
                    Toast.makeText(LoginActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String>map=new HashMap<>();
                    map.put("corp id",userid);
                    map.put("user id",username);
                    map.put("password",password);
                    return map;
                }
            };
                    Volley.newRequestQueue(LoginActivity.this).add(stringRequest);

        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String userid=ed_userid.getText().toString();
                String username=ed_username.getText().toString();
                String password=ed_userpassword.getText().toString();

                if(userid.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    if (userid.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Userid is required", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }
                    if (username.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Username is required", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }
                    if (password.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Password  is required", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }
                }else {


                    btn_login.setEnabled(false);
                    btn_login.setClickable(false);
                    btn_login.setAlpha(0.4f);
                    getlogin(userid, username, password);
                    entry_user = username;//---added by Satabhisha on 6th MAy
                }
                break;
                default:
                    break;
        }
    }

    //========following function is to resign keyboard on touching anywhere in the screen
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    //===========Code to enable gps, starts(added on 6th May)=========
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //-----------code to check location permission, code starts(added on 25th nov)---------
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }else if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

//                finish();
            }

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable your GPS")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        startActivity(new Intent(HomeActivity.this, FingerprintActivity.class));
                    }
                });
                /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });*/
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //===========Code to enable gps, ends(added on 6th May)=========


    //---verson upgrade code starts----
    @Override
    public boolean onVersionUpdate(int newVersion, int oldVersion) {
        return true;
    }
    //---verson upgrade code ends----

    //---code for file download/file access permission, starts (added on 20-o7-2021)
   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
    //---code for file download/file access permission, ends
}




