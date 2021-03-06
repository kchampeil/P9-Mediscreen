package com.mediscreen.patient.service.contracts;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import org.springframework.data.domain.Page;

public interface IPatientService {

    Page<PatientDTO> getAllPatientsPageable(int pageNumber, int size, String sortField, String sortDir);

    PatientDTO getPatientById(Integer patientId) throws PatientDoesNotExistException;

    PatientDTO updatePatient(PatientDTO patientDtoToUpdate) throws PatientDoesNotExistException,
        PatientAlreadyExistException;
    
    PatientDTO addPatient(PatientDTO patientDtoToAdd) throws PatientAlreadyExistException;

    void deletePatientById(Integer patientId) throws PatientDoesNotExistException;
}
