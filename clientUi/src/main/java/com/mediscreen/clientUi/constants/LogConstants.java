package com.mediscreen.clientUi.constants;

public class LogConstants {
    public static final String HOME_REQUEST_RECEIVED = "GET request / received on clientUI";
    public static final String HOME_ORGANIZER_REQUEST_RECEIVED = "GET request /organizer received on clientUI";
    public static final String HOME_DOCTOR_REQUEST_RECEIVED = "GET request /doctor received on clientUI";

    public static final String SHOW_PATIENTS_PER_PAGE_REQUEST_RECEIVED =
        "GET request /patient/list/{} sorted by {} and ordered {} received on clientUI";

    public static final String SHOW_UPDATE_PATIENT_FORM_RECEIVED =
        "GET request /patient/update/{} received on clientUI";

    public static final String UPDATE_PATIENT_RECEIVED = "POST request /patient/update/{} received on clientUI";
    public static final String UPDATE_PATIENT_REQUEST_NOT_VALID = "Patient information not valid for patient id: ";
    public static final String UPDATE_PATIENT_REQUEST_OK = "Patient id {} has been updated\n";
    public static final String UPDATE_PATIENT_REQUEST_KO = "Patient id {} has not been updated : {} \n";

    public static final String SHOW_ADD_PATIENT_FORM_RECEIVED = "GET request /patient/add received on clientUI";
    public static final String ADD_PATIENT_RECEIVED = "POST request /patient/add received on clientUI";
    public static final String ADD_PATIENT_REQUEST_NOT_VALID = "Patient information not valid \n";
    public static final String ADD_PATIENT_REQUEST_OK = "Patient id {} has been created\n";
    public static final String ADD_PATIENT_REQUEST_KO = "Patient has not been created : {} \n";

    public static final String DELETE_PATIENT_REQUEST_RECEIVED = "GET request /patient/delete/{} received on clientUI";
    public static final String DELETE_PATIENT_REQUEST_OK = "Patient id {} has been deleted\n";

    public static final String SHOW_NOTES_PER_PAGE_REQUEST_RECEIVED =
        "GET request /note/{}/list/{} sorted by {} and ordered {} received on clientUI";
    public static final String SHOW_ADD_NOTE_FORM_RECEIVED = "GET request /note/add received on clientUI";
    public static final String ADD_NOTE_RECEIVED = "POST request /note/{}/add received on clientUI";
    public static final String ADD_NOTE_REQUEST_NOT_VALID = "Note information not valid \n";
    public static final String ADD_NOTE_REQUEST_OK = "Note id {} has been created\n";
    public static final String SHOW_UPDATE_NOTE_FORM_RECEIVED = "GET request /note/update/{} received on clientUI";
    public static final String UPDATE_NOTE_RECEIVED = "POST request /note/update/{} received on clientUI";
    public static final String UPDATE_NOTE_REQUEST_NOT_VALID = "Note information not valid for note id: ";
    public static final String UPDATE_NOTE_REQUEST_OK = "Note id {} has been updated\n";
    public static final String UPDATE_NOTE_REQUEST_KO = "Note id {} has not been updated : {} \n";
}
