package com.mediscreen.note.constants;

public class LogConstants {
    public static final String GET_ALL_NOTES_REQUEST_RECEIVED = "GET /note/list request received on ms-note";
    public static final String GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_SERVICE_CALL =
        "NoteService.getAllNotesForPatientPageable request received on ms-note";
    public static final String GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_OK = "List of medical notes retrieved for page {} \n";

    public static final String ADD_NOTE_SERVICE_CALL = "NoteService.addNote request received on ms-note";
    public static final String ADD_NOTE_SERVICE_OK = "Note {} added \n";
    public static final String ADD_NOTE_REQUEST_RECEIVED = "POST /note request received for patientId {} on ms-note";
}
