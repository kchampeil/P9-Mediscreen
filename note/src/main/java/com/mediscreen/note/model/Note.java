package com.mediscreen.note.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Note class contains details about medical notes on the patient
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private Integer patientId;

    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

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
