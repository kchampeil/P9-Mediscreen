package com.mediscreen.note.service.contracts;

import java.util.Optional;

import com.mediscreen.commons.dto.NoteDTO;
import org.springframework.data.domain.Page;

public interface INoteService {
    Page<NoteDTO> getAllNotesForPatientPageable(Integer patientId, int pageNumber, int itemsPerPage, String sortField,
                                                String sortDir);

    Optional<NoteDTO> addNote(NoteDTO noteDtoToAdd);
}
