package com.mediscreen.note.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
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

    private static NoteDTO note1DtoToAdd;
    private static NoteDTO note2DtoToUpdate;
    private static Note note1InDb;
    private static Note note2InDb;
    @MockBean
    private NoteRepository noteRepositoryMock;
    @Autowired
    private INoteService noteService;

    @BeforeAll
    static void setUp() {

        note1DtoToAdd = new NoteDTO(TestConstants.NOTE1_PATIENT_ID, TestConstants.NOTE1_NOTE);
        note2DtoToUpdate = new NoteDTO(TestConstants.NOTE2_ID, TestConstants.NOTE2_PATIENT_ID,
                                       TestConstants.NOTE2_NOTE, TestConstants.NOTE2_CREATION_DATE,
                                       null);
        note1InDb = new Note(TestConstants.NOTE1_ID, TestConstants.NOTE1_PATIENT_ID,
                             TestConstants.NOTE1_NOTE, TestConstants.NOTE1_CREATION_DATE,
                             TestConstants.NOTE1_LAST_UPDATE_DATE);
        note2InDb = new Note(TestConstants.NOTE2_ID, TestConstants.NOTE2_PATIENT_ID,
                             TestConstants.NOTE2_NOTE, TestConstants.NOTE2_CREATION_DATE,
                             TestConstants.NOTE2_LAST_UPDATE_DATE);
    }

    @Test
    void addNote_returnsCreatedNote() {

        when(noteRepositoryMock.save(any(Note.class))).thenReturn(note1InDb);

        NoteDTO addedNoteDTO = noteService.addNote(note1DtoToAdd);
        assertNotNull(addedNoteDTO);
        assertEquals(TestConstants.NOTE1_ID, addedNoteDTO.getId());
        assertEquals(note1InDb.toString(), addedNoteDTO.toString());

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

    @Nested
    @DisplayName("getNoteById tests")
    class GetNoteByIdTests {
        @Test
        void getNoteById_ForExistingNote_returnsNote() throws NoteDoesNotExistException {

            when(noteRepositoryMock.findById(anyString())).thenReturn(Optional.ofNullable(note1InDb));

            NoteDTO noteDTO = noteService.getNoteById(TestConstants.NOTE1_ID);
            assertEquals(TestConstants.NOTE1_ID, noteDTO.getId());
            assertEquals(note1InDb.toString(), noteDTO.toString());

            verify(noteRepositoryMock, Mockito.times(1)).findById(anyString());
        }

        @Test
        void getNoteById_ForUnknownNote_throwsNoteNotFoundException() {

            when(noteRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

            Exception exception = assertThrows(NoteDoesNotExistException.class,
                                               () -> noteService.getNoteById(TestConstants.UNKNOWN_PATIENT_ID));
            assertEquals(ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID,
                         exception.getMessage());

            verify(noteRepositoryMock, Mockito.times(1)).findById(anyString());
        }
    }

    @Nested
    @DisplayName("updateNote tests")
    class UpdateNoteTests {
        @Test
        void updateNote_ForExistingNote_returnsUpdatedNote() throws NoteDoesNotExistException {

            when(noteRepositoryMock.findById(anyString())).thenReturn(Optional.ofNullable(note2InDb));
            when(noteRepositoryMock.save(any(Note.class))).thenReturn(note2InDb);

            NoteDTO updatedNoteDTO = noteService.updateNote(note2DtoToUpdate);
            assertNotNull(updatedNoteDTO);
            assertEquals(note2InDb.getId(), updatedNoteDTO.getId());
            assertEquals(note2InDb.toString(), updatedNoteDTO.toString());

            verify(noteRepositoryMock, Mockito.times(1)).findById(anyString());
            verify(noteRepositoryMock, Mockito.times(1)).save(any(Note.class));
        }

        @Test
        void updateNote_ForUnknownNote_throwsNoteNotFoundException() {

            when(noteRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

            Exception exception = assertThrows(NoteDoesNotExistException.class,
                                               () -> noteService.updateNote(note2DtoToUpdate));
            assertEquals(ExceptionConstants.NOTE_NOT_FOUND + note2DtoToUpdate.getId(),
                         exception.getMessage());

            verify(noteRepositoryMock, Mockito.times(1)).findById(anyString());
            verify(noteRepositoryMock, Mockito.times(0)).save(any(Note.class));
        }
    }
}
