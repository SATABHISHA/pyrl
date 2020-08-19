package org.wrkplan.payroll.Model;

public class OutDoorDetailModel {
    String od_duty_log_date, od_request_id, employee_id, employee_name, od_duty_log_id, log_action, log_datetime,
            log_time, latitude, longitude, location_address;

    //==========Getter Method starts========

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

    public String getOd_duty_log_id() {
        return od_duty_log_id;
    }

    public String getLog_action() {
        return log_action;
    }

    public String getLog_datetime() {
        return log_datetime;
    }

    public String getLog_time() {
        return log_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLocation_address() {
        return location_address;
    }

    //==========Getter Method ends========

    //==========Setter Method starts========

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

    public void setOd_duty_log_id(String od_duty_log_id) {
        this.od_duty_log_id = od_duty_log_id;
    }

    public void setLog_action(String log_action) {
        this.log_action = log_action;
    }

    public void setLog_datetime(String log_datetime) {
        this.log_datetime = log_datetime;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    //==========Setter Method ends========
}
