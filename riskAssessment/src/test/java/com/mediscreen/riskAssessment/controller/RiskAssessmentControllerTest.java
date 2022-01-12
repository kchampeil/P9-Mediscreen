package com.mediscreen.riskAssessment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.commons.constants.RiskLevel;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.dto.RiskAssessmentDTO;
import com.mediscreen.riskAssessment.constants.TestConstants;
import com.mediscreen.riskAssessment.service.contracts.IRiskAssessmentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RiskAssessmentController.class)
class RiskAssessmentControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static RiskAssessmentDTO riskAssessmentDTO;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IRiskAssessmentService riskAssessmentServiceMock;

    @BeforeAll
    static void setUp() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(TestConstants.PATIENT1_ID);
        patientDTO.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        patientDTO.setLastname(TestConstants.PATIENT1_LASTNAME);
        patientDTO.setBirthDate(TestConstants.BIRTHDATE_ABOVE_THIRTY);
        patientDTO.setGender(TestConstants.GENDER_FEMALE);
        patientDTO.setAddress(TestConstants.PATIENT1_ADDRESS);
        patientDTO.setPhone(TestConstants.PATIENT1_PHONE);

        NoteDTO noteWithTwoRiskFactors = new NoteDTO(TestConstants.NOTE_ID3, TestConstants.NOTE_PATIENT_ID,
                                                     TestConstants.NOTES_WITH_TWO_RISK_FACTOR,
                                                     TestConstants.NOTE_CREATION_DATE,
                                                     TestConstants.NOTE_LAST_UPDATE_DATE);

        List<NoteDTO> noteDTOList = new ArrayList<>();
        noteDTOList.add(noteWithTwoRiskFactors);

        riskAssessmentDTO = new RiskAssessmentDTO(patientDTO, noteDTOList);
    }

    @Test
    void getRiskForPatient_returnsRiskLevel() throws Exception {
        when(riskAssessmentServiceMock.evaluateRisk(any(RiskAssessmentDTO.class))).thenReturn(RiskLevel.EARLY_ONSET);

        mockMvc.perform(post("/assess/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(riskAssessmentDTO)))
               .andExpect(status().isOk())
               .andExpect(content().string(RiskLevel.EARLY_ONSET.getDescription()));

        verify(riskAssessmentServiceMock, Mockito.times(1))
            .evaluateRisk(any(RiskAssessmentDTO.class));
    }
}