package org.wrkplan.payroll.Model;

public class LTAModel {
    String lta_no, lta_amount, lta_date, lta_status;

    //========Getter Method starts==========

    public String getLta_no() {
        return lta_no;
    }

    public String getLta_amount() {
        return lta_amount;
    }

    public String getLta_date() {
        return lta_date;
    }

    public String getLta_status() {
        return lta_status;
    }

    //========Getter Method ends==========
    //========Setter Method starts==========

    public void setLta_no(String lta_no) {
        this.lta_no = lta_no;
    }

    public void setLta_amount(String lta_amount) {
        this.lta_amount = lta_amount;
    }

    public void setLta_date(String lta_date) {
        this.lta_date = lta_date;
    }

    public void setLta_status(String lta_status) {
        this.lta_status = lta_status;
    }

    //========Setter Method ends==========
}
