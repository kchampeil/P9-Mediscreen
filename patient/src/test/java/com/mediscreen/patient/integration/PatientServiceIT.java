package com.mediscreen.patient.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.contracts.IPatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest
@Sql({"/sql/schema-test.sql", "/sql/insert-data-test.sql"})
public class PatientServiceIT {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void getAllPatients_withDataInDb_returnsTheListOfAllValues() {

        List<PatientDTO> patientDTOList = patientService.getAllPatients();

        assertEquals(5, patientDTOList.size());
        assertEquals(TestConstants.PATIENT1_ID, patientDTOList.get(0).getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDTOList.get(0).getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDTOList.get(0).getLastname());
    }

    @Test
    public void getPatientById_ForExistingPatient_returnsPatient() throws PatientDoesNotExistException {

        PatientDTO patientDTO = patientService.getPatientById(TestConstants.PATIENT1_ID);

        assertEquals(TestConstants.PATIENT1_ID, patientDTO.getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDTO.getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDTO.getLastname());
    }

    @Test
    public void updatePatient_ForExistingPatient_returnsUpdatedPatient()
        throws PatientDoesNotExistException, PatientAlreadyExistException {

        PatientDTO patientDtoToUpdate = new PatientDTO();
        patientDtoToUpdate.setId(TestConstants.PATIENT1_ID);
        patientDtoToUpdate.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        patientDtoToUpdate.setLastname(TestConstants.PATIENT1_LASTNAME);
        patientDtoToUpdate.setBirthDate(TestConstants.PATIENT1_BIRTHDATE);
        patientDtoToUpdate.setGender(TestConstants.PATIENT1_GENDER);
        patientDtoToUpdate.setAddress(TestConstants.PATIENT1_ADDRESS_UPDATED);
        patientDtoToUpdate.setPhone(TestConstants.PATIENT1_PHONE);

        Optional<PatientDTO> patientDTO = patientService.updatePatient(patientDtoToUpdate);

        assertTrue(patientDTO.isPresent());
        assertEquals(TestConstants.PATIENT1_ID, patientDTO.get().getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDTO.get().getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDTO.get().getLastname());
    }

    @Test
    void addPatient_withNewPatient_returnsCreatedPatient() throws PatientAlreadyExistException {

        PatientDTO patientDtoToAdd = new PatientDTO();
        patientDtoToAdd.setFirstname(TestConstants.NEW_PATIENT_FIRSTNAME);
        patientDtoToAdd.setLastname(TestConstants.NEW_PATIENT_LASTNAME);
        patientDtoToAdd.setBirthDate(TestConstants.NEW_PATIENT_BIRTHDATE);
        patientDtoToAdd.setGender(TestConstants.NEW_PATIENT_GENDER);
        patientDtoToAdd.setAddress(TestConstants.NEW_PATIENT_ADDRESS);
        patientDtoToAdd.setPhone(TestConstants.NEW_PATIENT_PHONE);

        Optional<PatientDTO> addedPatientDTO = patientService.addPatient(patientDtoToAdd);

        assertTrue(addedPatientDTO.isPresent());
        assertNotNull(addedPatientDTO.get().getId());
        patientDtoToAdd.setId(addedPatientDTO.get().getId());
        assertEquals(patientDtoToAdd.toString(), addedPatientDTO.get().toString());

        //clean DB at the end of the test by deleting the patient created in test
        patientRepository.deleteById(addedPatientDTO.get().getId());
    }
}
