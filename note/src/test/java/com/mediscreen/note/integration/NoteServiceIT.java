package com.mediscreen.note.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.note.constants.TestConstants;
import com.mediscreen.note.repository.NoteRepository;
import com.mediscreen.note.service.contracts.INoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class NoteServiceIT {
    @Autowired
    private INoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void getAllNotesForPatientPageable_withDataInDb_returnsTheListOfAllValues() {

        Page<NoteDTO> noteDTOPage = noteService.getAllNotesForPatientPageable(
            TestConstants.NOTE1_PATIENT_ID, 1, 10, "id", "asc");

        assertFalse(noteDTOPage.getContent().isEmpty());
        assertTrue(noteDTOPage.getContent().size() <= 10);
        assertEquals(TestConstants.NOTE1_ID, noteDTOPage.getContent().get(0).getId());
        assertEquals(TestConstants.NOTE1_NOTE, noteDTOPage.getContent().get(0).getNote());
        assertEquals(TestConstants.NOTE1_NOTE_DATE, noteDTOPage.getContent().get(0).getCreationDate());
        assertEquals(TestConstants.NOTE2_NOTE, noteDTOPage.getContent().get(1).getNote());
    }
}
