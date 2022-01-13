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

    @Test
    void showAddForm_WithSuccess() throws Exception {

        mockMvc.perform(get("/note/" + TestConstants.PATIENT1_ID + "/add"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(model().attributeExists("note"))
               .andExpect(view().name(ViewNameConstants.ADD_NOTE));
    }

    @Test
    void addNote_withSuccess_returnsNoteListView() throws Exception {

        mockMvc.perform(post("/note/" + TestConstants.PATIENT1_ID + "/add")
                            .param("note", TestConstants.NOTE1_NOTE))
               .andExpect(model().hasNoErrors())
               .andExpect(status().isFound())
               .andExpect(redirectedUrl("/note/" + TestConstants.PATIENT1_ID + "/list/1"));
    }

    @Test
    void showUpdateForm_forExistingNote_returnsNoteUpdateFormInitialized() throws Exception {

        mockMvc.perform(get("/note/update/{id}", TestConstants.NOTE1_ID))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("patient"))
               .andExpect(model().attributeExists("note"))
               .andExpect(view().name(ViewNameConstants.UPDATE_NOTE));
    }

    @Test
    void updateNote_withSuccess_returnsNoteListView() throws Exception {

        mockMvc.perform(post("/note/update/{id}", TestConstants.NOTE1_ID)
                            .param("patientId", TestConstants.PATIENT1_ID.toString())
                            .param("note", TestConstants.NOTE1_NOTE_UPDATED))
               .andExpect(model().hasNoErrors())
               .andExpect(status().isFound())
               .andExpect(redirectedUrl("/note/" + TestConstants.PATIENT1_ID + "/list/1"));
    }
}
