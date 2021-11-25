package com.mediscreen.commons.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ApiModel(description = "DTO for patient information")
public class PatientDTO {

    public static final String PATIENT_DTO_EXAMPLE = "{\n \"id\": 1,\n \"firstname\": \"Emma\",\n \"lastname\": " +
                                                     "\"Stone\",\n  " + "\"birthDate\": \"1998-11-06\",\n \"gender\":" +
                                                     " \"F\",\n " + "\"address\": \"1 rue du Paradis 75001 PARIS\",\n" +
                                                     " \"phone\": " + "\"0101010101\"\n " + "}";

    private Integer id;

    @ApiModelProperty(value = "firstname of the patient", example = "Emma")
    @NotBlank(message = "{patient.firstname.notBlank}")
    @Size(max = 64, message = "{patient.firstname.size}")
    private String firstname;

    @ApiModelProperty(value = "lastname of the patient", example = "Stone")
    @NotBlank(message = "{patient.lastname.notBlank}")
    @Size(max = 64, message = "{patient.lastname.size}")
    private String lastname;

    @ApiModelProperty(value = "date of birth of the patient", example = "1998-11-06")
    @NotNull(message = "{patient.birthDate.notNull}")
    @Past(message = "{patient.birthDate.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @ApiModelProperty(value = "gender of the patient", example = "F")
    @NotBlank(message = "{patient.gender.notBlank}")
    @Size(max = 1, message = "{patient.gender.size}")
    private String gender;

    @ApiModelProperty(value = "address of the patient", example = "1 rue du Paradis 75001 PARIS")
    @NotBlank(message = "{patient.address.notBlank}")
    @Size(max = 150, message = "{patient.address.size}")
    private String address;

    @ApiModelProperty(value = "phone number of the patient", example = "0101010101")
    @NotBlank(message = "{patient.phone.notBlank}")
    @Size(max = 15, message = "{patient.phone.size}")
    private String phone;

    @Override
    public String toString() {
        return "{" +
               "id=" + id +
               ", firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", birthDate=" + birthDate +
               ", gender='" + gender + '\'' +
               ", address='" + address + '\'' +
               ", phone='" + phone + '\'' +
               '}';
    }
}
