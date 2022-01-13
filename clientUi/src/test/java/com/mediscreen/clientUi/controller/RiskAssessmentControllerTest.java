package com.mediscreen.clientUi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.clientUi.constants.TestConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.clientUi.proxies.IRiskAssessmentProxy;
import com.mediscreen.commons.constants.RiskLevel;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.dto.RiskAssessmentDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RiskAssessmentController.class)
class RiskAssessmentControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static PatientDTO patientDTO;
    private static List<NoteDTO> noteDTOList;
    private static RiskAssessmentDTO riskAssessmentDTO;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPatientProxy patientProxyMock;

    @MockBean
    private INoteProxy noteProxyMock;

    @MockBean
    private IRiskAssessmentProxy riskAssessmentProxyMock;

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

        noteDTOList = new ArrayList<>();
        NoteDTO noteDTO1 = new NoteDTO(TestConstants.NOTE1_ID, TestConstants.PATIENT1_ID, TestConstants.NOTE1_NOTE,
                                       TestConstants.NOTE1_CREATION_DATE, TestConstants.NOTE1_LAST_UPDATE_DATE);
        noteDTOList.add(noteDTO1);
        NoteDTO noteDTO2 = new NoteDTO(TestConstants.NOTE2_ID, TestConstants.PATIENT1_ID, TestConstants.NOTE2_NOTE,
                                       TestConstants.NOTE2_CREATION_DATE, TestConstants.NOTE2_LAST_UPDATE_DATE);
        noteDTOList.add(noteDTO2);

        riskAssessmentDTO = new RiskAssessmentDTO(patientDTO, noteDTOList);
    }

    @Test
    void showRiskAssessmentResult_WithSuccess() throws Exception {

        when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
        when(noteProxyMock.getAllNotesForPatient(anyInt())).thenReturn(noteDTOList);
        when(riskAssessmentProxyMock.getDiabetesRiskForPatient(any(RiskAssessmentDTO.class)))
            .thenReturn(RiskLevel.IN_DANGER);

        mockMvc.perform(get("/assess/{id}", TestConstants.PATIENT1_ID))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("riskLevel"))
               .andExpect(model().attribute("riskLevel", RiskLevel.IN_DANGER))
               .andExpect(view().name(ViewNameConstants.RISK_ASSESSMENT_RESULT));

        verify(patientProxyMock, Mockito.times(1)).getPatientById(anyInt());
        verify(noteProxyMock, Mockito.times(1)).getAllNotesForPatient(anyInt());
        verify(riskAssessmentProxyMock, Mockito.times(1))
            .getDiabetesRiskForPatient(any(RiskAssessmentDTO.class));
    }
}
