package org.wrkplan.payroll.Model;

public class UserSingletonModel {
    private static final UserSingletonModel userSingletonModel = new UserSingletonModel();
    public static UserSingletonModel getInstance() {
        return userSingletonModel;
    }
    private UserSingletonModel() { }

    //----login user variables
    public String fin_year_id;
    //----employee variables
    public  String user_id,user_type,employee_id,employee_code,employee_fname,employee_mname,employee_lname,department_id,
            designation_id,dob,doj,date_of_rehiring,mobile_no,home_no,emergency_no,aadhar_no,employee_pan_no,passport_no,gender,
            physically_challenged,marital_status,present_address_line1,present_address_line2,present_pin_no,present_state_id,category_name_id,
            present_country_id,present_district_name,same_as_present_address,permanent_address_line1,permanent_address_line2,
            permanent_pin_no,permanent_country_id,permanent_state_id,permanent_district_name,previous_salary_gross,salary_gross,
            salary_effective_date,salary_bank_account_no,delete_yn,is_active,personal_email,official_email,linkedin_id,esi,pf,grade_name_id,
            leave_master_id,payroll_setup_id,employee_no_id,bank_name,ifsc,basic_salary,emp_password,supervisor_1,supervisor_2,
            gratuity_paid,gratuity_paid_date,terminate,terminate_date,branch_office_id,blood_group_id,qualification_id,employee_pf_no,
            uan_no,lte,date_of_retire,ctc,employee_status_name_id,grade_name_idcategory_name_id,father_husband_name,probation_period_months,
            confirmation_date,sub_department_name,pf_amount,gratuity_amount,sub_id,nominee_name,reference_status,lta_eligible_emp,
            full_employee_name,vpf_yn,vpf_pnc,p_tax,archive_date,nominee_relation,salary_on_appointment,confirmation_basic,
            cause_of_termination_of_service,amount_of_gratuity_claimed,amount_of_gratuit_paid,date_of_payment_of_gratuity,gratuity_witness1,
            gratuity_witness2,gratuity_remarks,employee_image,esi_no,supervisor_1_name,supervisor_2_name;

    //----company variables
    public  String company_id,company_code,company_name,corporate_id,logo_path,active_yn,address_line1,address_line2,state,
            state_id,country,pin,business_type,email,phone,website,gst_no,company_pan_no,tan_no,cin_no,ptax_no,company_pf_no,esic_no, attendance_with_selfie_yn;

    //-------policies variables...
  //  public String provider_name,no,holder_name,holder_relationship,type,amount,expiry_date;

public  String casual_leave,earn_leave,sick_leave,comp_off,maternal_leave,paternal_leave;

   //-----log_view/task variables
    String log_task_employee_name, log_task_date, log_employee_id;

    //---------Getter method starts-----------------

    public static UserSingletonModel getUserSingletonModel() {
        return userSingletonModel;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }


    public String getFin_year_id() {
        return fin_year_id;
    }

    public String getSupervisor_1_name() {
        return supervisor_1_name;
    }

    public String getSupervisor_2_name() {
        return supervisor_2_name;
    }

    public String getCasual_leave() {
        return casual_leave;
    }

    public String getEarn_leave() {
        return earn_leave;
    }

    public String getSick_leave() {
        return sick_leave;
    }

    public String getComp_off() {
        return comp_off;
    }

    public String getMaternal_leave() {
        return maternal_leave;
    }

