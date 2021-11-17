package com.mediscreen.patient.service.contracts;

import java.util.List;
import java.util.Optional;

import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientAlreadyExistException;
import com.mediscreen.patient.exceptions.PatientDoesNotExistException;

public interface IPatientService {
    List<PatientDTO> getAllPatients();

    PatientDTO getPatientById(Integer patientId) throws PatientDoesNotExistException;

    Optional<PatientDTO> updatePatient(PatientDTO patientDtoToUpdate) throws PatientDoesNotExistException,
        PatientAlreadyExistException;
}
