package com.mesdiscreen.patient.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * The Patient class contains details of the patient
 */
@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false, length = 64)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 64)
    private String lastname;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false, length = 1)
    private String gender;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;
}
