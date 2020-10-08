package org.wrkplan.payroll.Model;

public class TimesheetMyAttendanceModel_v3 {
    String sl, attendance_biometric_id, date, time, biometric_machine_yn, status;

    //==========Getter Method starts=========

    public String getSl() {
        return sl;
    }

    public String getAttendance_biometric_id() {
        return attendance_biometric_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getBiometric_machine_yn() {
        return biometric_machine_yn;
    }

    public String getStatus() {
        return status;
    }

    //==========Getter Method ends=========


    //==========Setter Method starts=========

    public void setSl(String sl) {
        this.sl = sl;
    }

    public void setAttendance_biometric_id(String attendance_biometric_id) {
        this.attendance_biometric_id = attendance_biometric_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBiometric_machine_yn(String biometric_machine_yn) {
        this.biometric_machine_yn = biometric_machine_yn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //==========Setter Method ends=========
}
