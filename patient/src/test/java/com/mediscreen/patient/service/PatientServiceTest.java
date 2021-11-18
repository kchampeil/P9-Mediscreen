package com.mediscreen.patient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.TestConstants;
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

    private static Patient patientInDb;
    private static PatientDTO patientDto;
    @MockBean
    private PatientRepository patientRepositoryMock;
    @Autowired
    private IPatientService patientService;

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

        patientDto = new PatientDTO();
        patientDto.setId(patientInDb.getId());
        patientDto.setFirstname(patientInDb.getFirstname());
        patientDto.setLastname(patientInDb.getLastname());
        patientDto.setBirthDate(patientInDb.getBirthDate());
        patientDto.setGender(patientInDb.getGender());
        patientDto.setAddress(patientInDb.getAddress());
        patientDto.setPhone(patientInDb.getPhone());
    }

    @Nested
    @DisplayName("getAllPatients tests")
    class GetAllPatientsTests {
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

    @Nested
    @DisplayName("getPatientById tests")
    class GetPatientByIdTests {
        @Test
        void getPatientById_ForExistingPatient_returnsPatient() throws PatientDoesNotExistException {

            when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(patientInDb));

            PatientDTO patientDTO = patientService.getPatientById(TestConstants.PATIENT1_ID);
            assertEquals(TestConstants.PATIENT1_ID, patientDTO.getId());
            assertEquals(patientInDb.toString(), patientDTO.toString());

            verify(patientRepositoryMock, Mockito.times(1)).findById(anyInt());
        }

        @Test
        void getPatientById_ForUnknownPatient_throwsPatientNotFoundException() {

            when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(PatientDoesNotExistException.class,
                                               () -> patientService.getPatientById(TestConstants.UNKNOWN_PATIENT_ID));
            assertEquals(ExceptionConstants.PATIENT_NOT_FOUND + TestConstants.UNKNOWN_PATIENT_ID,
                         exception.getMessage());

            verify(patientRepositoryMock, Mockito.times(1)).findById(anyInt());
        }
    }

    @Nested
    @DisplayName("updatePatient tests")
    class UpdatePatientTests {
        @Test
        void updatePatient_ForExistingPatient_returnsUpdatedPatient()
            throws PatientDoesNotExistException, PatientAlreadyExistException {

            when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(patientInDb));
            when(patientRepositoryMock
                     .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                                 any(LocalDate.class)))
                .thenReturn(Optional.empty());
            when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patientInDb);

            Optional<PatientDTO> updatedPatientDTO = patientService.updatePatient(patientDto);
            assertTrue(updatedPatientDTO.isPresent());
            assertEquals(TestConstants.PATIENT1_ID, updatedPatientDTO.get().getId());
            assertEquals(patientInDb.toString(), updatedPatientDTO.get().toString());

            verify(patientRepositoryMock, Mockito.times(1)).findById(anyInt());
            verify(patientRepositoryMock, Mockito.times(1))
                .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                            any(LocalDate.class));
            verify(patientRepositoryMock, Mockito.times(1)).save(any(Patient.class));
        }

        @Test
        void updatePatient_ForUnknownPatient_throwsPatientNotFoundException() {

            when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(PatientDoesNotExistException.class,
                                               () -> patientService.updatePatient(patientDto));
            assertEquals(ExceptionConstants.PATIENT_NOT_FOUND + patientDto.getId(),
                         exception.getMessage());

            verify(patientRepositoryMock, Mockito.times(1)).findById(anyInt());
            verify(patientRepositoryMock, Mockito.times(0))
                .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                            any(LocalDate.class));
            verify(patientRepositoryMock, Mockito.times(0)).save(any(Patient.class));
        }

        @Test
        void updatePatient_ForAlreadyExistingPatient_throwsPatientAlreadyExistsException() {

            Patient existingPatient = new Patient();
            existingPatient.setId(TestConstants.PATIENT2_ID);
            existingPatient.setFirstname(TestConstants.PATIENT2_FIRSTNAME);
            existingPatient.setLastname(TestConstants.PATIENT2_LASTNAME);
            existingPatient.setBirthDate(TestConstants.PATIENT2_BIRTHDATE);

            when(patientRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(patientInDb));
            when(patientRepositoryMock
                     .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(), any(
                         LocalDate.class))).thenReturn(Optional.ofNullable(existingPatient));

            Exception exception = assertThrows(PatientAlreadyExistException.class,
                                               () -> patientService.updatePatient(patientDto));
            assertEquals(ExceptionConstants.PATIENT_ALREADY_EXISTS, exception.getMessage());

            verify(patientRepositoryMock, Mockito.times(1)).findById(anyInt());
            verify(patientRepositoryMock, Mockito.times(1))
                .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                            any(LocalDate.class));
            verify(patientRepositoryMock, Mockito.times(0)).save(any(Patient.class));
        }
    }

    @Nested
    @DisplayName("addPatient tests")
    class AddPatientTests {

        @Test
        void addPatient_withNewPatient_returnsCreatedPatient() throws PatientAlreadyExistException {

            when(patientRepositoryMock.findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(),
                                                                                                  anyString(),
                                                                                                  any(LocalDate.class)))
                .thenReturn(Optional.empty());
            when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patientInDb);

            Optional<PatientDTO> addedPatientDTO = patientService.addPatient(patientDto);
            assertTrue(addedPatientDTO.isPresent());
            assertEquals(TestConstants.PATIENT1_ID, addedPatientDTO.get().getId());
            assertEquals(patientInDb.toString(), addedPatientDTO.get().toString());

            verify(patientRepositoryMock, Mockito.times(1))
                .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                            any(LocalDate.class));
            verify(patientRepositoryMock, Mockito.times(1)).save(any(Patient.class));
        }

        @Test
        void addPatient_withExistingPatientWithSameFullNameAndDob_throwsPatientAlreadyExistsException() {

            Patient existingPatient = new Patient();
            existingPatient.setId(TestConstants.PATIENT2_ID);
            existingPatient.setFirstname(TestConstants.PATIENT2_FIRSTNAME);
            existingPatient.setLastname(TestConstants.PATIENT2_LASTNAME);
            existingPatient.setBirthDate(TestConstants.PATIENT2_BIRTHDATE);

            when(patientRepositoryMock
                     .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(), any(
                         LocalDate.class))).thenReturn(Optional.ofNullable(existingPatient));

            Exception exception = assertThrows(PatientAlreadyExistException.class,
                                               () -> patientService.addPatient(patientDto));
            assertEquals(ExceptionConstants.PATIENT_ALREADY_EXISTS, exception.getMessage());

            verify(patientRepositoryMock, Mockito.times(1))
                .findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(anyString(), anyString(),
                                                                            any(LocalDate.class));
            verify(patientRepositoryMock, Mockito.times(0)).save(any(Patient.class));
        }
    }
}