package com.mediscreen.clientUi.constants;

public class ViewNameConstants {

    public static final String HOME = "home";

    /* Patient Views */
    public static final String HOME_ORGANIZER = "/organizer";
    public static final String HOME_DOCTOR = "/doctor";
    public static final String SHOW_ALL_PATIENTS = "/patient/list";
    public static final String UPDATE_PATIENT = "/patient/update";
    public static final String ADD_PATIENT = "/patient/add";

    /* Note Views */
    public static final String SHOW_ALL_NOTES_FOR_PATIENT = "note/list";//TODO revoir pourquoi là pas de / et pas
    // pour patient
    public static final String ADD_NOTE = "note/add";
    public static final String UPDATE_NOTE = "note/update";

    public static final String RISK_ASSESSMENT_RESULT = "/riskAssessment/assess";
}
