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
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    private static PatientDTO patientDTO;
    private static List<NoteDTO> noteDTOList;

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

        noteDTOList = new ArrayList<>();
        NoteDTO noteDTO1 = new NoteDTO(TestConstants.NOTE1_ID, TestConstants.PATIENT1_ID, TestConstants.NOTE1_NOTE,
                                       TestConstants.NOTE1_CREATION_DATE, TestConstants.NOTE1_LAST_UPDATE_DATE);
        noteDTOList.add(noteDTO1);
        NoteDTO noteDTO2 = new NoteDTO(TestConstants.NOTE2_ID, TestConstants.PATIENT1_ID, TestConstants.NOTE2_NOTE,
                                       TestConstants.NOTE2_CREATION_DATE, TestConstants.NOTE2_LAST_UPDATE_DATE);
        noteDTOList.add(noteDTO2);
    }

    private static Stream<Arguments> provideArgsForShowAllPatientsByPageWithSuccess() {
        return Stream.of(
            Arguments.of("1", "id", "asc", "10"),
            Arguments.of("1", "id", "desc", "10")
                        );
    }

    @Test
    void showHomePageOrganizer_WithSuccess() throws Exception {
        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);
        Page<PatientDTO> patientDTOPage = new PageImpl<>(patientDTOList);

        when(patientProxyMock.getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class)))
            .thenReturn(patientDTOPage);

        mockMvc.perform(get("/organizer"))
               .andExpect(status().isOk())
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_PATIENTS))
               .andExpect(model().attribute("profile", "organizer"));

        verify(patientProxyMock, Mockito.times(1))
            .getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Test
    void showHomePageDoctor_WithSuccess() throws Exception {
        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);
        Page<PatientDTO> patientDTOPage = new PageImpl<>(patientDTOList);

        when(patientProxyMock.getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class)))
            .thenReturn(patientDTOPage);

        mockMvc.perform(get("/doctor"))
               .andExpect(status().isOk())
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_PATIENTS))
               .andExpect(model().attribute("profile", "doctor"));

        verify(patientProxyMock, Mockito.times(1))
            .getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForShowAllPatientsByPageWithSuccess")
    void showAllPatientsByPage_WithSuccess(String page, String sortField, String sortDir, String itemsPerPage)
        throws Exception {

        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);
        Page<PatientDTO> patientDTOPage = new PageImpl<>(patientDTOList);

        when(patientProxyMock.getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class)))
            .thenReturn(patientDTOPage);

        mockMvc.perform(get("/patient/list/{page}", 1)
                            .param("page", page)
                            .param("sortField", sortField)
                            .param("sortDir", sortDir)
                            .param("itemsPerPage", itemsPerPage))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patientDtoList"))
               .andExpect(model().attributeExists("totalPages"))
               .andExpect(model().attributeExists("totalItems"))
               .andExpect(model().attributeExists("itemsPerPage"))
               .andExpect(model().attributeExists("sortField"))
               .andExpect(model().attributeExists("sortDir"))
               .andExpect(model().attributeExists("reverseSortDir"))
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_PATIENTS));

        verify(patientProxyMock, Mockito.times(1))
            .getAllPatientsByPage(anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Test
    void showAddForm_WithSuccess() throws Exception {

        mockMvc.perform(get("/patient/add"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(view().name(ViewNameConstants.ADD_PATIENT));

    }

    @Nested
    @DisplayName("showUpdateForm tests")
    class ShowUpdateFormTest {

        @Test
        void showUpdateForm_forExistingPatient_returnsPatientUpdateFormInitialized() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/patient/update/{id}", TestConstants.PATIENT1_ID))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void showUpdateForm_forUnknownPatient_returnsPatientListView() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenThrow(new PatientDoesNotExistException(
                ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(get("/patient/update/{id}", TestConstants.UNKNOWN_PATIENT_ID))
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        }
    }

    @Nested
    @DisplayName("updatePatient tests")
    class UpdatePatientTest {
        @Test
        void updatePatient_withSuccess_returnsPatientListView() throws Exception {

            when(patientProxyMock.updatePatient(any(PatientDTO.class))).thenReturn(patientDTO);

            mockMvc.perform(post("/patient/update/{id}", TestConstants.PATIENT1_ID)
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(model().hasNoErrors())
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_withMissingInfo_returnsUpdatePatientViewWithErrors() throws Exception {

            mockMvc.perform(post("/patient/update/{id}", TestConstants.PATIENT1_ID)
                                .param("firstname", "")
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("patient", "firstname", "NotBlank"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

            verify(patientProxyMock, Mockito.times(0)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_withInvalidInfo_returnsUpdatePatientViewWithErrors() throws Exception {

            mockMvc.perform(post("/patient/update/{id}", TestConstants.PATIENT1_ID)
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE_IN_FUTURE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER_TOO_LONG)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("patient", "birthDate", "Past"))
                   .andExpect(model().attributeHasFieldErrorCode("patient", "gender", "Size"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

            verify(patientProxyMock, Mockito.times(0)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_forUnknownPatient_returnsUpdatePatientViewWithErrorMessage() throws Exception {
            when(patientProxyMock.updatePatient(any(PatientDTO.class))).thenThrow(new PatientDoesNotExistException(
                ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(post("/patient/update/{id}", TestConstants.UNKNOWN_PATIENT_ID)
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().attributeExists("errorMessage"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

            verify(patientProxyMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }
    }

    @Nested
    @DisplayName("addPatient tests")
    class AddPatientTest {
        @Test
        void addPatient_withSuccess_returnsPatientListView() throws Exception {

            when(patientProxyMock.addPatient(any(PatientDTO.class))).thenReturn(patientDTO);

            mockMvc.perform(post("/patient/add")
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(model().hasNoErrors())
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withMissingInfo_returnsAddPatientViewWithErrors() throws Exception {

            mockMvc.perform(post("/patient/add")
                                .param("firstname", "")
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("patient", "firstname", "NotBlank"))
                   .andExpect(view().name(ViewNameConstants.ADD_PATIENT));

            verify(patientProxyMock, Mockito.times(0)).addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withInvalidInfo_returnsAddPatientViewWithErrors() throws Exception {

            mockMvc.perform(post("/patient/add")
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE_IN_FUTURE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER_TOO_LONG)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().hasErrors())
                   .andExpect(model().attributeHasFieldErrorCode("patient", "birthDate", "Past"))
                   .andExpect(model().attributeHasFieldErrorCode("patient", "gender", "Size"))
                   .andExpect(view().name(ViewNameConstants.ADD_PATIENT));

            verify(patientProxyMock, Mockito.times(0)).addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_forExistingPatient_returnsAddPatientViewWithErrorMessage() throws Exception {
            when(patientProxyMock.addPatient(any(PatientDTO.class)))
                .thenThrow(new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS));

            mockMvc.perform(post("/patient/add")
                                .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                                .param("lastname", TestConstants.PATIENT1_LASTNAME)
                                .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                                .param("gender", TestConstants.PATIENT1_GENDER)
                                .param("address", TestConstants.PATIENT1_ADDRESS)
                                .param("phone", TestConstants.PATIENT1_PHONE))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(model().attributeExists("errorMessage"))
                   .andExpect(view().name(ViewNameConstants.ADD_PATIENT));

            verify(patientProxyMock, Mockito.times(1)).addPatient(any(PatientDTO.class));
        }
    }

    @Nested
    @DisplayName("deletePatient tests")
    class DeletePatientTest {

        @Test
        void deletePatient_forExistingPatient_returnsPatientListViewWithInfoMessage() throws Exception {

            when(patientProxyMock.deletePatientById(anyInt())).thenReturn(HttpStatus.OK.value());
            when(noteProxyMock.getAllNotesForPatient(anyInt())).thenReturn(noteDTOList);
            when(noteProxyMock.deleteNoteById(anyString())).thenReturn(HttpStatus.OK.value());

            mockMvc.perform(get("/patient/delete/{id}", TestConstants.PATIENT1_ID))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("infoMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).deletePatientById(anyInt());
            verify(noteProxyMock, Mockito.times(1)).getAllNotesForPatient(anyInt());
            verify(noteProxyMock, Mockito.atLeast(1)).deleteNoteById(anyString());
        }

        @Test
        void deletePatient_forUnknownPatient_returnsPatientListViewWithErrorMessage() throws Exception {

            when(patientProxyMock.deletePatientById(anyInt())).thenThrow(new PatientDoesNotExistException(
                ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(get("/patient/delete/{id}", TestConstants.UNKNOWN_PATIENT_ID))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("errorMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).deletePatientById(anyInt());
            verify(noteProxyMock, Mockito.times(1)).getAllNotesForPatient(anyInt());
            verify(noteProxyMock, Mockito.times(0)).deleteNoteById(anyString());
        }

        @Test
        void deleteNoteWhileDeletingPatient_forUnknownNote_returnsPatientListViewWithInfoMessage() throws Exception {

            when(noteProxyMock.getAllNotesForPatient(anyInt())).thenReturn(noteDTOList);
            when(noteProxyMock.deleteNoteById(anyString())).thenThrow(new NoteDoesNotExistException(
                ExceptionConstants.NOTE_NOT_FOUND + TestConstants.UNKNOWN_NOTE_ID));

            mockMvc.perform(get("/patient/delete/{id}", TestConstants.PATIENT1_ID))
                   .andExpect(status().isFound())
                   .andExpect(flash().attributeExists("infoMessage"))
                   .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

            verify(patientProxyMock, Mockito.times(1)).deletePatientById(anyInt());
            verify(noteProxyMock, Mockito.times(1)).getAllNotesForPatient(anyInt());
            verify(noteProxyMock, Mockito.atLeast(1)).deleteNoteById(anyString());
        }
    }
}
