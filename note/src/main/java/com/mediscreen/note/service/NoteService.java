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

    private static final ModelMapper modelMapper = new ModelMapper();
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

        Page<NoteDTO> noteDTOPage = noteRepository.findAllByPatientId(patientId, pageable)
                                                  .map(note -> modelMapper.map(note, NoteDTO.class));

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
    public NoteDTO addNote(NoteDTO noteDtoToAdd) {

        log.debug(LogConstants.ADD_NOTE_SERVICE_CALL);

        Note noteToAdd = modelMapper.map(noteDtoToAdd, Note.class);
        noteToAdd.setCreationDate(LocalDate.now());

        Note addedNote = noteRepository.save(noteToAdd);

        log.debug(LogConstants.ADD_NOTE_SERVICE_OK, noteToAdd.getId());
        return modelMapper.map(addedNote, NoteDTO.class);
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
    public NoteDTO updateNote(NoteDTO noteDtoToUpdate) throws NoteDoesNotExistException {

        log.debug(LogConstants.UPDATE_NOTE_SERVICE_CALL);

        try {
            /* check if note exists for the id */
            this.getNoteById(noteDtoToUpdate.getId());

            Note noteToUpdate = modelMapper.map(noteDtoToUpdate, Note.class);
            noteToUpdate.setLastUpdateDate(LocalDate.now());

            Note updatedNote = noteRepository.save(noteToUpdate);

            log.debug(LogConstants.UPDATE_NOTE_SERVICE_OK, noteToUpdate.getId());
            return modelMapper.map(updatedNote, NoteDTO.class);

        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(LogConstants.UPDATE_NOTE_SERVICE_NOT_FOUND, noteDtoToUpdate.getId());
            throw noteDoesNotExistException;
        }
    }

    /**
     * delete a note
     *
     * @param noteId of the note to delete
     * @throws NoteDoesNotExistException if no note found to delete
     */
    @Override
    public void deleteNoteById(String noteId) throws NoteDoesNotExistException {

        log.debug(LogConstants.DELETE_NOTE_BY_ID_SERVICE_CALL);

        noteRepository.findById(noteId)
                      .orElseThrow(() -> {
                          log.error(LogConstants.NOTE_SERVICE_NOT_FOUND, noteId);
                          return new NoteDoesNotExistException(ExceptionConstants.NOTE_NOT_FOUND + noteId);
                      });

        noteRepository.deleteById(noteId);
    }
}
