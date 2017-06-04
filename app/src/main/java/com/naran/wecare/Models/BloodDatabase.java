package com.naran.wecare.Models;

/**
 * Created by NaRan on 5/25/17.
 */

public class BloodDatabase {
    private String full_name;
    public String age;
    public String sex;
    public String district;
    public String local_address;
    public String email;
    public String contact_number;
    public String blood_group;

    public BloodDatabase(String full_name, String age, String sex, String district, String local_address, String email, String contact_number, String blood_group) {
        this.full_name = full_name;
        this.age = age;
        this.sex = sex;
        this.district = district;
        this.local_address = local_address;
        this.email = email;
        this.contact_number = contact_number;
        this.blood_group = blood_group;
    }

    public BloodDatabase() {

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocal_address() {
        return local_address;
    }

    public void setLocal_address(String local_address) {
        this.local_address = local_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }
}
