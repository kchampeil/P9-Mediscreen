package com.mesdiscreen.patient.service;

import java.util.List;

import com.mesdiscreen.patient.dto.PatientDTO;
import com.mesdiscreen.patient.service.contracts.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class PatientService implements IPatientService {
    @Override
    public List<PatientDTO> getAllPatients() {
        return null;
    }
}
