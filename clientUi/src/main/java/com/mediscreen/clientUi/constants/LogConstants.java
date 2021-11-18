package com.mediscreen.clientUi.constants;

import org.slf4j.Marker;

public class LogConstants {
    public static final String HOME_REQUEST_RECEIVED = "GET request / received on clientUI";

    public static final String SHOW_ALL_PATIENTS_REQUEST_RECEIVED = "GET request /patient/list received on clientUI";

    public static final String SHOW_UPDATE_FORM_RECEIVED = "GET request /patient/update/{} received on clientUI";

    public static final String UPDATE_PATIENT_RECEIVED = "POST request /patient/update/{} received on clientUI";
    public static final String UPDATE_PATIENT_REQUEST_NOT_VALID = "Patient information not valid for patient id: ";
    public static final String UPDATE_PATIENT_REQUEST_OK = "Patient id {} has been updated\n";
    public static final String UPDATE_PATIENT_REQUEST_KO = "Patient id {} has not been updated : {} \n";

    public static final String SHOW_ADD_FORM_RECEIVED = "GET request /patient/add received on clientUI";
    public static final String ADD_PATIENT_RECEIVED = "POST request /patient/add received on clientUI";
    public static final String ADD_PATIENT_REQUEST_NOT_VALID = "Patient information not valid \n";
    public static final String ADD_PATIENT_REQUEST_OK = "Patient id {} has been created\n";
    public static final String ADD_PATIENT_REQUEST_KO ="Patient has not been created : {} \n";
}
