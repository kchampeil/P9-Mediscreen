package com.mediscreen.note.service;

import java.time.LocalDate;
import java.util.Optional;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
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

    /**
     * get note  from his id
     *
     * @param noteId id of the note
     * @return note(DTO)
     * @throws NoteDoesNotExistException if no note found for the id
     */
    @Override
    public NoteDTO getNoteById(String noteId) throws NoteDoesNotExistException {
        log.debug(LogConstants.GET_NOTE_BY_ID_SERVICE_CALL);

        Optional<Note> note = noteRepository.findById(noteId);

        if (note.isPresent()) {
            log.debug(LogConstants.GET_NOTE_BY_ID_SERVICE_OK, noteId);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(note.get(), NoteDTO.class);

        } else {
            log.error(LogConstants.NOTE_SERVICE_NOT_FOUND, noteId);
            throw new NoteDoesNotExistException(ExceptionConstants.NOTE_NOT_FOUND + noteId);
        }
    }

    /**
     * update a note
     *
     * @param noteDtoToUpdate new information for the note
     * @return updated note (DTO)
     */
    @Override
    public Optional<NoteDTO> updateNote(NoteDTO noteDtoToUpdate) throws NoteDoesNotExistException {

        log.debug(LogConstants.UPDATE_NOTE_SERVICE_CALL);

        try {
            /* check if patient exists for the id */
            this.getNoteById(noteDtoToUpdate.getId());

            /* map DTO to DAO, save in repository and map back to NoteDTO for return */
            ModelMapper modelMapper = new ModelMapper();
            Note noteToUpdate = modelMapper.map(noteDtoToUpdate, Note.class);
            noteToUpdate.setLastUpdateDate(LocalDate.now());

            Note updatedNote = noteRepository.save(noteToUpdate);

            log.debug(LogConstants.UPDATE_NOTE_SERVICE_OK, noteToUpdate.getId());
            return Optional.ofNullable(modelMapper.map(updatedNote, NoteDTO.class));
        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(LogConstants.UPDATE_NOTE_SERVICE_NOT_FOUND, noteDtoToUpdate.getId());
            throw noteDoesNotExistException;
        }
    }
}
