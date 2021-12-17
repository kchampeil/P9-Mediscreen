package com.mediscreen.commons.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO for medical notes on patient")
public class NoteDTO {

    private String id;

    @ApiModelProperty(value = "id of the patient", example = "1")
    private Integer patientId;

    @ApiModelProperty(value = "medical note for the patient", example = "notes de janvier sur Emma Stone")
    @NotBlank(message = "{note.note.notBlank}")
    private String note;

    @ApiModelProperty(value = "creation date of medical note", example = "2021-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd") //TODO
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate creationDate;

    @ApiModelProperty(value = "last update date of medical note", example = "2021-12-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate lastUpdateDate;

    public NoteDTO(Integer patientId, String note) {
        this.patientId = patientId;
        this.note = note;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "{" +
               "id='" + id + '\'' +
               ", patientId=" + patientId +
               ", note='" + note + '\'' +
               ", creationDate=" + creationDate +
               ", lastUpdateDate=" + lastUpdateDate +
               '}';
    }
}
