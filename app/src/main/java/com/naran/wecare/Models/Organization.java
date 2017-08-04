package com.naran.wecare.Models;

/**
 * Created by NaRan on 6/8/17.
 */

public class Organization {
    private String userName;
    private String aP;
    private String aN;
    private String bP;
    private String bN;
    private String abP;
    private String abN;
    private String oP;
    private String oN;
    private String contactNumber;

    public Organization(String userName, String aP, String aN, String bP, String bN, String abP, String abN, String oP, String oN, String contactNumber) {
        this.userName = userName;
        this.aP = aP;
        this.aN = aN;
        this.bP = bP;
        this.bN = bN;
        this.abP = abP;
        this.abN = abN;
        this.oP = oP;
        this.oN = oN;
        this.contactNumber = contactNumber;
    }

    public Organization() {

    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
