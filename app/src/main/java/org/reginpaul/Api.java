package org.reginpaul;

public class Api {

    private static final String ROOT_URL = "http://mindvoice.info/rpweb/v1/Api.php?apicall=";

    /*public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_READ_USERS = ROOT_URL + "getuser&email=";
    public static final String URL_READ_MSG = ROOT_URL + "getmessage";
    public static final String URL_CREATE_MSG = ROOT_URL + "createmessage";*/
    //public static final String URL_READ_MATERIALS = ROOT_URL + "getpdf&category=\"CSE\"";
    public static final String URL_READ_MATERIALS = ROOT_URL + "getpdf&category=";
    public static final String URL_READ_STUDY = ROOT_URL + "getstudy&category=&semester=";
    public static final String URL_READ_QUESTIONS = ROOT_URL + "getquestion&category=";
    public static final String URL_READ_MSG = ROOT_URL + "getmessage";
    public static final String URL_CREATE_MSG = ROOT_URL + "createmessage";
    public static final String URL_CREATE_EVNT = ROOT_URL + "createuser";
    public static final String URL_UP_EVNT = ROOT_URL + "getuser";
}
