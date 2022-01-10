package com.mediscreen.note.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.note.constants.TestConstants;
import com.mediscreen.note.service.contracts.INoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static NoteDTO noteDTO;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INoteService noteServiceMock;

    @BeforeAll
    static void setUp() {
        noteDTO = new NoteDTO(TestConstants.NOTE1_ID,
                              TestConstants.NOTE1_PATIENT_ID,
                              TestConstants.NOTE1_NOTE,
                              TestConstants.NOTE1_CREATION_DATE,
                              TestConstants.NOTE1_LAST_UPDATE_DATE);
    }

    @Test
    void getAllNotesByPage_returnsTheListOfAllValues_And_StatusOk() throws Exception {

        List<NoteDTO> noteDTOList = new ArrayList<>();
        noteDTOList.add(noteDTO);
        Page<NoteDTO> noteDTOPage = new PageImpl<>(noteDTOList);

        when(noteServiceMock.getAllNotesForPatientPageable(anyInt(), anyInt(), anyInt(),
                                                           any(String.class), any(String.class)))
            .thenReturn(noteDTOPage);

        mockMvc.perform(get("/note/list")
                            .param("patientId", String.valueOf(1))
                            .param("pageNumber", String.valueOf(1))
                            .param("itemsPerPage", String.valueOf(10))
                            .param("sortField", "id")
                            .param("sortDir", "asc"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isNotEmpty());

        verify(noteServiceMock, Mockito.times(1))
            .getAllNotesForPatientPageable(anyInt(), anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Nested
    @DisplayName("getNoteById tests")
    class GetNoteByIdTests {
        @Test
        void getNoteById_WithExistingNoteId_returnsExistingNote_And_StatusOk() throws Exception {

            when(noteServiceMock.getNoteById(anyString())).thenReturn(noteDTO);

            mockMvc.perform(get("/note/")
                                .param("noteId", TestConstants.NOTE1_ID))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.note", is(TestConstants.NOTE1_NOTE)));

            verify(noteServiceMock, Mockito.times(1)).getNoteById(anyString());
        }

        @Test
        void getNoteById_WithUnknownNoteId_returnsStatusNotFound() throws Exception {

            when(noteServiceMock.getNoteById(anyString()))
                .thenThrow(new NoteDoesNotExistException(
                    ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(get("/note/")
                                .param("noteId", TestConstants.UNKNOWN_NOTE_ID))
                   .andExpect(status().isNotFound());

            verify(noteServiceMock, Mockito.times(1)).getNoteById(anyString());
        }
    }

    @Nested
    @DisplayName("addNote tests")
    class AddNoteTests {

        @Test
        void addNote_ForNewNote_returnsCreatedNoteAndStatusCreated() throws Exception {
            when(noteServiceMock.addNote(any(NoteDTO.class))).thenReturn(noteDTO);

            mockMvc.perform(post("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isCreated())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.patientId", is(noteDTO.getPatientId())))
                   .andExpect(jsonPath("$.note", is(noteDTO.getNote())));

            verify(noteServiceMock, Mockito.times(1)).addNote(any(NoteDTO.class));
        }

        @Test
        void addNote_WithNoDtoInReturn_returnsStatusBadRequest() throws Exception {

            when(noteServiceMock.addNote(any(NoteDTO.class))).thenReturn(null);

            mockMvc.perform(post("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isBadRequest());

            verify(noteServiceMock, Mockito.times(1)).addNote(any(NoteDTO.class));
        }
    }

    @Nested
    @DisplayName("updateNote tests")
    class UpdateNoteTests {
        @Test
        void updateNote_ForExistingNote_returnsUpdatedNoteAndStatusOk() throws Exception {

            NoteDTO updatedNoteDto = new NoteDTO(TestConstants.NOTE1_ID,
                                                 TestConstants.NOTE1_PATIENT_ID,
                                                 TestConstants.NOTE1_NOTE_UPDATED,
                                                 TestConstants.NOTE1_CREATION_DATE,
                                                 TestConstants.NOTE1_LAST_UPDATE_DATE);

            when(noteServiceMock.updateNote(any(NoteDTO.class))).thenReturn(updatedNoteDto);

            mockMvc.perform(put("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.note", is(TestConstants.NOTE1_NOTE_UPDATED)));

            verify(noteServiceMock, Mockito.times(1)).updateNote(any(NoteDTO.class));
        }

        @Test
        void updateNote_ForUnknownNote_returnsStatusNotFound() throws Exception {

            when(noteServiceMock.updateNote(any(NoteDTO.class)))
                .thenThrow(new NoteDoesNotExistException(
                    ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(put("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isNotFound())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                    .contains(ExceptionConstants.NOTE_NOT_FOUND
                                                              + TestConstants.UNKNOWN_NOTE_ID));

            verify(noteServiceMock, Mockito.times(1)).updateNote(any(NoteDTO.class));
        }

        @Test
        void updateNote_WithNoDtoInReturn_returnsStatusBadRequest() throws Exception {

            when(noteServiceMock.updateNote(any(NoteDTO.class))).thenReturn(null);

            mockMvc.perform(put("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isBadRequest());

            verify(noteServiceMock, Mockito.times(1)).updateNote(any(NoteDTO.class));
        }
    }
}