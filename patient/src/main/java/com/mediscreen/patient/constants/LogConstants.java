package com.mediscreen.patient.constants;

public class LogConstants {
    public static final String GET_ALL_PATIENTS_REQUEST_RECEIVED = "GET /patient/list request received on ms-patient";
    public static final String GET_ALL_PATIENTS_PER_PAGE_SERVICE_CALL =
        "PatientService.getAllPatientsPageable request received on ms-patient";
    public static final String GET_ALL_PATIENTS_PER_PAGE_SERVICE_OK = "List of patients retrieved for page {} \n";

    public static final String GET_PATIENT_BY_ID_REQUEST_RECEIVED =
        "GET /patient?patientId={} request received on ms-patient";
    public static final String GET_PATIENT_BY_ID_REQUEST_OK = "Patient {} retrieved \n";
    public static final String GET_PATIENT_BY_ID_SERVICE_CALL =
        "PatientService.getPatientById request received on ms-patient";
    public static final String GET_PATIENT_BY_ID_SERVICE_OK = "Patient {} retrieved \n";

    public static final String PATIENT_SERVICE_NOT_FOUND = "Patient {} not found \n";
    public static final String PATIENT_SERVICE_ALREADY_EXISTS =
        "Patient already exists (same firstname, lastname and birth date)";

    public static final String UPDATE_PATIENT_REQUEST_RECEIVED =
        "PUT /patient request received for patientId {} on ms-patient";
    public static final String UPDATE_PATIENT_SERVICE_CALL =
        "PatientService.updatePatient request received on ms-patient";
    public static final String UPDATE_PATIENT_SERVICE_OK = "Patient {} updated \n";
    public static final String UPDATE_PATIENT_SERVICE_NOT_FOUND = "Patient {} not found \n";

    public static final String ADD_PATIENT_REQUEST_RECEIVED = "POST /patient request received for {} on ms-patient";
    public static final String ADD_PATIENT_SERVICE_CALL = "PatientService.addPatient request received on ms-patient";
    public static final String ADD_PATIENT_SERVICE_OK = "Patient {} added \n";

    public static final String DELETE_PATIENT_BY_ID_REQUEST_RECEIVED =
        "DELETE /patient/{} request received on ms-patient";
    public static final String DELETE_PATIENT_BY_ID_REQUEST_OK = "Patient {} deleted \n";
    public static final String DELETE_PATIENT_BY_ID_SERVICE_CALL =
        "PatientService.deletePatient request received on ms-patient";
}
