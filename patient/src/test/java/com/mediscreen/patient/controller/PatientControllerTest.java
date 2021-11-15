package com.mediscreen.patient.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.mediscreen.patient.constants.ExceptionConstants;
import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.service.contracts.IPatientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPatientService patientServiceMock;

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
    void getAllPatients_returnsTheListOfAllValues_And_StatusOk() throws Exception {

        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);

        when(patientServiceMock.getAllPatients()).thenReturn(patientDTOList);

        mockMvc.perform(get("/patient/list"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isNotEmpty());

        verify(patientServiceMock, Mockito.times(1)).getAllPatients();
    }

    @Nested
    @DisplayName("getPatientById tests")
    class GetPatientById {
        @Test
        void getPatientById_WithExistingPatientId_returnsExistingPatient_And_StatusOk() throws Exception {

            when(patientServiceMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/patient/get")
                                .param("patientId", TestConstants.PATIENT1_ID.toString()))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.address", is(TestConstants.PATIENT1_ADDRESS)));

            verify(patientServiceMock, Mockito.times(1)).getPatientById(anyInt());
        }

        @Test
        void getPatientById_WithUnknownPatientId_returnsStatusNotFound() throws Exception {

            when(patientServiceMock.getPatientById(anyInt()))
                .thenThrow(new PatientDoesNotExistException(
                    ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(get("/patient/get")
                                .param("patientId", TestConstants.UNKNOWN_PATIENT_ID.toString()))
                   .andExpect(status().isNotFound())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                               .contains(
                                                                   ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            verify(patientServiceMock, Mockito.times(1)).getPatientById(anyInt());
        }
    }
}