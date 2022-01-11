package com.mediscreen.note.service.contracts;

import java.util.List;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import org.springframework.data.domain.Page;

public interface INoteService {
    Page<NoteDTO> getAllNotesForPatientPageable(Integer patientId, int pageNumber, int itemsPerPage, String sortField,
                                                String sortDir);

    List<NoteDTO> getAllNotesForPatient(Integer patientId);

    NoteDTO addNote(NoteDTO noteDtoToAdd);

    NoteDTO updateNote(NoteDTO NoteDtoToUpdate) throws NoteDoesNotExistException;

    NoteDTO getNoteById(String id) throws NoteDoesNotExistException;

    void deleteNoteById(String noteId) throws NoteDoesNotExistException;
}
