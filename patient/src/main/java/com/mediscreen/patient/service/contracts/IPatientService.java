package com.mediscreen.patient.service.contracts;

import java.util.List;

import com.mediscreen.patient.dto.PatientDTO;

public interface IPatientService {
    List<PatientDTO> getAllPatients();
}
