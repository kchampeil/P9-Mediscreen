package com.mesdiscreen.patient.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.mesdiscreen.patient.constants.TestConstants;
import com.mesdiscreen.patient.dto.PatientDTO;
import com.mesdiscreen.patient.service.contracts.IPatientService;
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

}
