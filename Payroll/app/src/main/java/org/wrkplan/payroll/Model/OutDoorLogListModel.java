package org.wrkplan.payroll.Model;

public class OutDoorLogListModel {
    String od_duty_log_date, od_request_id, employee_id, employee_name;

    //===========Getter Method Starts==========

    public String getOd_duty_log_date() {
        return od_duty_log_date;
    }

    public String getOd_request_id() {
        return od_request_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    //===========Getter Method Ends==========

    //===========Setter Method Starts==========

    public void setOd_duty_log_date(String od_duty_log_date) {
        this.od_duty_log_date = od_duty_log_date;
    }

    public void setOd_request_id(String od_request_id) {
        this.od_request_id = od_request_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    //===========Setter Method Ends==========
}
