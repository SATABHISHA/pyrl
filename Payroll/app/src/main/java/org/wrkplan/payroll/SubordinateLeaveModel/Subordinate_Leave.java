package org.wrkplan.payroll.SubordinateLeaveModel;

public class Subordinate_Leave {
    String appliction_code,leave_name,employee_name,from_date,to_date,total_days,leave_status,appliction_id
            ,supervisor2_id,supervisor1_id;

    public String getAppliction_code() {
        return appliction_code;
    }

    public String getSupervisor2_id() {
        return supervisor2_id;
    }

    public void setSupervisor2_id(String supervisor2_id) {
        this.supervisor2_id = supervisor2_id;
    }

    public String getSupervisor1_id() {
        return supervisor1_id;
    }

    public void setSupervisor1_id(String supervisor1_id) {
        this.supervisor1_id = supervisor1_id;
    }

    public String getAppliction_id() {
        return appliction_id;
    }

    public void setAppliction_id(String appliction_id) {
        this.appliction_id = appliction_id;
    }

    public void setAppliction_code(String appliction_code) {
        this.appliction_code = appliction_code;
    }

    public String getLeave_name() {
        return leave_name;
    }

    public void setLeave_name(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }

    public String getLeave_status() {
        return leave_status;
    }

    public void setLeave_status(String leave_status) {
        this.leave_status = leave_status;
    }
}
