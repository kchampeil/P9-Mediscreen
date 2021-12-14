package com.mediscreen.note.service;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.note.repository.NoteRepository;
import com.mediscreen.note.service.contracts.INoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoteService implements INoteService {

    public final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Page<NoteDTO> getAllNotesForPatientPageable(Integer patientId, int pageNumber, int itemsPerPage,
                                                       String sortField, String sortDir) {
        return null;
    }
}
