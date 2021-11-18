package com.mediscreen.patient.constants;

public class LogConstants {
    public static final String GET_ALL_PATIENTS_REQUEST_RECEIVED = "GET /patient/list request received on ms-patient";
    public static final String GET_ALL_PATIENTS_SERVICE_CALL = "PatientService.getAllPatients request received on " +
                                                               "ms-patient";
    public static final String GET_ALL_PATIENTS_SERVICE_OK = "List of patients retrieved with {} values \n";

    public static final String GET_PATIENT_BY_ID_REQUEST_RECEIVED = "GET /patient/{} request received on ms-patient";
    public static final String GET_PATIENT_BY_ID_REQUEST_OK = "Patient {} retrieved \n";
    public static final String GET_PATIENT_BY_ID_SERVICE_CALL = "PatientService.getPatientById request received on " +
                                                                "ms-patient";
    public static final String GET_PATIENT_BY_ID_SERVICE_OK = "Patient {} retrieved \n";
    public static final String GET_PATIENT_BY_ID_SERVICE_NOT_FOUND = "Patient {} not found \n";

    public static final String UPDATE_PATIENT_REQUEST_RECEIVED = "PUT /patient/{} request received on ms-patient";
    public static final String UPDATE_PATIENT_REQUEST_OK = "Patient {} updated \n";
    public static final String UPDATE_PATIENT_REQUEST_KO = "Error when updating patient {} \n";
    public static final String UPDATE_PATIENT_SERVICE_CALL = "PatientService.updatePatient request received on " +
                                                             "ms-patient";
    public static final String UPDATE_PATIENT_SERVICE_OK = "Patient {} updated \n";
    public static final String UPDATE_PATIENT_SERVICE_NOT_FOUND = "Patient {} not found \n";
    public static final String UPDATE_PATIENT_SERVICE_ALREADY_EXISTS = "Patient already exists (same firstname, " +
                                                                       "lastname and birth date)";

    public static final String ADD_PATIENT_REQUEST_RECEIVED = "POST /patient/{} request received on ms-patient";
    public static final String ADD_PATIENT_REQUEST_OK = "Patient {} added \n";
    public static final String ADD_PATIENT_REQUEST_KO = "Error when creating patient, patient has not been added \n";
    public static final String ADD_PATIENT_SERVICE_CALL = "PatientService.addPatient request received on ms-patient";
    public static final String ADD_PATIENT_SERVICE_ALREADY_EXISTS = "Patient already exists (same firstname, " +
                                                                    "lastname and birth date)"; //TODO à mutualiser avec update ?
    public static final String ADD_PATIENT_SERVICE_OK = "Patient {} added \n";
}
