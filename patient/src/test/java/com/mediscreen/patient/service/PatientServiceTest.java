package com.mediscreen.patient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.contracts.IPatientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PatientServiceTest {

    @MockBean
    private PatientRepository patientRepositoryMock;

    @Autowired
    private IPatientService patientService;

    private static Patient patientInDb;

    @BeforeAll
    static void setUp() {

        patientInDb = new Patient();
        patientInDb.setId(TestConstants.PATIENT1_ID);
        patientInDb.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        patientInDb.setLastname(TestConstants.PATIENT1_LASTNAME);
        patientInDb.setBirthDate(TestConstants.PATIENT1_BIRTHDATE);
        patientInDb.setGender(TestConstants.PATIENT1_GENDER);
        patientInDb.setAddress(TestConstants.PATIENT1_ADDRESS);
        patientInDb.setPhone(TestConstants.PATIENT1_PHONE);

    }

    @Nested
    @DisplayName("getAllPatients tests")
    class GetAllPatients {
        @Test
        void getAllPatients_withDataInDb_returnsTheListOfAllValues() {

            List<Patient> patientList = new ArrayList<>();
            patientList.add(patientInDb);
            when(patientRepositoryMock.findAll()).thenReturn(patientList);

            List<PatientDTO> patientDTOList = patientService.getAllPatients();
            assertEquals(patientList.size(), patientDTOList.size());
            assertEquals(patientInDb.getId(), patientDTOList.get(0).getId());

            verify(patientRepositoryMock, Mockito.times(1)).findAll();
        }

        @Test
        void getAllPatients_withNoDataInDb_returnsAnEmptyList() {

            List<Patient> patientList = new ArrayList<>();
            when(patientRepositoryMock.findAll()).thenReturn(patientList);

            List<PatientDTO> patientDTOList = patientService.getAllPatients();
            assertThat(patientDTOList).isEmpty();

            verify(patientRepositoryMock, Mockito.times(1)).findAll();
        }
    }
}