package com.detatech.vitaluser.Utils;

/**
 * Created by Arbab on 7/17/2019.
 */

public class URLHelper {
    //    private static String base = "http://medical.elteyab.cf/api/";
    private static String base = "https://medical.detatech.xyz/api/";

    public static String login = base + "auth/login";
    public static String register = base + "auth/register";
    public static String check_token = base + "auth/check";
    public static String profile = base + "auth/profile";
    public static String cv = base + "emp_cv/";
    public static String employee = base + "employs";
    public static String category = base + "medicalFields";
    public static String sub_category = base + "medicalSpecialties";
    public static String medical_board = base + "medicalBoards";
    public static String accept_request = base + "userAcceptRequestSpecialists";
    public static String request_specialist = base + "requestSpecialists";
    public static String get_admin_requests = base + "requestSpecialists";
    public static String accept_emergency_services = base + "acceptEmergency";
    public static String emergency_services = base + "emergencyServiceds";
}
