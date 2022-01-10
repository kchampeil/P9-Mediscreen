package com.mediscreen.note.constants;

import java.time.LocalDate;

public class TestConstants {
    public static final String NOTE1_ID = "61b85fe5fa8e508d4860c7e9";
    public static final Integer NOTE1_PATIENT_ID = 1;
    public static final String NOTE1_NOTE = "notes de janvier sur Emma Stone";
    public static final String NOTE1_NOTE_UPDATED = "notes MODIFIÉES de janvier sur Emma Stone";
    public static final LocalDate NOTE1_CREATION_DATE = LocalDate.of(2021, 1, 2);
    public static final LocalDate NOTE1_LAST_UPDATE_DATE = LocalDate.of(2021, 12, 1);

    public static final String NOTE2_ID = "61b85fe5fa8e508d4860c7ec";
    public static final Integer NOTE2_PATIENT_ID = 1;
    public static final String NOTE2_NOTE = "notes de février sur Emma Stone";
    public static final LocalDate NOTE2_CREATION_DATE = LocalDate.of(2021, 2, 1);
    public static final LocalDate NOTE2_LAST_UPDATE_DATE = LocalDate.of(2021, 11, 1);

    public static final Integer NEW_NOTE_PATIENT_ID = 2021;
    public static final String NEW_NOTE_NOTE = "notes de décembre sur nouveau patient";

    public static final String UNKNOWN_PATIENT_ID = "666";
    public static final String UNKNOWN_NOTE_ID = "666";
}
