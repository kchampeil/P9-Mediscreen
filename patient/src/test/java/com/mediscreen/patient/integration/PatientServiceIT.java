package com.mediscreen.patient.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.TestConstants;
import com.mediscreen.patient.service.contracts.IPatientService;
import org.junit.jupiter.api.Test;
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

        PatientDTO oldPatientDtoToUpdate = new PatientDTO();
        oldPatientDtoToUpdate.setId(TestConstants.PATIENT1_ID);
        oldPatientDtoToUpdate.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        oldPatientDtoToUpdate.setLastname(TestConstants.PATIENT1_LASTNAME);
        oldPatientDtoToUpdate.setBirthDate(TestConstants.PATIENT1_BIRTHDATE);
        oldPatientDtoToUpdate.setGender(TestConstants.PATIENT1_GENDER);
        oldPatientDtoToUpdate.setAddress(TestConstants.PATIENT1_ADDRESS_UPDATED);
        oldPatientDtoToUpdate.setPhone(TestConstants.PATIENT1_PHONE);
        Optional<PatientDTO> patientDTO = patientService.updatePatient(oldPatientDtoToUpdate);

        assertTrue(patientDTO.isPresent());
        assertEquals(TestConstants.PATIENT1_ID, patientDTO.get().getId());
        assertEquals(TestConstants.PATIENT1_FIRSTNAME, patientDTO.get().getFirstname());
        assertEquals(TestConstants.PATIENT1_LASTNAME, patientDTO.get().getLastname());
    }
}
