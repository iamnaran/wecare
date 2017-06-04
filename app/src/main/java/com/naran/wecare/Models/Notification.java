package com.naran.wecare.Models;

import java.io.Serializable;

/**
 * Created by NaRan on 5/22/17.
 */

public class Notification implements Serializable{

    public String full_name;
    public String blood_type;
    public String blood_amount;
    public String contact_number;
    public String donation_date;
    public String donation_place;
    public String donation_type;
    public String description;

    public Notification(String full_name, String blood_type, String blood_amount, String contact_number, String donation_date, String donation_place, String donation_type, String description) {
        this.full_name = full_name;
        this.blood_type = blood_type;
        this.blood_amount = blood_amount;
        this.contact_number = contact_number;
        this.donation_date = donation_date;
        this.donation_place = donation_place;
        this.donation_type = donation_type;
        this.description = description;
    }

    public Notification() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getBlood_amount() {
        return blood_amount;
    }

    public void setBlood_amount(String blood_amount) {
        this.blood_amount = blood_amount;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getDonation_date() {
        return donation_date;
    }

    public void setDonation_date(String donation_date) {
        this.donation_date = donation_date;
    }

    public String getDonation_place() {
        return donation_place;
    }

    public void setDonation_place(String donation_place) {
        this.donation_place = donation_place;
    }

    public String getDonation_type() {
        return donation_type;
    }

    public void setDonation_type(String donation_type) {
        this.donation_type = donation_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
