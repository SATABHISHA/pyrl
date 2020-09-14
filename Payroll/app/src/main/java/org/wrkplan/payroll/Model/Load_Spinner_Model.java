package org.wrkplan.payroll.Model;

public class Load_Spinner_Model {

    String financial_year_id,financial_year_code,display_code,date_start,date_end,
            closed_yn,payroll_year_start_date,payroll_year_end_date,calender_year;

    //Start  getter method


    public String getFinancial_year_id() {
        return financial_year_id;
    }

    public String getFinancial_year_code() {
        return financial_year_code;
    }

    public String getDisplay_code() {
        return display_code;
    }

    public String getDate_start() {
        return date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public String getClosed_yn() {
        return closed_yn;
    }

    public String getPayroll_year_start_date() {
        return payroll_year_start_date;
    }

    public String getPayroll_year_end_date() {
        return payroll_year_end_date;
    }

    public String getCalender_year() {
        return calender_year;
    }

    //start setter method


    public void setFinancial_year_id(String financial_year_id) {
        this.financial_year_id = financial_year_id;
    }

    public void setFinancial_year_code(String financial_year_code) {
        this.financial_year_code = financial_year_code;
    }

    public void setDisplay_code(String display_code) {
        this.display_code = display_code;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public void setClosed_yn(String closed_yn) {
        this.closed_yn = closed_yn;
    }

    public void setPayroll_year_start_date(String payroll_year_start_date) {
        this.payroll_year_start_date = payroll_year_start_date;
    }

    public void setPayroll_year_end_date(String payroll_year_end_date) {
        this.payroll_year_end_date = payroll_year_end_date;
    }

    public void setCalender_year(String calender_year) {
        this.calender_year = calender_year;
    }
}
