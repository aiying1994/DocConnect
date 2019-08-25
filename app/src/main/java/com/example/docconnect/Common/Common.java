package com.example.docconnect.Common;

import android.content.Intent;

import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.User;

import java.security.cert.CertPath;

public class Common {

    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:3000/";
    //10.161.90.71/MYPENLT-DLWKNQ2\TILIEWSQL
//    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:3000/";
    public static final String API_KEY = "1234"; // We will hard code API key now. Secure it with Firebase remote  config
    public static final int APP_REQUEST_CODE = 1234;
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_PREMISE_SAVE = "PREMISE_SAVE";
    public static final String KEY_SERVICE_LOAD_DONE = "SERVICE_LOAD_DONE"; // Premise's Services
    public static final String KEY_INFO_LOAD_DONE = "INFO_LOAD_DONE";// Premise's Info
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Premise currentPremise;
    public static int step = 0;
    public static String premise = ""; // This is premiseId
    public static String service = ""; // This is serviceId

    public class KEY_STEP {
    }
}
