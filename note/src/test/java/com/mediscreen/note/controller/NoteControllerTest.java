package com.mediscreen.note.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.commons.dto.NoteDTO;
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


        /*TODEL noteDTO = new NoteDTO();
        noteDTO.setId(TestConstants.NOTE1_ID);
        noteDTO.setPatientId(TestConstants.NOTE1_PATIENT_ID);
        noteDTO.setNote(TestConstants.NOTE1_NOTE);
        noteDTO.setCreationDate(TestConstants.NOTE1_CREATION_DATE);

         */
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
    @DisplayName("addNote tests")
    class AddNoteTests {

        @Test
        void addNote_ForNewNote_returnsCreatedNoteAndStatusCreated() throws Exception {
            when(noteServiceMock.addNote(any(NoteDTO.class))).thenReturn(Optional.of(noteDTO));

            mockMvc.perform(post("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isCreated())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.patientId", is(noteDTO.getPatientId())))
                   .andExpect(jsonPath("$.note", is(noteDTO.getNote())));
            //.andExpect(jsonPath("$.creationDate", is(noteDTO.getCreationDate()))); TODO

            verify(noteServiceMock, Mockito.times(1)).addNote(any(NoteDTO.class));
        }

        @Test
        void addNote_WithNoDtoInReturn_returnsStatusBadRequest() throws Exception {

            when(noteServiceMock.addNote(any(NoteDTO.class))).thenReturn(Optional.empty());

            mockMvc.perform(post("/note/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noteDTO)))
                   .andExpect(status().isBadRequest());

            verify(noteServiceMock, Mockito.times(1)).addNote(any(NoteDTO.class));
        }
    }
}