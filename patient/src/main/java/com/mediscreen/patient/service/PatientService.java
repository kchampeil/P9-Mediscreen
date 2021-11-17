package com.mediscreen.patient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mediscreen.patient.constants.ExceptionConstants;
import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.dto.PatientDTO;
import com.mediscreen.patient.exceptions.PatientAlreadyExistException;
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

    /**
     * get all patient's information  from his id
     *
     * @param patientId id of the patient
     * @return patient information (DTO)
     * @throws PatientDoesNotExistException if no patient found for the id
     */
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
            throw new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND + patientId);
        }

    }

    /**
     * update a patient
     *
     * @param patientDtoToUpdate new information for the patient
     * @return updated patient (DTO)
     * @throws PatientDoesNotExistException if no patient found to update
     * @throws PatientAlreadyExistException if one patient already exists with the same firstname, lastname and
     *                                      birthdate
     */
    @Override
    public Optional<PatientDTO> updatePatient(PatientDTO patientDtoToUpdate)
        throws PatientDoesNotExistException, PatientAlreadyExistException {
        log.debug(LogConstants.UPDATE_PATIENT_SERVICE_CALL);

        try {
            /* check if patient exists for the id */
            this.getPatientById(patientDtoToUpdate.getId());

            /* check if a patient with same firstname+lastname+birthdate already exists with another patient id */
            Optional<Patient> existingPatient =
                patientRepository.findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(
                    patientDtoToUpdate.getFirstname(), patientDtoToUpdate.getLastname(),
                    patientDtoToUpdate.getBirthDate());

            if (existingPatient.isPresent() && !patientDtoToUpdate.getId().equals(existingPatient.get().getId())) {
                log.error(LogConstants.UPDATE_PATIENT_SERVICE_ALREADY_EXISTS);
                throw new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS);

            } else {

                /* map DTO to DAO, save in repository and map back to PatientDTO for return */
                ModelMapper modelMapper = new ModelMapper();
                Patient patientToUpdate = modelMapper.map(patientDtoToUpdate, Patient.class);

                Patient updatedPatient = patientRepository.save(patientToUpdate);

                log.debug(LogConstants.UPDATE_PATIENT_SERVICE_OK, patientToUpdate.getId());
                return Optional.ofNullable(modelMapper.map(updatedPatient, PatientDTO.class));
            }

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(LogConstants.UPDATE_PATIENT_SERVICE_NOT_FOUND, patientDtoToUpdate.getId());
            throw patientDoesNotExistException;
        }
    }
}
