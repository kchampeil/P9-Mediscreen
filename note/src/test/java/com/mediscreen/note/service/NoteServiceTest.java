package com.mediscreen.note.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.note.constants.TestConstants;
import com.mediscreen.note.model.Note;
import com.mediscreen.note.repository.NoteRepository;
import com.mediscreen.note.service.contracts.INoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class NoteServiceTest {

    private static NoteDTO note1Dto;
    private static Note note1InDb;
    private static Note note2InDb;
    @MockBean
    private NoteRepository noteRepositoryMock;
    @Autowired
    private INoteService noteService;

    @BeforeAll
    static void setUp() {

        note1Dto = new NoteDTO(TestConstants.NOTE1_ID, TestConstants.NOTE1_PATIENT_ID,
                               TestConstants.NOTE1_NOTE, TestConstants.NOTE1_CREATION_DATE);
        note1InDb = new Note(TestConstants.NOTE1_ID, TestConstants.NOTE1_PATIENT_ID,
                             TestConstants.NOTE1_NOTE, TestConstants.NOTE1_CREATION_DATE);
        note2InDb = new Note(TestConstants.NOTE2_ID, TestConstants.NOTE2_PATIENT_ID,
                             TestConstants.NOTE2_NOTE, TestConstants.NOTE2_CREATION_DATE);
    }

    @Test
    void addNote_returnsCreatedNote() throws PatientAlreadyExistException {

        when(noteRepositoryMock.save(any(Note.class))).thenReturn(note1InDb);

        Optional<NoteDTO> addedNoteDTO = noteService.addNote(note1Dto);
        assertTrue(addedNoteDTO.isPresent());
        assertEquals(TestConstants.NOTE1_ID, addedNoteDTO.get().getId());
        assertEquals(note1InDb.toString(), addedNoteDTO.get().toString());

        verify(noteRepositoryMock, Mockito.times(1)).save(any(Note.class));
    }

    @Nested
    @DisplayName("getAllNotesForPatientPageable tests")
    class GetAllNotesForPatientPageableTests {
        @Test
        void getAllNotesForPatientPageable_withDataInDb_returnsTheListOfAllValuesForThePage() {

            List<Note> noteList = new ArrayList<>();
            noteList.add(note1InDb);
            noteList.add(note2InDb);
            Page<Note> notePage = new PageImpl<>(noteList);
            when(noteRepositoryMock.findAllByPatientId(anyInt(), any(Pageable.class))).thenReturn(notePage);

            Page<NoteDTO> noteDTOPage = noteService.getAllNotesForPatientPageable(
                TestConstants.NOTE1_PATIENT_ID, 1, 1000, "id", "asc");
            assertEquals(noteList.size(), noteDTOPage.getContent().size());
            assertEquals(note1InDb.getId(), noteDTOPage.getContent().get(0).getId());

            verify(noteRepositoryMock, Mockito.times(1))
                .findAllByPatientId(anyInt(), any(Pageable.class));
        }

        @Test
        void getAllNotesForPatientPageable_withNoDataInDb_returnsAnEmptyList() {

            List<Note> noteList = new ArrayList<>();
            Page<Note> notePage = new PageImpl<>(noteList);
            when(noteRepositoryMock.findAllByPatientId(anyInt(), any(Pageable.class))).thenReturn(notePage);

            Page<NoteDTO> noteDTOPage = noteService.getAllNotesForPatientPageable(
                TestConstants.NOTE1_PATIENT_ID, 1, 1000, "id", "asc");
            assertThat(noteDTOPage.getContent()).isEmpty();

            verify(noteRepositoryMock, Mockito.times(1))
                .findAllByPatientId(anyInt(), any(Pageable.class));
        }
    }
}
