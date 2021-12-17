package com.mediscreen.clientUi.constants;

import java.time.LocalDate;

public class TestConstants {

    public static final Integer PATIENT1_ID = 1;
    public static final String PATIENT1_FIRSTNAME = "Emma";
    public static final String PATIENT1_LASTNAME = "Stone";
    public static final LocalDate PATIENT1_BIRTHDATE = LocalDate.of(1988, 11, 6);
    public static final String PATIENT1_GENDER = "F";
    public static final String PATIENT1_ADDRESS = "1 Rue du Paradis 75000 PARIS";
    public static final String PATIENT1_PHONE = "0101010101";

    public static final Integer UNKNOWN_PATIENT_ID = 666;

    public static final LocalDate PATIENT1_BIRTHDATE_IN_FUTURE = LocalDate.of(2088, 11, 6);
    public static final String PATIENT1_GENDER_TOO_LONG = "Female";

    public static final String NEW_PATIENT_FIRSTNAME = "Firstname new";
    public static final String NEW_PATIENT_LASTNAME = "Lastname new";
    public static final LocalDate NEW_PATIENT_BIRTHDATE = LocalDate.of(1980, 12, 31);
    public static final String NEW_PATIENT_GENDER = "F";
    public static final String NEW_PATIENT_ADDRESS = "Address new";
    public static final String NEW_PATIENT_PHONE = "0909090909";

    public static final String NOTE1_ID = "61b85fe5fa8e508d4860c7e9";
    public static final Integer NOTE1_PATIENT_ID = 1;
    public static final String NOTE1_NOTE = "notes de janvier sur Emma Stone";
    public static final LocalDate NOTE1_CREATION_DATE = LocalDate.of(2021, 1, 1);

    public static final LocalDate NOTE1_LAST_UPDATE_DATE = LocalDate.of(2021, 12, 1);
}
