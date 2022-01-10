package com.mediscreen.note.constants;

public class LogConstants {
    public static final String GET_ALL_NOTES_REQUEST_RECEIVED = "GET /note/list request received on ms-note";
    public static final String GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_SERVICE_CALL =
        "NoteService.getAllNotesForPatientPageable request received on ms-note";
    public static final String GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_OK = "List of medical notes retrieved for page {} \n";

    public static final String ADD_NOTE_SERVICE_CALL = "NoteService.addNote request received on ms-note";
    public static final String ADD_NOTE_SERVICE_OK = "Note {} added \n";
    public static final String ADD_NOTE_REQUEST_RECEIVED = "POST /note request received for patientId {} on ms-note";

    public static final String UPDATE_NOTE_SERVICE_CALL = "NoteService.updateNote request received on ms-note";
    public static final String UPDATE_NOTE_SERVICE_OK = "Note {} updated \n";
    public static final String UPDATE_NOTE_SERVICE_NOT_FOUND = "Note {} not found \n";
    public static final String UPDATE_NOTE_REQUEST_RECEIVED = "PUT /note request received for patientId {} on ms-note";

    public static final String GET_NOTE_BY_ID_SERVICE_CALL = "NoteService.getNoteById request received on ms-note";
    public static final String GET_NOTE_BY_ID_SERVICE_OK = "Note {} retrieved \n";
    public static final String GET_NOTE_BY_ID_REQUEST_RECEIVED = "GET /note?noteId={} request received on ms-note";
    public static final String GET_NOTE_BY_ID_REQUEST_OK = "Note {} retrieved \n";

    public static final String NOTE_SERVICE_NOT_FOUND = "Note {} not found \n";

    public static final String DELETE_NOTE_BY_ID_SERVICE_CALL = "NoteService.deleteNote request received on ms-note";
}
