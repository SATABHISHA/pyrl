package org.wrkplan.payroll.Model;

public class SubordinateLeaveApplicationModel {
    String appliction_code,approved_by,approved_date,description,employee_name,final_approved_by,from_date,leave_name,leave_status,
            supervisor_remark,to_date;
    int appliction_id,approved_by_id,approved_level,employee_id,supervisor1_id,supervisor2_id,total_days;

    public SubordinateLeaveApplicationModel(String appliction_code, int appliction_id, String approved_by, int approved_by_id, String approved_date, int approved_level, String description, int employee_id, String employee_name, String final_approved_by, String from_date, String leave_name, String leave_status, int supervisor1_id, int supervisor2_id, String supervisor_remark, int total_days, String to_date) {

        this.appliction_code = appliction_code;
        this.approved_by = approved_by;
        this.approved_date = approved_date;
        this.description = description;
        this.employee_name = employee_name;
        this.final_approved_by = final_approved_by;
        this.from_date = from_date;
        this.leave_name = leave_name;
        this.leave_status = leave_status;
        this.supervisor_remark = supervisor_remark;
        this.to_date = to_date;
        this.appliction_id = appliction_id;
        this.approved_by_id = approved_by_id;
        this.approved_level = approved_level;
        this.employee_id = employee_id;
        this.supervisor1_id = supervisor1_id;
        this.supervisor2_id = supervisor2_id;
        this.total_days = total_days;
    }

    public int getAppliction_id() {
        return appliction_id;
    }

    public void setAppliction_id(int appliction_id) {
        this.appliction_id = appliction_id;
    }

    public int getApproved_by_id() {
        return approved_by_id;
    }

    public void setApproved_by_id(int approved_by_id) {
        this.approved_by_id = approved_by_id;
    }

    public int getApproved_level() {
        return approved_level;
    }

    public void setApproved_level(int approved_level) {
        this.approved_level = approved_level;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getSupervisor1_id() {
        return supervisor1_id;
    }

    public void setSupervisor1_id(int supervisor1_id) {
        this.supervisor1_id = supervisor1_id;
    }

    public int getSupervisor2_id() {
        return supervisor2_id;
    }

    public void setSupervisor2_id(int supervisor2_id) {
        this.supervisor2_id = supervisor2_id;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public String getAppliction_code() {
        return appliction_code;
    }

    public void setAppliction_code(String appliction_code) {
        this.appliction_code = appliction_code;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getFinal_approved_by() {
        return final_approved_by;
    }

    public void setFinal_approved_by(String final_approved_by) {
        this.final_approved_by = final_approved_by;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getLeave_status() {
        return leave_status;
    }

    public void setLeave_status(String leave_status) {
        this.leave_status = leave_status;
    }

    public String getSupervisor_remark() {
        return supervisor_remark;
    }

    public void setSupervisor_remark(String supervisor_remark) {
        this.supervisor_remark = supervisor_remark;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
}
