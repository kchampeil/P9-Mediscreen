package com.mediscreen.commons.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskAssessmentDTO {

    public static final String RISK_ASSESSMENT_DTO_EXAMPLE = "{\n \"id\": 1,\n \"firstname\": \"Emma\",\n " +
                                                             "\"lastname\": " +
                                                             "\"Stone\",\n  " + "\"birthDate\": \"1998-11-06\",\n " +
                                                             "\"gender\":" +
                                                             " \"F\",\n " + "\"address\": \"1 rue du Paradis 75001 " +
                                                             "PARIS\",\n" +
                                                             " \"phone\": " + "\"0101010101\"\n " + "}";
    //TODO à faire car là exemple pour patient

    private PatientDTO patientDTO;

    private List<NoteDTO> noteDTOList;

    public RiskAssessmentDTO(PatientDTO patientDTO, List<NoteDTO> noteDTOList) {
        this.patientDTO = patientDTO;
        this.noteDTOList = noteDTOList;
    }
}
