package com.mediscreen.patient.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.mediscreen.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(String firstname, String lastname,
                                                                                 LocalDate birthDate);
}
