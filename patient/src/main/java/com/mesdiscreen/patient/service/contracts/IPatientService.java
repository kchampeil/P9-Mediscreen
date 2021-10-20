package com.mesdiscreen.patient.service.contracts;

import java.util.List;

import com.mesdiscreen.patient.dto.PatientDTO;

public interface IPatientService {
    List<PatientDTO> getAllPatients();
}
