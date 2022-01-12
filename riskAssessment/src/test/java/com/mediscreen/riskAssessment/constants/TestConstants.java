package com.mediscreen.riskAssessment.constants;

import java.time.LocalDate;

public class TestConstants {

    public static final Integer PATIENT1_ID = 1;
    public static final String PATIENT1_FIRSTNAME = "Camille";
    public static final String PATIENT1_LASTNAME = "Doe";
    public static final String PATIENT1_ADDRESS = "1 Rue du Paradis 75000 PARIS";
    public static final String PATIENT1_PHONE = "0101010101";
    public static final LocalDate BIRTHDATE_ABOVE_THIRTY = LocalDate.of(1988, 11, 6);
    public static final LocalDate BIRTHDATE_UNDER_THIRTY = LocalDate.of(2000, 11, 6);
    public static final LocalDate BIRTHDATE_THIRTY = LocalDate.of(1992, 1, 1);
    public static final String GENDER_FEMALE = "F";
    public static final String GENDER_MALE = "M";

    public static final Integer NOTE_PATIENT_ID = 1;
    public static final LocalDate NOTE_CREATION_DATE = LocalDate.of(2021, 1, 2);
    public static final LocalDate NOTE_LAST_UPDATE_DATE = LocalDate.of(2021, 12, 1);

    public static final String NOTE_ID1 = "61b85fe5fa8e508d4860c7e9";
    public static final String NOTES_WITHOUT_RISK_FACTOR = "notes sans facteurs de risques du diabète";

    public static final String NOTE_ID2 = "61b85fe5fa8e508d4860c7ec";
    public static final String NOTES_WITH_ONE_RISK_FACTOR = "notes avec un facteur de risques du diabète : vertiges";

    public static final String NOTE_ID3 = "61dd51ddf6c1bb681b040357";
    public static final String NOTES_WITH_TWO_RISK_FACTOR =
        "notes avec deux facteurs de risques du diabète : Réaction à certains produits et faible taux d'anticorps";
}
