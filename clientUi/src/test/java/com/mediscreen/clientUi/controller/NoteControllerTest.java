package com.mediscreen.clientUi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mediscreen.clientUi.constants.TestConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NoteController.class)
public class NoteControllerTest {
    private static PatientDTO patientDTO;
    private static NoteDTO noteDTO;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPatientProxy patientProxyMock;
    @MockBean
    private INoteProxy noteProxyMock;

    @BeforeAll
    static void setUp() {
        patientDTO = new PatientDTO();
        patientDTO.setId(TestConstants.PATIENT1_ID);
        patientDTO.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        patientDTO.setLastname(TestConstants.PATIENT1_LASTNAME);
        patientDTO.setBirthDate(TestConstants.PATIENT1_BIRTHDATE);
        patientDTO.setGender(TestConstants.PATIENT1_GENDER);
        patientDTO.setAddress(TestConstants.PATIENT1_ADDRESS);
        patientDTO.setPhone(TestConstants.PATIENT1_PHONE);

        noteDTO = new NoteDTO(TestConstants.NOTE1_ID,
                              TestConstants.NOTE1_PATIENT_ID,
                              TestConstants.NOTE1_NOTE,
                              TestConstants.NOTE1_CREATION_DATE,
                              TestConstants.NOTE1_LAST_UPDATE_DATE);
    }

    private static Stream<Arguments> provideArgsForShowAllNotesWithSuccess() {
        return Stream.of(
            Arguments.of("1", "id", "asc", "10"),
            Arguments.of("1", "id", "desc", "10")
                        );
    }

