package com.naran.wecare.URLConstants;

/**
 * Created by NaRan on 5/21/17.
 */

public class UrlConstants {

    public static final String BASE_URL = "https://wecareyou.000webhostapp.com/";


    //fcm data

    public static final String URL_FCM = BASE_URL + "fcm/fcm_insert.php/";

    //Login, Register, Org login

    public static final String URL_REGISTER = BASE_URL + "login/registerUser.php/";
    public static final String URL_LOGIN = BASE_URL + "login/userLogin.php/";
    public static final String URL_ORG_LOGIN = BASE_URL + "organization/orgLogin.php/";

    // blood request

    public static final String POST_BLOOD_REQUEST_URL = BASE_URL + "postBloodRequest.php/";
    public static final String GET_BLOOD_REQUEST_URL = BASE_URL + "getBloodRequest.php/";
    public static final String GET_NEW_BLOOD_REQUEST_URL = BASE_URL + "getNewBloodRequest.php/";

    public static final String KEY_FULLNAME = "full_name";
    public static final String KEY_BLOOD_TYPE = "blood_type";
    public static final String KEY_BLOOD_AMOUNT = "blood_amount";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_DONATION_DATE = "donation_date";
    public static final String KEY_DONATION_PLACE = "donation_place";
    public static final String KEY_DONATION_TYPE = "donation_type";

    //blood database

    public static final String GET_BLOOD_DATABASE_URL = BASE_URL + "blood/getBloodDatabase.php/";
    public static final String POST_BLOOD_DATABASE_URL = BASE_URL + "blood/postBloodDatabase.php/";

    public static final String KEY_AGE = "age";
    public static final String KEY_SEX = "sex";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_LOCAL_ADDRESS = "local_address";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_EMAIL = "email";


    //event data

    public static final String GET_EVENT = BASE_URL + "getEvents.php/";
    public static final String POST_EVENT = BASE_URL + "postEvents.php/";

    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_LOCATION = "location";
    public static final String KEY_EVENT_CONTACT = "contact_number";
    public static final String KEY_EVENT_START_TIME = "time_start";
    public static final String KEY_EVENT_END_TIME = "time_end";
    public static final String KEY_EVENT_DATE = "event_date";

    // Blood  bank data

    public static final String KEY_ORG_NAME = "org_name";
    public static final String KEY_UPDATE_DATE = "update_date";
    public static final String KEY_ORG_CONTACT_NUMBER = "contact_number";
    public static final String KEY_AP = "'aPositive'";
    public static final String KEY_AN = "'aNegative'";
    public static final String KEY_BP = "'bPositive'";
    public static final String KEY_BN = "'bNegative'";
    public static final String KEY_OP = "'oPositive'";
    public static final String KEY_ON = "'oNegative'";
    public static final String KEY_ABP = "'abPositive'";
    public static final String KEY_ABN = "'abNegative'";

}
