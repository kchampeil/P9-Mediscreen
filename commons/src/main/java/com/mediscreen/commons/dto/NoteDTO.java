package com.mediscreen.commons.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "DTO for medical notes on patient")
public class NoteDTO {

    private String id;

    @ApiModelProperty(value = "id of the patient", example = "1")
    @NotBlank(message = "{note.patientId.notBlank}") //TODO
    private Integer patientId;

    @ApiModelProperty(value = "medical note for the patient", example = "Stone")
    @NotBlank(message = "{note.note.notBlank}") //TODO
    private String note;

    @ApiModelProperty(value = "date of medical note", example = "2021-11-06")
    @NotNull(message = "{note.noteDate.notNull}") //TODO
    @Past(message = "{note.noteDate.past}") //TODO
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate noteDate;
}