    @ParameterizedTest
    @MethodSource("provideArgsForShowAllNotesWithSuccess")
    void showAllNotesForPatientByPage_WithSuccess(String page, String sortField, String sortDir, String itemsPerPage)
        throws Exception {

        List<NoteDTO> noteDTOList = new ArrayList<>();
        noteDTOList.add(noteDTO);
        Page<NoteDTO> noteDTOPage = new PageImpl<>(noteDTOList);

        when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
        when(noteProxyMock.getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class),
                                                       any(String.class)))
            .thenReturn(noteDTOPage);

        mockMvc.perform(get("/note/{patientId}/list/{page}", 1, 1)
                            .param("patientId", String.valueOf(noteDTO.getPatientId()))
                            .param("page", page)
                            .param("sortField", sortField)
                            .param("sortDir", sortDir)
                            .param("itemsPerPage", itemsPerPage))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(model().attributeExists("noteDtoList"))
               .andExpect(model().attributeExists("totalPages"))
               .andExpect(model().attributeExists("currentPage"))
               .andExpect(model().attributeExists("totalItems"))
               .andExpect(model().attributeExists("itemsPerPage"))
               .andExpect(model().attributeExists("sortField"))
               .andExpect(model().attributeExists("sortDir"))
               .andExpect(model().attributeExists("reverseSortDir"))
               .andExpect(model().attributeDoesNotExist("infoMessage"))
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_NOTES_FOR_PATIENT));

        verify(patientProxyMock, Mockito.times(1))
            .getPatientById(anyInt());
        verify(noteProxyMock, Mockito.times(1))
            .getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Test
    void showAllNotesForPatientByPage_WithNoNotesForPatient_returnsInfoMessage() throws Exception {

        List<NoteDTO> noteDTOList = new ArrayList<>();
        Page<NoteDTO> noteDTOPage = new PageImpl<>(noteDTOList);

        when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
        when(noteProxyMock.getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class),
                                                       any(String.class))).thenReturn(noteDTOPage);

        mockMvc.perform(get("/note/{patientId}/list/{page}", 1, 1)
                            .param("patientId", String.valueOf(noteDTO.getPatientId()))
                            .param("page", "1")
                            .param("sortField", "id")
                            .param("sortDir", "asc")
                            .param("itemsPerPage", "10"))
               .andExpect(status().isFound())
               .andExpect(flash().attributeExists("infoMessage"))
               .andExpect(redirectedUrl(ViewNameConstants.HOME_DOCTOR));
        //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil

        verify(patientProxyMock, Mockito.times(1))
            .getPatientById(anyInt());
        verify(noteProxyMock, Mockito.times(1))
            .getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Test
    void showAllNotesForPatientByPage_forUnknownPatient_returnsPatientListView() throws Exception {

        when(patientProxyMock.getPatientById(anyInt())).thenThrow(new PatientDoesNotExistException(
            ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

        mockMvc.perform(get("/note/{patientId}/list/{page}", TestConstants.UNKNOWN_PATIENT_ID, 1)
                            .param("patientId", String.valueOf(TestConstants.UNKNOWN_PATIENT_ID))
                            .param("page", "1")
                            .param("sortField", "id")
                            .param("sortDir", "asc")
                            .param("itemsPerPage", "10"))
               .andExpect(status().isFound())
               .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));
        //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil

        verify(patientProxyMock, Mockito.times(1))
            .getPatientById(anyInt());
        verify(noteProxyMock, Mockito.times(0))
            .getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Nested
    @DisplayName("showAddForm tests")
    class ShowAddFormTest {
        @Test
        void showAddForm_WithSuccess() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/note/" + noteDTO.getPatientId() + "/add"))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().attributeExists("note"))
                   .andExpect(view().name(ViewNameConstants.ADD_NOTE));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void showAddForm_forUnknownPatient_returnsPatientListViewWithErrorMessage() throws Exception {
            when(patientProxyMock.getPatientById(anyInt()))
                .thenThrow(new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND));

            mockMvc.perform(get("/note/" + noteDTO.getPatientId() + "/add"))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("errorMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_DOCTOR));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
            verify(noteProxyMock, Mockito.times(0)).addNote(any(NoteDTO.class));
        }
    }

    @Nested
    @DisplayName("addNote tests")
    class AddNoteTest {
        @Test
        void addNote_withSuccess_returnsNoteListView() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
            when(noteProxyMock.addNote(any(NoteDTO.class))).thenReturn(noteDTO);

            mockMvc.perform(post("/note/" + noteDTO.getPatientId() + "/add")
                                .param("note", TestConstants.NOTE1_NOTE))
                   .andExpect(model().hasNoErrors())
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl("/note/" + noteDTO.getPatientId() + "/list/1"));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
            verify(noteProxyMock, Mockito.times(1)).addNote(any(NoteDTO.class));
        }

        @Test
        void addNote_withMissingInfo_returnsAddNoteViewWithErrors() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(post("/note/" + noteDTO.getPatientId() + "/add")
                                .param("note", ""))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("note"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("note", "note", "NotBlank"))
                   .andExpect(view().name(ViewNameConstants.ADD_NOTE));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
            verify(noteProxyMock, Mockito.times(0)).addNote(any(NoteDTO.class));
        }

        @Test
        void addNote_forUnknownPatient_returnsPatientListViewWithErrorMessage() throws Exception {
            when(patientProxyMock.getPatientById(anyInt()))
                .thenThrow(new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND));

            mockMvc.perform(post("/note/" + noteDTO.getPatientId() + "/add")
                                .param("note", noteDTO.getNote()))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("errorMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_DOCTOR));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
            verify(noteProxyMock, Mockito.times(0)).addNote(any(NoteDTO.class));
        }
    }

    @Nested
    @DisplayName("showUpdateForm tests")
    class ShowUpdateFormTest {

        @Test
        void showUpdateForm_forExistingNote_returnsNoteUpdateFormInitialized() throws Exception {

            when(noteProxyMock.getNoteById(anyString())).thenReturn(noteDTO);
            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/note/update/{id}", TestConstants.NOTE1_ID))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().attributeExists("note"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_NOTE));

            verify(noteProxyMock, Mockito.times(1)).getNoteById(anyString());
            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void showUpdateForm_forUnknownNote_returnsNoteListView() throws Exception {

            when(noteProxyMock.getNoteById(anyString())).thenThrow(new NoteDoesNotExistException(
                ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(get("/note/update/{id}", TestConstants.UNKNOWN_NOTE_ID))
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil

            verify(noteProxyMock, Mockito.times(1)).getNoteById(anyString());
            verify(patientProxyMock, Mockito.times(0)).getPatientById(anyInt());
        }
    }

    @Nested
    @DisplayName("updateNote tests")
    class UpdateNoteTest {
        @Test
        void updateNote_withSuccess_returnsNoteListView() throws Exception {

            when(noteProxyMock.updateNote(any(NoteDTO.class))).thenReturn(noteDTO);
            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(post("/note/update/{id}", TestConstants.NOTE1_ID)
                                .param("patientId", TestConstants.NOTE1_PATIENT_ID.toString())
                                .param("note", TestConstants.NOTE1_NOTE))
                   .andExpect(model().hasNoErrors())
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl("/note/" + TestConstants.NOTE1_PATIENT_ID + "/list/1"));

            verify(noteProxyMock, Mockito.times(1)).updateNote(any(NoteDTO.class));
            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void updateNote_withMissingInfo_returnsUpdateNoteViewWithErrors() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(post("/note/update/{id}", TestConstants.NOTE1_ID)
                                .param("patientId", TestConstants.NOTE1_PATIENT_ID.toString())
                                .param("note", ""))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("note"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("note", "note", "NotBlank"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_NOTE));

            verify(noteProxyMock, Mockito.times(0)).updateNote(any(NoteDTO.class));
            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void updateNote_forUnknownNote_returnsUpdateNoteViewWithErrorMessage() throws Exception {
            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
            when(noteProxyMock.updateNote(any(NoteDTO.class))).thenThrow(new NoteDoesNotExistException(
                ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(post("/note/update/{id}", TestConstants.UNKNOWN_NOTE_ID)
                                .param("patientId", TestConstants.NOTE1_PATIENT_ID.toString())
                                .param("note", TestConstants.NOTE1_NOTE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("note"))
                   .andExpect(model().attributeExists("errorMessage"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_NOTE));

            verify(noteProxyMock, Mockito.times(1)).updateNote(any(NoteDTO.class));
            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void updateNote_forUnknownPatient_returnsUpdateNoteViewWithErrorMessage() throws Exception {
            when(patientProxyMock.getPatientById(anyInt()))
                .thenThrow(new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND));
            when(noteProxyMock.updateNote(any(NoteDTO.class))).thenThrow(new NoteDoesNotExistException(
                ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(post("/note/update/{id}", TestConstants.NOTE1_ID)
                                .param("patientId", TestConstants.UNKNOWN_PATIENT_ID.toString())
                                .param("note", TestConstants.NOTE1_NOTE))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("errorMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_DOCTOR));

            verify(noteProxyMock, Mockito.times(0)).updateNote(any(NoteDTO.class));
            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }
    }
}
