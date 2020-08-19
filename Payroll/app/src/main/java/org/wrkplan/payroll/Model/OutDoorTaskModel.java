package org.wrkplan.payroll.Model;

public class OutDoorTaskModel {
    String od_duty_task_detail_id, od_duty_task_head_id, task_name, task_description;
    Integer task_delete_api_call;

    //===========Getter method starts===========

    public String getOd_duty_task_detail_id() {
        return od_duty_task_detail_id;
    }

    public String getOd_duty_task_head_id() {
        return od_duty_task_head_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public Integer getTask_delete_api_call() {
        return task_delete_api_call;
    }

    //===========Getter method ends===========
    //===========Setter method starts===========

    public void setOd_duty_task_detail_id(String od_duty_task_detail_id) {
        this.od_duty_task_detail_id = od_duty_task_detail_id;
    }

    public void setOd_duty_task_head_id(String od_duty_task_head_id) {
        this.od_duty_task_head_id = od_duty_task_head_id;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public void setTask_delete_api_call(Integer task_delete_api_call) {
        this.task_delete_api_call = task_delete_api_call;
    }
//===========Setter method ends===========
}
