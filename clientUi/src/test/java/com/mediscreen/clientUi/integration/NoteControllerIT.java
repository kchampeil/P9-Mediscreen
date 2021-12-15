package com.mediscreen.clientUi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class NoteControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void showAllNotesForPatientByPage_WithSuccess() throws Exception {

        mockMvc.perform(get("/note/{patientId}/list/{page}", 1, 1)
                            .param("patientId", "1")
                            .param("page", "1")
                            .param("sortField", "id")
                            .param("sortDir", "asc")
                            .param("itemsPerPage", "10"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(model().attributeExists("noteDtoList"))
               .andExpect(model().attributeExists("totalPages"))
               .andExpect(model().attributeExists("currentPage"))
               .andExpect(model().attributeExists("totalItems"))
               .andExpect(model().attributeExists("itemsPerPage"))
               .andExpect(model().attributeExists("sortField"))
               .andExpect(model().attributeExists("sortDir"))
               .andExpect(model().attributeExists("reverseSortDir"))
               .andExpect(view().name(ViewNameConstants.SHOW_ALL_NOTES_FOR_PATIENT));
    }
}
