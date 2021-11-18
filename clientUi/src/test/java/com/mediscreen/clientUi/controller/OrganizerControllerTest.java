package com.mediscreen.clientUi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import com.mediscreen.clientUi.constants.TestConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrganizerController.class)
class OrganizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPatientProxy patientProxyMock;

    private static PatientDTO patientDTO;

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
    }

    @Test
    void showAllPatients_WithSuccess() throws Exception {

        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);

        when(patientProxyMock.getAllPatients()).thenReturn(patientDTOList);

        mockMvc.perform(get("/patient/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patientDtoList"))
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_PATIENTS));

        verify(patientProxyMock, Mockito.times(1))
            .getAllPatients();
    }

    @Nested
    @DisplayName("showUpdateForm tests")
    class ShowUpdateFormTest {

        @Test
        void showUpdateForm_forExistingUser_returnsPatientUpdateFormInitialized() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/patient/update/{id}", TestConstants.PATIENT1_ID))
                   .andExpect(status().isOk())
                   .andExpect(model().attributeExists("patient"))
                   .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

            verify(patientProxyMock, Mockito.times(1))
                .getPatientById(anyInt());
        }

        @Test
        void showUpdateForm_forUnknownUser_returnsPatientListView() throws Exception {

            when(patientProxyMock.getPatientById(anyInt())).thenThrow(new PatientDoesNotExistException(
                ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(get("/patient/update/{id}", TestConstants.UNKNOWN_PATIENT_ID))
                   .andExpect(status().isFound())
                   .andExpect(redirectedUrl(ViewNameConstants.SHOW_ALL_PATIENTS));

            verify(patientProxyMock, Mockito.times(1))
                .getPatientById(anyInt());
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
                   .andExpect(redirectedUrl(ViewNameConstants.SHOW_ALL_PATIENTS));

            verify(patientProxyMock, Mockito.times(1))
                .updatePatient(any(PatientDTO.class));
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

            verify(patientProxyMock, Mockito.times(0))
                .updatePatient(any(PatientDTO.class));
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

            verify(patientProxyMock, Mockito.times(0))
                .updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_withException_returnsUpdatePatientViewWithErrorMessage() throws Exception {
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

            verify(patientProxyMock, Mockito.times(1))
                .updatePatient(any(PatientDTO.class));
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
                   .andExpect(redirectedUrl(ViewNameConstants.SHOW_ALL_PATIENTS));

            verify(patientProxyMock, Mockito.times(1))
                .addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withMissingInfo_returnsUpdatePatientViewWithErrors() throws Exception {

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

            verify(patientProxyMock, Mockito.times(0))
                .addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withInvalidInfo_returnsUpdatePatientViewWithErrors() throws Exception {

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

            verify(patientProxyMock, Mockito.times(0))
                .addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withException_returnsUpdatePatientViewWithErrorMessage() throws Exception {
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

            verify(patientProxyMock, Mockito.times(1))
                .addPatient(any(PatientDTO.class));
        }
    }
}
