package com.mediscreen.note.service;

import java.time.LocalDate;
import java.util.Optional;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.note.constants.LogConstants;
import com.mediscreen.note.model.Note;
import com.mediscreen.note.repository.NoteRepository;
import com.mediscreen.note.service.contracts.INoteService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoteService implements INoteService {

    public final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * get all medical notes (DTO) for a given patient, page number, items per page
     * sorted on one given field with one direction
     *
     * @param patientId    id of the patient we want to retrieve the medical notes
     * @param pageNumber   page we want to retrieve the list of medical notes
     * @param itemsPerPage number of items per page
     * @param sortField    sorted field
     * @param sortDir      sorted direction
     * @return Page of NoteDTO
     */
    @Override
    public Page<NoteDTO> getAllNotesForPatientPageable(Integer patientId, int pageNumber, int itemsPerPage,
                                                       String sortField, String sortDir) {
        log.debug(LogConstants.GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_SERVICE_CALL);

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, itemsPerPage, sort);
        //TOASK envoyer directement pagenumber-1 par le controller de l'UI ?

        Page<Note> notePage = noteRepository.findAllByPatientId(patientId, pageable);

        ModelMapper modelMapper = new ModelMapper();
        Page<NoteDTO> noteDTOPage = notePage.map(note -> modelMapper.map(note, NoteDTO.class));

        log.debug(LogConstants.GET_ALL_NOTES_FOR_PATIENT_PER_PAGE_OK, pageNumber);

        return noteDTOPage;
    }

    /**
     * add a note for a patient
     *
     * @param noteDtoToAdd information for the note to add
     * @return added note (DTO)
     */
    @Override
    public Optional<NoteDTO> addNote(NoteDTO noteDtoToAdd) {

        log.debug(LogConstants.ADD_NOTE_SERVICE_CALL);

        ModelMapper modelMapper = new ModelMapper();
        Note noteToAdd = modelMapper.map(noteDtoToAdd, Note.class);
        noteToAdd.setCreationDate(LocalDate.now());

        Note addedNote = noteRepository.save(noteToAdd);

        log.debug(LogConstants.ADD_NOTE_SERVICE_OK, noteToAdd.getId());
        return Optional.ofNullable(modelMapper.map(addedNote, NoteDTO.class));
    }
}
