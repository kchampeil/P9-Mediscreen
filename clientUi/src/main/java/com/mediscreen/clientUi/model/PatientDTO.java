package com.mediscreen.clientUi.model;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class PatientDTO { //TODO voir comment ne pas dupliquer cette déclaration

    private Integer id;

    @NotBlank(message = "{patient.firstname.notBlank}")
    @Size(max = 64, message = "{patient.firstname.size}")
    private String firstname;

    @NotBlank(message = "{patient.lastname.notBlank}")
    @Size(max = 64, message = "{patient.lastname.size}")
    private String lastname;

    @NotNull(message = "{patient.birthDate.notNull}")
    @Past(message = "{patient.birthDate.past}")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    //TODEL @DateTimeFormat(pattern="MM/dd/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    @NotBlank(message = "{patient.gender.notBlank}")
    @Size(max = 1, message = "{patient.gender.size}")
    private String gender;

    @NotBlank(message = "{patient.address.notBlank}")
    @Size(max = 150, message = "{patient.address.size}")
    private String address;

    @NotBlank(message = "{patient.phone.notBlank}")
    @Size(max = 15, message = "{patient.phone.size}")
    private String phone;

    //TODEL
    @Override
    public String toString() {
        return "PatientDTO{" +
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
