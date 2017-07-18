package com.naran.wecare.Models;

/**
 * Created by NaRan on 6/8/17.
 */

public class Organization {
    private String org_name;
    private String contact_number;
    private String update_date;
    private String aP;
    private String aN;
    private String bP;
    private String bN;
    private String abP;
    private String abN;
    private String oP;
    private String oN;

    public Organization(String org_name, String contact_number, String update_date, String aP, String aN, String bP, String bN, String abP, String abN, String oP, String oN) {
        this.org_name = org_name;
        this.contact_number = contact_number;
        this.update_date = update_date;
        this.aP = aP;
        this.aN = aN;
        this.bP = bP;
        this.bN = bN;
        this.abP = abP;
        this.abN = abN;
        this.oP = oP;
        this.oN = oN;
    }

    public Organization() {

    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getaP() {
        return aP;
    }

    public void setaP(String aP) {
        this.aP = aP;
    }

    public String getaN() {
        return aN;
    }

    public void setaN(String aN) {
        this.aN = aN;
    }

    public String getbP() {
        return bP;
    }

    public void setbP(String bP) {
        this.bP = bP;
    }

    public String getbN() {
        return bN;
    }

    public void setbN(String bN) {
        this.bN = bN;
    }

    public String getAbP() {
        return abP;
    }

    public void setAbP(String abP) {
        this.abP = abP;
    }

    public String getAbN() {
        return abN;
    }

    public void setAbN(String abN) {
        this.abN = abN;
    }

    public String getoP() {
        return oP;
    }

    public void setoP(String oP) {
        this.oP = oP;
    }

    public String getoN() {
        return oN;
    }

    public void setoN(String oN) {
        this.oN = oN;
    }
}
