package org.wrkplan.payroll.Model;

public class DashboardPendingItemModel {
    public String event_id, event_name, event_owner_id, event_type, event_status, event_owner_name;

    //--------Getter method starts-------

    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_owner_id() {
        return event_owner_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getEvent_status() {
        return event_status;
    }

    public String getEvent_owner_name() {
        return event_owner_name;
    }
//--------Getter method ends-------

    //--------Setter method starts-------

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_owner_id(String event_owner_id) {
        this.event_owner_id = event_owner_id;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public void setEvent_owner_name(String event_owner_name) {
        this.event_owner_name = event_owner_name;
    }

    //--------Setter method ends-------
}
