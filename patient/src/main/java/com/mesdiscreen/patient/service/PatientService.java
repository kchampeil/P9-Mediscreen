package com.mesdiscreen.patient.service;

import java.util.ArrayList;
import java.util.List;

import com.mesdiscreen.patient.constants.LogConstants;
import com.mesdiscreen.patient.dto.PatientDTO;
import com.mesdiscreen.patient.model.Patient;
import com.mesdiscreen.patient.repository.PatientRepository;
import com.mesdiscreen.patient.service.contracts.IPatientService;
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
