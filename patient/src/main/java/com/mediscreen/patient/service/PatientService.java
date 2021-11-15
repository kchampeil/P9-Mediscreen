package com.mediscreen.patient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.patient.constants.ExceptionConstants;
import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.contracts.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class PatientService implements IPatientService {

    public final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * get all registered patients (DTO)
     *
     * @return list of patientDTO
     */
    @Override
    public List<PatientDTO> getAllPatients() {
        log.debug(LogConstants.GET_ALL_PATIENTS_SERVICE_CALL);

        List<Patient> patientList = patientRepository.findAll();

        List<PatientDTO> patientDTOList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        patientList.forEach(patient -> patientDTOList.add(modelMapper.map(patient, PatientDTO.class)));

        log.debug(LogConstants.GET_ALL_PATIENTS_SERVICE_OK, patientDTOList.size());

        return patientDTOList;
    }

    @Override
    public PatientDTO getPatientById(Integer patientId) throws PatientDoesNotExistException {
        log.debug(LogConstants.GET_PATIENT_BY_ID_SERVICE_CALL);

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isPresent()) {
            log.debug(LogConstants.GET_PATIENT_BY_ID_SERVICE_OK, patientId);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(patient.get(), PatientDTO.class);

        } else {
            log.error(LogConstants.GET_PATIENT_BY_ID_SERVICE_NOT_FOUND, patientId);
            throw new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND + patientId.toString());
        }

    }
}
