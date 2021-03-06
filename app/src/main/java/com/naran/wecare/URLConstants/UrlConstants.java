package com.naran.wecare.URLConstants;

/**
 * Created by NaRan on 5/21/17.
 */

public class UrlConstants {

    public static final String BASE_URL = "https://wecareyou.000webhostapp.com/";


    //fcm notification

    public static final String URL_FCM = BASE_URL + "fcm/fcm_insert.php/";

    //Login, Register, Org login

    public static final String URL_REGISTER = BASE_URL + "login/registerUser.php/";
    public static final String URL_LOGIN = BASE_URL + "login/userLogin.php/";
    public static final String URL_ORG_LOGIN = BASE_URL + "organization/orgLogin.php/";

    // blood request

    public static final String POST_BLOOD_REQUEST_URL = BASE_URL + "postBloodRequest.php/";
    public static final String GET_BLOOD_REQUEST_URL = BASE_URL + "getBloodRequest.php/";

    public static final String KEY_FULLNAME = "full_name";
    public static final String KEY_BLOOD_TYPE = "blood_type";
    public static final String KEY_BLOOD_AMOUNT = "blood_amount";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_DONATION_DATE = "donation_date";
    public static final String KEY_DONATION_PLACE = "donation_place";
    public static final String KEY_DONATION_TYPE = "donation_type";

    //blood database

    public static final String GET_BLOOD_DATABASE_URL = BASE_URL + "blood/getBloodDatabase.php/";
    public static final String POST_BLOOD_DATABASE_URL = BASE_URL + "postUserData.php/";

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

    public static final String KEY_ORG_USER_NAME = "org_name";
    public static final String KEY_ORG_CONTACT = "contact_number";

    // BLOOD BANK

    public static final String POST_BLOOD_BANK = BASE_URL + "organization/org/postData.php/";
    public static final String GET_BLOOD_BANK = BASE_URL + "organization/org/getData.php/";


    // DELETE RECORDS

    public static final String DELETE_REQUEST = BASE_URL + "delete/blood_request.php/";
    public static final String DELETE_EVENT = BASE_URL + "delete/donation_event.php/";




}
