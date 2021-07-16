package org.wrkplan.payroll.Model.Mediclaim;

public class My_Mediclaim_Model {
    String approved_mediclaim_amount,employee_id,employee_name,mediclaim_amount,mediclaim_date,mediclaim_id,
            mediclaim_no,mediclaim_status,payment_amount;

    public String getApproved_mediclaim_amount() {
        return approved_mediclaim_amount;
    }

    public void setApproved_mediclaim_amount(String approved_mediclaim_amount) {
        this.approved_mediclaim_amount = approved_mediclaim_amount;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getMediclaim_amount() {
        return mediclaim_amount;
    }

    public void setMediclaim_amount(String mediclaim_amount) {
        this.mediclaim_amount = mediclaim_amount;
    }

    public String getMediclaim_date() {
        return mediclaim_date;
    }

    public void setMediclaim_date(String mediclaim_date) {
        this.mediclaim_date = mediclaim_date;
    }

    public String getMediclaim_id() {
        return mediclaim_id;
    }

    public void setMediclaim_id(String mediclaim_id) {
        this.mediclaim_id = mediclaim_id;
    }

    public String getMediclaim_no() {
        return mediclaim_no;
    }

    public void setMediclaim_no(String mediclaim_no) {
        this.mediclaim_no = mediclaim_no;
    }

    public String getMediclaim_status() {
        return mediclaim_status;
    }

    public void setMediclaim_status(String mediclaim_status) {
        this.mediclaim_status = mediclaim_status;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public My_Mediclaim_Model(String approved_mediclaim_amount, String employee_id, String employee_name, String mediclaim_amount, String mediclaim_date, String mediclaim_id, String mediclaim_no, String mediclaim_status, String payment_amount) {
        this.approved_mediclaim_amount = approved_mediclaim_amount;
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.mediclaim_amount = mediclaim_amount;
        this.mediclaim_date = mediclaim_date;
        this.mediclaim_id = mediclaim_id;
        this.mediclaim_no = mediclaim_no;
        this.mediclaim_status = mediclaim_status;
        this.payment_amount = payment_amount;
    }
}
