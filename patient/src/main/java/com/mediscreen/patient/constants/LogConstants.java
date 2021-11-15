package com.mediscreen.patient.constants;

public class LogConstants {
    public static final String GET_ALL_PATIENTS_REQUEST_RECEIVED = "GET /patient/list request received on ms-patient" ;
    public static final String GET_ALL_PATIENTS_SERVICE_CALL = "PatientService.getAllPatients request received on ms-patient";
    public static final String GET_ALL_PATIENTS_SERVICE_OK = "List of patients retrieved with {} values \n";

    public static final String GET_PATIENT_BY_ID_REQUEST_RECEIVED = "GET /patient/{} request received on ms-patient" ;
    public static final String GET_PATIENT_BY_ID_REQUEST_OK = "Patient {} retrieved \n";
    public static final String GET_PATIENT_BY_ID_SERVICE_CALL = "PatientService.getPatientById request received on ms-patient";
    public static final String GET_PATIENT_BY_ID_SERVICE_OK = "Patient {} retrieved \n";
    public static final String GET_PATIENT_BY_ID_SERVICE_NOT_FOUND = "Patient {} not found \n";
}
