package com.mediscreen.patient.service.contracts;

import java.util.List;

import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientDoesNotExistException;

public interface IPatientService {
    List<PatientDTO> getAllPatients();

    PatientDTO getPatientById(Integer patientId) throws PatientDoesNotExistException;
}
