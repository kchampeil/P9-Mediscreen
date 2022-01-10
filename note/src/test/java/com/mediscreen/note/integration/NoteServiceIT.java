package com.mediscreen.note.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.note.constants.TestConstants;
import com.mediscreen.note.model.Note;
import com.mediscreen.note.repository.NoteRepository;
import com.mediscreen.note.service.contracts.INoteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class NoteServiceIT {
    @Autowired
    private INoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Note.class);

        /* prepare DB for test with data */
        Note noteInDb = new Note(TestConstants.NOTE1_ID, TestConstants.NOTE1_PATIENT_ID, TestConstants.NOTE1_NOTE,
                                 TestConstants.NOTE1_CREATION_DATE, TestConstants.NOTE1_LAST_UPDATE_DATE);
        noteRepository.save(noteInDb);
        noteInDb = new Note(TestConstants.NOTE2_ID, TestConstants.NOTE2_PATIENT_ID, TestConstants.NOTE2_NOTE,
                            TestConstants.NOTE2_CREATION_DATE, TestConstants.NOTE2_LAST_UPDATE_DATE);
        noteRepository.save(noteInDb);
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.dropCollection(Note.class);
    }

    @Test
    void getAllNotesForPatientPageable_withDataInDb_returnsTheListOfAllValues() {

        Page<NoteDTO> noteDTOPage = noteService.getAllNotesForPatientPageable(
            TestConstants.NOTE1_PATIENT_ID, 1, 10, "id", "asc");

        assertFalse(noteDTOPage.getContent().isEmpty());
        assertEquals(2, noteDTOPage.getContent().size());
        assertEquals(TestConstants.NOTE1_ID, noteDTOPage.getContent().get(0).getId());
        assertEquals(TestConstants.NOTE1_NOTE, noteDTOPage.getContent().get(0).getNote());
        assertEquals(TestConstants.NOTE1_CREATION_DATE, noteDTOPage.getContent().get(0).getCreationDate());
        assertEquals(TestConstants.NOTE2_NOTE, noteDTOPage.getContent().get(1).getNote());
    }

    @Test
    void addNote_returnsCreatedNote() {

        NoteDTO noteDtoToAdd = new NoteDTO(TestConstants.NEW_NOTE_PATIENT_ID, TestConstants.NEW_NOTE_NOTE);

        NoteDTO addedNoteDTO = noteService.addNote(noteDtoToAdd);

        assertNotNull(addedNoteDTO);
        assertNotNull(addedNoteDTO.getId());
        assertEquals(noteDtoToAdd.getPatientId(), addedNoteDTO.getPatientId());
        assertEquals(noteDtoToAdd.getNote(), addedNoteDTO.getNote());
    }

    @Test
    void getNoteById_ForExistingNote_returnsNote() throws NoteDoesNotExistException {

        NoteDTO noteDTO = noteService.getNoteById(TestConstants.NOTE1_ID);

        assertEquals(TestConstants.NOTE1_ID, noteDTO.getId());
        assertEquals(TestConstants.NOTE1_NOTE, noteDTO.getNote());
    }

    @Test
    void updateNote_ForExistingNote_returnsUpdatedNote() throws NoteDoesNotExistException {

        NoteDTO noteDtoToUpdate = new NoteDTO(TestConstants.NOTE2_ID, TestConstants.NOTE2_PATIENT_ID,
                                              TestConstants.NOTE2_NOTE, TestConstants.NOTE2_CREATION_DATE,
                                              null);

        NoteDTO updatedNoteDTO = noteService.updateNote(noteDtoToUpdate);

        assertNotNull(updatedNoteDTO);
        assertEquals(TestConstants.NOTE2_ID, updatedNoteDTO.getId());
        assertEquals(TestConstants.NOTE2_NOTE, updatedNoteDTO.getNote());
        assertEquals(TestConstants.NOTE2_CREATION_DATE, updatedNoteDTO.getCreationDate());
        assertNotNull(updatedNoteDTO.getLastUpdateDate());
    }

    @Test
    void deleteNote_ForExistingNote_returnsNothing() throws NoteDoesNotExistException {

        /* check that the note exists before deletion */
        NoteDTO noteDTO = noteService.getNoteById(TestConstants.NOTE1_ID);
        assertNotNull(noteDTO);

        noteService.deleteNoteById(TestConstants.NOTE1_ID);

        /* check that the note has been deleted */
        assertThrows(NoteDoesNotExistException.class, () -> noteService.getNoteById(TestConstants.NOTE1_ID));
    }
}
