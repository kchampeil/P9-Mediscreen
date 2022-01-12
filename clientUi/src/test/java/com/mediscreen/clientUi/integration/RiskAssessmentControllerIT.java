package com.mediscreen.clientUi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.mediscreen.clientUi.constants.TestConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RiskAssessmentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showRiskAssessmentResult_WithSuccess() throws Exception {

        mockMvc.perform(get("/assess/{id}", TestConstants.PATIENT1_ID))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("riskLevel"))
               .andExpect(view().name(ViewNameConstants.RISK_ASSESSMENT_RESULT));
    }
}