    public String getPaternal_leave() {
        return paternal_leave;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getEmployee_code() {
        return employee_code;
    }

    public String getEmployee_fname() {
        return employee_fname;
    }

    public String getCategory_name_id() {
        return category_name_id;
    }

    public String getGrade_name_id() {
        return grade_name_id;
    }

    public String getEmployee_mname() {
        return employee_mname;
    }

    public String getPresent_state_id() {
        return present_state_id;
    }

    public String getEmployee_lname() {
        return employee_lname;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public String getDob() {
        return dob;
    }

    public String getDoj() {
        return doj;
    }

    public String getDate_of_rehiring() {
        return date_of_rehiring;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getHome_no() {
        return home_no;
    }

    public String getEmergency_no() {
        return emergency_no;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public String getEmployee_pan_no() {
        return employee_pan_no;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public String getGender() {
        return gender;
    }

    public String getPhysically_challenged() {
        return physically_challenged;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public String getPresent_address_line1() {
        return present_address_line1;
    }

    public String getPresent_address_line2() {
        return present_address_line2;
    }

    public String getPresent_pin_no() {
        return present_pin_no;
    }

    public String getPresent_country_id() {
        return present_country_id;
    }

    public String getPresent_district_name() {
        return present_district_name;
    }

    public String getSame_as_present_address() {
        return same_as_present_address;
    }

    public String getPermanent_address_line1() {
        return permanent_address_line1;
    }

    public String getPermanent_address_line2() {
        return permanent_address_line2;
    }

    public String getPermanent_pin_no() {
        return permanent_pin_no;
    }

    public String getPermanent_country_id() {
        return permanent_country_id;
    }

    public String getPermanent_state_id() {
        return permanent_state_id;
    }

    public String getPermanent_district_name() {
        return permanent_district_name;
    }

    public String getPrevious_salary_gross() {
        return previous_salary_gross;
    }

    public String getSalary_gross() {
        return salary_gross;
    }

    public String getSalary_effective_date() {
        return salary_effective_date;
    }

    public String getSalary_bank_account_no() {
        return salary_bank_account_no;
    }

    public String getDelete_yn() {
        return delete_yn;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getPersonal_email() {
        return personal_email;
    }

    public String getOfficial_email() {
        return official_email;
    }

    public String getLinkedin_id() {
        return linkedin_id;
    }

    public String getEsi() {
        return esi;
    }

    public String getPf() {
        return pf;
    }

    public String getLeave_master_id() {
        return leave_master_id;
    }

    public String getPayroll_setup_id() {
        return payroll_setup_id;
    }

    public String getEmployee_no_id() {
        return employee_no_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getBasic_salary() {
        return basic_salary;
    }

    public String getEmp_password() {
        return emp_password;
    }

    public String getSupervisor_1() {
        return supervisor_1;
    }

    public String getSupervisor_2() {
        return supervisor_2;
    }

    public String getGratuity_paid() {
        return gratuity_paid;
    }

    public String getGratuity_paid_date() {
        return gratuity_paid_date;
    }

    public String getTerminate() {
        return terminate;
    }

    public String getTerminate_date() {
        return terminate_date;
    }

    public String getBranch_office_id() {
        return branch_office_id;
    }

    public String getBlood_group_id() {
        return blood_group_id;
    }

    public String getQualification_id() {
        return qualification_id;
    }

    public String getEmployee_pf_no() {
        return employee_pf_no;
    }

    public String getUan_no() {
        return uan_no;
    }

    public String getLte() {
        return lte;
    }

    public String getDate_of_retire() {
        return date_of_retire;
    }

    public String getCtc() {
        return ctc;
    }

    public String getEmployee_status_name_id() {
        return employee_status_name_id;
    }

    public String getGrade_name_idcategory_name_id() {
        return grade_name_idcategory_name_id;
    }

    public String getFather_husband_name() {
        return father_husband_name;
    }

    public String getProbation_period_months() {
        return probation_period_months;
    }

    public String getConfirmation_date() {
        return confirmation_date;
    }

    public String getSub_department_name() {
        return sub_department_name;
    }

    public String getPf_amount() {
        return pf_amount;
    }

    public String getGratuity_amount() {
        return gratuity_amount;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getNominee_name() {
        return nominee_name;
    }

    public String getReference_status() {
        return reference_status;
    }

    public String getLta_eligible_emp() {
        return lta_eligible_emp;
    }

    public String getFull_employee_name() {
        return full_employee_name;
    }

    public String getVpf_yn() {
        return vpf_yn;
    }

    public String getVpf_pnc() {
        return vpf_pnc;
    }

    public String getP_tax() {
        return p_tax;
    }

    public String getArchive_date() {
        return archive_date;
    }

    public String getNominee_relation() {
        return nominee_relation;
    }

    public String getSalary_on_appointment() {
        return salary_on_appointment;
    }

    public String getConfirmation_basic() {
        return confirmation_basic;
    }

    public String getCause_of_termination_of_service() {
        return cause_of_termination_of_service;
    }

    public String getAmount_of_gratuity_claimed() {
        return amount_of_gratuity_claimed;
    }

    public String getAmount_of_gratuit_paid() {
        return amount_of_gratuit_paid;
    }

    public String getDate_of_payment_of_gratuity() {
        return date_of_payment_of_gratuity;
    }

    public String getGratuity_witness1() {
        return gratuity_witness1;
    }

    public String getGratuity_witness2() {
        return gratuity_witness2;
    }

    public String getGratuity_remarks() {
        return gratuity_remarks;
    }

    public String getEmployee_image() {
        return employee_image;
    }

    public String getEsi_no() {
        return esi_no;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getCompany_code() {
        return company_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCorporate_id() {
        return corporate_id;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public String getActive_yn() {
        return active_yn;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public String getState() {
        return state;
    }

    public String getState_id() {
        return state_id;
    }

    public String getCountry() {
        return country;
    }

    public String getPin() {
        return pin;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getGst_no() {
        return gst_no;
    }

    public String getCompany_pan_no() {
        return company_pan_no;
    }

    public String getTan_no() {
        return tan_no;
    }

    public String getCin_no() {
        return cin_no;
    }

    public String getPtax_no() {
        return ptax_no;
    }

    public String getCompany_pf_no() {
        return company_pf_no;
    }

    public String getEsic_no() {
        return esic_no;
    }

    public String getLog_task_employee_name() {
        return log_task_employee_name;
    }

    public String getLog_task_date() {
        return log_task_date;
    }

    public String getLog_employee_id() {
        return log_employee_id;
    }

    public String getAttendance_with_selfie_yn() {
        return attendance_with_selfie_yn;
    }

    //---------Getter method ends-----------------

    //---------setter method starts-----------------

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public void setEmployee_code(String employee_code) {
        this.employee_code = employee_code;
    }

    public void setEmployee_fname(String employee_fname) {
        this.employee_fname = employee_fname;
    }

    public void setEmployee_mname(String employee_mname) {
        this.employee_mname = employee_mname;
    }

    public void setGrade_name_id(String grade_name_id) {
        this.grade_name_id = grade_name_id;
    }

    public void setEmployee_lname(String employee_lname) {
        this.employee_lname = employee_lname;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public void setCategory_name_id(String category_name_id) {
        this.category_name_id = category_name_id;
    }

    public void setSupervisor_1_name(String supervisor_1_name) {
        this.supervisor_1_name = supervisor_1_name;
    }

    public void setSupervisor_2_name(String supervisor_2_name) {
        this.supervisor_2_name = supervisor_2_name;
    }

    public void setDate_of_rehiring(String date_of_rehiring) {
        this.date_of_rehiring = date_of_rehiring;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public void setHome_no(String home_no) {
        this.home_no = home_no;
    }

    public void setEmergency_no(String emergency_no) {
        this.emergency_no = emergency_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public void setEmployee_pan_no(String employee_pan_no) {
        this.employee_pan_no = employee_pan_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public void setCasual_leave(String casual_leave) {
        this.casual_leave = casual_leave;
    }

    public void setEarn_leave(String earn_leave) {
        this.earn_leave = earn_leave;
    }

    public void setSick_leave(String sick_leave) {
        this.sick_leave = sick_leave;
    }

    public void setComp_off(String comp_off) {
        this.comp_off = comp_off;
    }

    public void setMaternal_leave(String maternal_leave) {
        this.maternal_leave = maternal_leave;
    }

    public void setPaternal_leave(String paternal_leave) {
        this.paternal_leave = paternal_leave;
    }

    public void setPresent_state_id(String present_state_id) {
        this.present_state_id = present_state_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public void setPhysically_challenged(String physically_challenged) {
        this.physically_challenged = physically_challenged;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public void setPresent_address_line1(String present_address_line1) {
        this.present_address_line1 = present_address_line1;
    }

    public void setPresent_address_line2(String present_address_line2) {
        this.present_address_line2 = present_address_line2;
    }

    public void setPresent_pin_no(String present_pin_no) {
        this.present_pin_no = present_pin_no;
    }

    public void setPresent_country_id(String present_country_id) {
        this.present_country_id = present_country_id;
    }

    public void setPresent_district_name(String present_district_name) {
        this.present_district_name = present_district_name;
    }

    public void setSame_as_present_address(String same_as_present_address) {
        this.same_as_present_address = same_as_present_address;
    }

    public void setPermanent_address_line1(String permanent_address_line1) {
        this.permanent_address_line1 = permanent_address_line1;
    }

    public void setPermanent_address_line2(String permanent_address_line2) {
        this.permanent_address_line2 = permanent_address_line2;
    }

    public void setPermanent_pin_no(String permanent_pin_no) {
        this.permanent_pin_no = permanent_pin_no;
    }

    public void setPermanent_country_id(String permanent_country_id) {
        this.permanent_country_id = permanent_country_id;
    }

    public void setPermanent_state_id(String permanent_state_id) {
        this.permanent_state_id = permanent_state_id;
    }

    public void setPermanent_district_name(String permanent_district_name) {
        this.permanent_district_name = permanent_district_name;
    }

    public void setPrevious_salary_gross(String previous_salary_gross) {
        this.previous_salary_gross = previous_salary_gross;
    }

    public void setSalary_gross(String salary_gross) {
        this.salary_gross = salary_gross;
    }

    public void setSalary_effective_date(String salary_effective_date) {
        this.salary_effective_date = salary_effective_date;
    }

    public void setSalary_bank_account_no(String salary_bank_account_no) {
        this.salary_bank_account_no = salary_bank_account_no;
    }

    public void setDelete_yn(String delete_yn) {
        this.delete_yn = delete_yn;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }

    public void setOfficial_email(String official_email) {
        this.official_email = official_email;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedin_id = linkedin_id;
    }

    public void setEsi(String esi) {
        this.esi = esi;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public void setLeave_master_id(String leave_master_id) {
        this.leave_master_id = leave_master_id;
    }

    public void setPayroll_setup_id(String payroll_setup_id) {
        this.payroll_setup_id = payroll_setup_id;
    }

    public void setEmployee_no_id(String employee_no_id) {
        this.employee_no_id = employee_no_id;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public void setBasic_salary(String basic_salary) {
        this.basic_salary = basic_salary;
    }

    public void setEmp_password(String emp_password) {
        this.emp_password = emp_password;
    }

    public void setSupervisor_1(String supervisor_1) {
        this.supervisor_1 = supervisor_1;
    }

    public void setSupervisor_2(String supervisor_2) {
        this.supervisor_2 = supervisor_2;
    }

    public void setGratuity_paid(String gratuity_paid) {
        this.gratuity_paid = gratuity_paid;
    }

    public void setGratuity_paid_date(String gratuity_paid_date) {
        this.gratuity_paid_date = gratuity_paid_date;
    }

    public void setTerminate(String terminate) {
        this.terminate = terminate;
    }

    public void setTerminate_date(String terminate_date) {
        this.terminate_date = terminate_date;
    }

    public void setBranch_office_id(String branch_office_id) {
        this.branch_office_id = branch_office_id;
    }

    public void setBlood_group_id(String blood_group_id) {
        this.blood_group_id = blood_group_id;
    }

    public void setQualification_id(String qualification_id) {
        this.qualification_id = qualification_id;
    }

    public void setEmployee_pf_no(String employee_pf_no) {
        this.employee_pf_no = employee_pf_no;
    }

    public void setUan_no(String uan_no) {
        this.uan_no = uan_no;
    }

    public void setLte(String lte) {
        this.lte = lte;
    }

    public void setDate_of_retire(String date_of_retire) {
        this.date_of_retire = date_of_retire;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public void setEmployee_status_name_id(String employee_status_name_id) {
        this.employee_status_name_id = employee_status_name_id;
    }

    public void setGrade_name_idcategory_name_id(String grade_name_idcategory_name_id) {
        this.grade_name_idcategory_name_id = grade_name_idcategory_name_id;
    }

    public void setFather_husband_name(String father_husband_name) {
        this.father_husband_name = father_husband_name;
    }

    public void setProbation_period_months(String probation_period_months) {
        this.probation_period_months = probation_period_months;
    }

    public void setConfirmation_date(String confirmation_date) {
        this.confirmation_date = confirmation_date;
    }

    public void setSub_department_name(String sub_department_name) {
        this.sub_department_name = sub_department_name;
    }

    public void setPf_amount(String pf_amount) {
        this.pf_amount = pf_amount;
    }

    public void setGratuity_amount(String gratuity_amount) {
        this.gratuity_amount = gratuity_amount;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public void setNominee_name(String nominee_name) {
        this.nominee_name = nominee_name;
    }

    public void setReference_status(String reference_status) {
        this.reference_status = reference_status;
    }

    public void setLta_eligible_emp(String lta_eligible_emp) {
        this.lta_eligible_emp = lta_eligible_emp;
    }

    public void setFull_employee_name(String full_employee_name) {
        this.full_employee_name = full_employee_name;
    }

    public void setVpf_yn(String vpf_yn) {
        this.vpf_yn = vpf_yn;
    }

    public void setVpf_pnc(String vpf_pnc) {
        this.vpf_pnc = vpf_pnc;
    }

    public void setP_tax(String p_tax) {
        this.p_tax = p_tax;
    }

    public void setArchive_date(String archive_date) {
        this.archive_date = archive_date;
    }

    public void setNominee_relation(String nominee_relation) {
        this.nominee_relation = nominee_relation;
    }

    public void setSalary_on_appointment(String salary_on_appointment) {
        this.salary_on_appointment = salary_on_appointment;
    }

    public void setConfirmation_basic(String confirmation_basic) {
        this.confirmation_basic = confirmation_basic;
    }

    public void setCause_of_termination_of_service(String cause_of_termination_of_service) {
        this.cause_of_termination_of_service = cause_of_termination_of_service;
    }

    public void setAmount_of_gratuity_claimed(String amount_of_gratuity_claimed) {
        this.amount_of_gratuity_claimed = amount_of_gratuity_claimed;
    }

    public void setAmount_of_gratuit_paid(String amount_of_gratuit_paid) {
        this.amount_of_gratuit_paid = amount_of_gratuit_paid;
    }

    public void setDate_of_payment_of_gratuity(String date_of_payment_of_gratuity) {
        this.date_of_payment_of_gratuity = date_of_payment_of_gratuity;
    }

    public void setGratuity_witness1(String gratuity_witness1) {
        this.gratuity_witness1 = gratuity_witness1;
    }

    public void setGratuity_witness2(String gratuity_witness2) {
        this.gratuity_witness2 = gratuity_witness2;
    }

    public void setGratuity_remarks(String gratuity_remarks) {
        this.gratuity_remarks = gratuity_remarks;
    }

    public void setEmployee_image(String employee_image) {
        this.employee_image = employee_image;
    }

    public void setEsi_no(String esi_no) {
        this.esi_no = esi_no;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setCorporate_id(String corporate_id) {
        this.corporate_id = corporate_id;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public void setActive_yn(String active_yn) {
        this.active_yn = active_yn;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public void setCompany_pan_no(String company_pan_no) {
        this.company_pan_no = company_pan_no;
    }

    public void setTan_no(String tan_no) {
        this.tan_no = tan_no;
    }

    public void setCin_no(String cin_no) {
        this.cin_no = cin_no;
    }

    public void setPtax_no(String ptax_no) {
        this.ptax_no = ptax_no;
    }

    public void setCompany_pf_no(String company_pf_no) {
        this.company_pf_no = company_pf_no;
    }

    public void setEsic_no(String esic_no) {
        this.esic_no = esic_no;
    }

    public void setLog_task_employee_name(String log_task_employee_name) {
        this.log_task_employee_name = log_task_employee_name;
    }

    public void setLog_task_date(String log_task_date) {
        this.log_task_date = log_task_date;
    }

    public void setLog_employee_id(String log_employee_id) {
        this.log_employee_id = log_employee_id;
    }

    public void setAttendance_with_selfie_yn(String attendance_with_selfie_yn) {
        this.attendance_with_selfie_yn = attendance_with_selfie_yn;
    }

    public void setFin_year_id(String fin_year_id) {
        this.fin_year_id = fin_year_id;
    }
    //---------Setter method ends-----------------








}
