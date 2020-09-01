package org.wrkplan.payroll.Model;

public class TimesheetMyAttendanceModel {
    String slno, ts_date, time_in, time_out, work_from_home, attendance_status;

    //---------Getter method starts--

    public String getSlno() {
        return slno;
    }

    public String getTs_date() {
        return ts_date;
    }

    public String getTime_in() {
        return time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public String getWork_from_home() {
        return work_from_home;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    //---------Getter method ends--

    //---------Setter method starts--

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public void setTs_date(String ts_date) {
        this.ts_date = ts_date;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public void setWork_from_home(String work_from_home) {
        this.work_from_home = work_from_home;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }

    //---------Setter method ends--
}
