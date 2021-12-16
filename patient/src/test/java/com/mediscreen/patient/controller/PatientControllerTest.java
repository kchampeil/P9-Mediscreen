package com.mediscreen.patient.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.service.contracts.IPatientService;
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

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static PatientDTO patientDTO;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPatientService patientServiceMock;

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
    void getAllPatientsByPage_returnsTheListOfAllValues_And_StatusOk() throws Exception {

        List<PatientDTO> patientDTOList = new ArrayList<>();
        patientDTOList.add(patientDTO);
        Page<PatientDTO> patientDTOPage = new PageImpl<>(patientDTOList);

        when(patientServiceMock.getAllPatientsPageable(anyInt(), anyInt(), any(String.class),
                                                       any(String.class))).thenReturn(patientDTOPage);

        mockMvc.perform(get("/patient/list")
                            .param("pageNumber", String.valueOf(1))
                            .param("itemsPerPage", String.valueOf(10))
                            .param("sortField", "id")
                            .param("sortDir", "asc"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isNotEmpty());

        verify(patientServiceMock, Mockito.times(1))
            .getAllPatientsPageable(anyInt(), anyInt(), any(String.class), any(String.class));
    }

    @Nested
    @DisplayName("getPatientById tests")
    class GetPatientByIdTests {
        @Test
        void getPatientById_WithExistingPatientId_returnsExistingPatient_And_StatusOk() throws Exception {

            when(patientServiceMock.getPatientById(anyInt())).thenReturn(patientDTO);

            mockMvc.perform(get("/patient/")
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

            mockMvc.perform(get("/patient/")
                                .param("patientId", TestConstants.UNKNOWN_PATIENT_ID.toString()))
                   .andExpect(status().isNotFound());

            verify(patientServiceMock, Mockito.times(1)).getPatientById(anyInt());
        }
    }

    @Nested
    @DisplayName("updatePatient tests")
    class UpdatePatientTests {
        @Test
        void updatePatient_ForExistingPatient_returnsUpdatedPatientAndStatusOk() throws Exception {

            PatientDTO updatedPatientDto = new PatientDTO();
            updatedPatientDto.setId(TestConstants.PATIENT1_ID);
            updatedPatientDto.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
            updatedPatientDto.setLastname(TestConstants.PATIENT1_LASTNAME);
            updatedPatientDto.setBirthDate(TestConstants.PATIENT1_BIRTHDATE);
            updatedPatientDto.setGender(TestConstants.PATIENT1_GENDER);
            updatedPatientDto.setAddress(TestConstants.PATIENT1_ADDRESS_UPDATED);
            updatedPatientDto.setPhone(TestConstants.PATIENT1_PHONE);

            when(patientServiceMock.updatePatient(any(PatientDTO.class))).thenReturn(Optional.of(
                updatedPatientDto));

            mockMvc.perform(put("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.address", is(TestConstants.PATIENT1_ADDRESS_UPDATED)));

            verify(patientServiceMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_ForUnknownPatient_returnsStatusNotFound() throws Exception {

            when(patientServiceMock.updatePatient(any(PatientDTO.class)))
                .thenThrow(new PatientDoesNotExistException(
                    ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID));

            mockMvc.perform(put("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isNotFound())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                    .contains(
                                                        ExceptionConstants.PATIENT_NOT_FOUND + TestConstants
                                                            .UNKNOWN_PATIENT_ID));

            verify(patientServiceMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_ForExistingPatientWithSameFullNameAndDob_returnsStatusConflict()
            throws Exception {

            when(patientServiceMock.updatePatient(any(PatientDTO.class)))
                .thenThrow(new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS));

            mockMvc.perform(put("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isConflict())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                    .contains(ExceptionConstants.PATIENT_ALREADY_EXISTS));

            verify(patientServiceMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }

        @Test
        void updatePatient_WithNoDtoInReturn_returnsStatusBadRequest() throws Exception {

            when(patientServiceMock.updatePatient(any(PatientDTO.class))).thenReturn(Optional.empty());

            mockMvc.perform(put("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isBadRequest());

            verify(patientServiceMock, Mockito.times(1)).updatePatient(any(PatientDTO.class));
        }
    }

    @Nested
    @DisplayName("addPatient tests")
    class AddPatientTests {

        @Test
        void addPatient_ForNewPatient_returnsCreatedPatientAndStatusCreated() throws Exception {
            when(patientServiceMock.addPatient(any(PatientDTO.class))).thenReturn(Optional.of(patientDTO));

            mockMvc.perform(post("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isCreated())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$").isNotEmpty())
                   .andExpect(jsonPath("$.address", is(TestConstants.PATIENT1_ADDRESS)))
                   .andExpect(jsonPath("$.id", is(TestConstants.PATIENT1_ID)));

            verify(patientServiceMock, Mockito.times(1))
                .addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_withExistingPatientWithSameFullNameAndDob_returnsStatusConflict()
            throws Exception {

            when(patientServiceMock.addPatient(any(PatientDTO.class)))
                .thenThrow(new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS));

            mockMvc.perform(post("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isConflict())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                    .contains(ExceptionConstants.PATIENT_ALREADY_EXISTS));

            verify(patientServiceMock, Mockito.times(1)).addPatient(any(PatientDTO.class));
        }

        @Test
        void addPatient_WithNoDtoInReturn_returnsStatusBadRequest() throws Exception {

            when(patientServiceMock.addPatient(any(PatientDTO.class))).thenReturn(Optional.empty());

            mockMvc.perform(post("/patient/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientDTO)))
                   .andExpect(status().isBadRequest());

            verify(patientServiceMock, Mockito.times(1)).addPatient(any(PatientDTO.class));
        }
    }

    @Nested
    @DisplayName("deletePatient tests")
    class DeletePatientByIdTests {
        @Test
        void deletePatient_WithExistingPatientId_returnsExistingPatient_And_StatusNoContent() throws Exception {

            mockMvc.perform(delete("/patient/")
                                .param("patientId", TestConstants.PATIENT1_ID.toString()))
                   .andExpect(status().isNoContent());

            verify(patientServiceMock, Mockito.times(1)).deletePatientById(anyInt());
        }

        @Test
        void deletePatient_WithUnknownPatientId_returnsStatusNotFound() throws Exception {

            doThrow(new PatientDoesNotExistException(
                ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID))
                .when(patientServiceMock).deletePatientById(anyInt());

            mockMvc.perform(delete("/patient/")
                                .param("patientId", TestConstants.UNKNOWN_PATIENT_ID.toString()))
                   .andExpect(status().isNotFound())
                   .andExpect(mvcResult -> mvcResult.getResolvedException().getMessage()
                                                    .contains(ExceptionConstants.PATIENT_NOT_FOUND));

            verify(patientServiceMock, Mockito.times(1)).deletePatientById(anyInt());
        }
    }
}
