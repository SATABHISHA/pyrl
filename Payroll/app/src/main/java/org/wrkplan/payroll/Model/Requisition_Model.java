package org.wrkplan.payroll.Model;

public class Requisition_Model {
   String approved_by_id,approved_by_name,approved_date,approved_requisition_amount,description,
           employee_id,employee_name,requisition_amount,requisition_date,
           requisition_id,requisition_no,requisition_status,
           requisition_type,supervisor1_id,supervisor2_id,supervisor_remark,reason,return_period_in_months;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReturn_period_in_months() {
        return return_period_in_months;
    }

    public void setReturn_period_in_months(String return_period_in_months) {
        this.return_period_in_months = return_period_in_months;
    }

    public String getApproved_by_id() {
        return approved_by_id;
    }

    public void setApproved_by_id(String approved_by_id) {
        this.approved_by_id = approved_by_id;
    }

    public String getApproved_by_name() {
        return approved_by_name;
    }

    public void setApproved_by_name(String approved_by_name) {
        this.approved_by_name = approved_by_name;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public String getApproved_requisition_amount() {
        return approved_requisition_amount;
    }

    public void setApproved_requisition_amount(String approved_requisition_amount) {
        this.approved_requisition_amount = approved_requisition_amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRequisition_amount() {
        return requisition_amount;
    }

    public void setRequisition_amount(String requisition_amount) {
        this.requisition_amount = requisition_amount;
    }

    public String getRequisition_date() {
        return requisition_date;
    }

    public void setRequisition_date(String requisition_date) {
        this.requisition_date = requisition_date;
    }

    public String getRequisition_id() {
        return requisition_id;
    }

    public void setRequisition_id(String requisition_id) {
        this.requisition_id = requisition_id;
    }

    public String getRequisition_no() {
        return requisition_no;
    }

    public void setRequisition_no(String requisition_no) {
        this.requisition_no = requisition_no;
    }

    public String getRequisition_status() {
        return requisition_status;
    }

    public void setRequisition_status(String requisition_status) {
        this.requisition_status = requisition_status;
    }

    public String getRequisition_type() {
        return requisition_type;
    }

    public void setRequisition_type(String requisition_type) {
        this.requisition_type = requisition_type;
    }

    public String getSupervisor1_id() {
        return supervisor1_id;
    }

    public void setSupervisor1_id(String supervisor1_id) {
        this.supervisor1_id = supervisor1_id;
    }

    public String getSupervisor2_id() {
        return supervisor2_id;
    }

    public void setSupervisor2_id(String supervisor2_id) {
        this.supervisor2_id = supervisor2_id;
    }

    public String getSupervisor_remark() {
        return supervisor_remark;
    }

    public void setSupervisor_remark(String supervisor_remark) {
        this.supervisor_remark = supervisor_remark;
    }

    public Requisition_Model(String approved_by_id, String approved_by_name, String approved_date, String approved_requisition_amount, String description, String employee_id, String employee_name, String requisition_amount, String requisition_date, String requisition_id, String requisition_no, String requisition_status, String requisition_type, String supervisor1_id, String supervisor2_id, String supervisor_remark,String reason,String return_period_in_months) {
        this.approved_by_id = approved_by_id;
        this.approved_by_name = approved_by_name;
        this.approved_date = approved_date;
        this.approved_requisition_amount = approved_requisition_amount;
        this.description = description;
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.requisition_amount = requisition_amount;
        this.requisition_date = requisition_date;
        this.requisition_id = requisition_id;
        this.requisition_no = requisition_no;
        this.requisition_status = requisition_status;
        this.requisition_type = requisition_type;
        this.supervisor1_id = supervisor1_id;
        this.supervisor2_id = supervisor2_id;
        this.supervisor_remark = supervisor_remark;
        this.reason=reason;
        this.return_period_in_months=return_period_in_months;
    }
}
