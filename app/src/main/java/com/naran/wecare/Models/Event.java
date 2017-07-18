package com.naran.wecare.Models;

/**
 * Created by NaRan on 6/8/17.
 */

public class Event {
    private String event_name;
    private String location;
    private String contact_number;
    private String time_start;
    private String time_end;
    private String event_date;

    public Event(String event_name, String location, String contact_number, String time_start, String time_end, String event_date) {
        this.event_name = event_name;
        this.location = location;
        this.contact_number = contact_number;
        this.time_start = time_start;
        this.time_end = time_end;
        this.event_date = event_date;
    }

    public Event() {

    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
}
