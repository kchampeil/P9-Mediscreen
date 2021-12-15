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

import com.mediscreen.clientUi.constants.TestConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NoteController.class)
public class NoteControllerTest {
    private static PatientDTO patientDTO;
    private static NoteDTO noteDTO;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPatientProxy patientProxyMock;
    @MockBean
    private INoteProxy noteProxyMock;

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

        noteDTO = new NoteDTO(TestConstants.NOTE1_ID,
                              TestConstants.NOTE1_PATIENT_ID,
                              TestConstants.NOTE1_NOTE,
                              TestConstants.NOTE1_NOTE_DATE);
    }

    @Test
    void showAllNotesForPatientByPage_WithSuccess() throws Exception {

        List<NoteDTO> noteDTOList = new ArrayList<>();
        noteDTOList.add(noteDTO);
        Page<NoteDTO> noteDTOPage = new PageImpl<>(noteDTOList);

        when(patientProxyMock.getPatientById(anyInt())).thenReturn(patientDTO);
        when(noteProxyMock.getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class),
                                                       any(String.class)))
            .thenReturn(noteDTOPage);

        mockMvc.perform(get("/note/{patientId}/list/{page}", 1, 1)
                            .param("patientId", String.valueOf(noteDTO.getPatientId()))
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

        verify(patientProxyMock, Mockito.times(1))
            .getPatientById(anyInt());
        verify(noteProxyMock, Mockito.times(1))
            .getAllNotesForPatientByPage(anyInt(), anyInt(), anyInt(), any(String.class), any(String.class));
    }
}
