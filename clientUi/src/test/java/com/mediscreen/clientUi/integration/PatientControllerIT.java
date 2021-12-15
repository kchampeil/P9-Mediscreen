package com.mediscreen.clientUi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
public class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showAllPatientsByPage_WithSuccess() throws Exception {

        mockMvc.perform(get("/patient/list/{page}", 1)
                            .param("page", "1")
                            .param("sortField", "id")
                            .param("sortDir", "asc")
                            .param("itemsPerPage", "10"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patientDtoList"))
               .andExpect(model().attributeExists("totalPages"))
               .andExpect(model().attributeExists("totalItems"))
               .andExpect(model().attributeExists("itemsPerPage"))
               .andExpect(model().attributeExists("sortField"))
               .andExpect(model().attributeExists("sortDir"))
               .andExpect(model().attributeExists("reverseSortDir"))
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_PATIENTS));
    }

    @Test
    void showUpdateForm_forExistingUser_returnsPatientUpdateFormInitialized() throws Exception {

        mockMvc.perform(get("/patient/update/{id}", TestConstants.PATIENT1_ID))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(view().name(ViewNameConstants.UPDATE_PATIENT));

    }

    @Test
    void updatePatient_withSuccess_returnsPatientListView() throws Exception {

        mockMvc.perform(post("/patient/update/{id}", TestConstants.PATIENT1_ID)
                            .param("firstname", TestConstants.PATIENT1_FIRSTNAME)
                            .param("lastname", TestConstants.PATIENT1_LASTNAME)
                            .param("birthDate", TestConstants.PATIENT1_BIRTHDATE.toString())
                            .param("gender", TestConstants.PATIENT1_GENDER)
                            .param("address", TestConstants.PATIENT1_ADDRESS)
                            .param("phone", TestConstants.PATIENT1_PHONE))
               .andExpect(model().hasNoErrors())
               .andExpect(status().isFound())
               .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));
    }

    @Test
    void addPatient_withSuccess_returnsPatientListView() throws Exception {

        mockMvc.perform(post("/patient/add")
                            .param("firstname", TestConstants.NEW_PATIENT_FIRSTNAME)
                            .param("lastname", TestConstants.NEW_PATIENT_LASTNAME)
                            .param("birthDate", TestConstants.NEW_PATIENT_BIRTHDATE.toString())
                            .param("gender", TestConstants.NEW_PATIENT_GENDER)
                            .param("address", TestConstants.NEW_PATIENT_ADDRESS)
                            .param("phone", TestConstants.NEW_PATIENT_PHONE))
               .andExpect(model().hasNoErrors())
               .andExpect(status().isFound())
               .andExpect(redirectedUrl(ViewNameConstants.HOME_ORGANIZER));

        //TOASK How-to delete patient after test

    }
}
