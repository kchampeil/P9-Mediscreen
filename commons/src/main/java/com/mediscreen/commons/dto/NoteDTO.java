package com.mediscreen.commons.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

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
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "DTO for medical notes on patient")
public class NoteDTO {

    private String id;

    @ApiModelProperty(value = "id of the patient", example = "1")
    @NotBlank(message = "{note.patientId.notBlank}")
    private Integer patientId;

    @ApiModelProperty(value = "medical note for the patient", example = "medical notes about Emma Stone")
    @NotBlank(message = "{note.note.notBlank}")
    private String note;

    @ApiModelProperty(value = "creation date of medical note", example = "2021-11-06")
    @NotNull(message = "{note.creationDate.notNull}")
    @PastOrPresent(message = "{note.creationDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd") //TOASK pourquoi non pris en compte ?
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate creationDate;

    public NoteDTO(Integer patientId, String note, LocalDate creationDate) {
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
               '}';
    }
}
