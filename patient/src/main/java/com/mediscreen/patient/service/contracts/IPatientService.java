package com.mediscreen.patient.service.contracts;

import java.util.List;
import java.util.Optional;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;

public interface IPatientService {
    List<PatientDTO> getAllPatients();

    PatientDTO getPatientById(Integer patientId) throws PatientDoesNotExistException;

    Optional<PatientDTO> updatePatient(PatientDTO patientDtoToUpdate) throws PatientDoesNotExistException,
        PatientAlreadyExistException;

    Optional<PatientDTO> addPatient(PatientDTO patientDtoToAdd) throws PatientAlreadyExistException;

    void deletePatientById(Integer patientId) throws PatientDoesNotExistException;
}
