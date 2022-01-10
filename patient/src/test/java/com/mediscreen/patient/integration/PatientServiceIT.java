package com.mediscreen.patient.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.contracts.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
    void getAllPatientsPageable_withDataInDb_returnsTheListOfAllValues() {

        Page<PatientDTO> patientDTOPage = patientService.getAllPatientsPageable(1, 10, "id", "asc");

        assertFalse(patientDTOPage.getContent().isEmpty());
        assertTrue(patientDTOPage.getContent().size() <= 10);
        assertEquals(TestConstants.PATIENT1_ID, patientDTOPage.getContent().get(0).getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDTOPage.getContent().get(0).getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDTOPage.getContent().get(0).getLastname());
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

        PatientDTO patientDtoUpdated = patientService.updatePatient(patientDtoToUpdate);

        assertNotNull(patientDtoUpdated);
        assertEquals(TestConstants.PATIENT1_ID, patientDtoUpdated.getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDtoUpdated.getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDtoUpdated.getLastname());
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

        PatientDTO addedPatientDTO = patientService.addPatient(patientDtoToAdd);

        assertNotNull(addedPatientDTO);
        assertNotNull(addedPatientDTO.getId());
        patientDtoToAdd.setId(addedPatientDTO.getId());
        assertEquals(patientDtoToAdd.toString(), addedPatientDTO.toString());
    }

    @Test
    public void deletePatientById_ForExistingPatient_returnsNothingButPatientNoLongerExists()
        throws PatientDoesNotExistException {

        Optional<Patient> patientToDelete = patientRepository.findById(TestConstants.PATIENT1_ID);
        assertTrue(patientToDelete.isPresent());

        patientService.deletePatientById(TestConstants.PATIENT1_ID);
        Optional<Patient> patientDeleted = patientRepository.findById(TestConstants.PATIENT1_ID);

        assertFalse(patientDeleted.isPresent());
    }
}
