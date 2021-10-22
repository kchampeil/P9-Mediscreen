package com.mediscreen.patient.dto;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    private Integer id;

    @NotBlank(message = "{patient.firstname.notBlank}")
    @Size(max = 64, message = "{patient.firstname.size}")
    private String firstname;

    @NotBlank(message = "{patient.lastname.notBlank}")
    @Size(max = 64, message = "{patient.lastname.size}")
    private String lastname;

    @Past(message = "{patient.birthDate.past}")
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
}
