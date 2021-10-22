package com.mediscreen.patient.service;

import java.util.ArrayList;
import java.util.List;

import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.dto.PatientDTO;
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
}
