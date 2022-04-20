package org.wrkplan.payroll.Model;

public class NotificationModel {
    String title, notification_id, event_name, event_id, event_type, event_owner_id, event_owner, event_date, event_status, message;

    //----------Getter method, starts---------


    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getEvent_owner_id() {
        return event_owner_id;
    }

    public String getEvent_owner() {
        return event_owner;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_status() {
        return event_status;
    }

    //----------Getter method, ends---------

    //----------Setter method, starts---------


    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public void setEvent_owner_id(String event_owner_id) {
        this.event_owner_id = event_owner_id;
    }

    public void setEvent_owner(String event_owner) {
        this.event_owner = event_owner;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    //----------setter method, ends---------
}
