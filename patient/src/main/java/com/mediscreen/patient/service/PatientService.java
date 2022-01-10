package com.mediscreen.patient.service;

import java.util.Optional;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import com.mediscreen.patient.constants.LogConstants;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import com.mediscreen.patient.service.contracts.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class PatientService implements IPatientService {

    private static final ModelMapper modelMapper = new ModelMapper();
    public final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * get all registered patients (DTO) for a given page number, items per page
     * sorted on one given field with one direction
     *
     * @param pageNumber   page we want to retrieve the list of patients
     * @param itemsPerPage number of items per page
     * @param sortField    sorted field
     * @param sortDir      sorted direction
     * @return Page of PatientDTO
     */
    @Override
    public Page<PatientDTO> getAllPatientsPageable(int pageNumber, int itemsPerPage, String sortField,
                                                   String sortDir) {
        log.debug(LogConstants.GET_ALL_PATIENTS_PER_PAGE_SERVICE_CALL);

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, itemsPerPage, sort);

        Page<PatientDTO> patientDTOPage = patientRepository.findAll(pageable)
                                                           .map(patient -> modelMapper.map(patient, PatientDTO.class));

        log.debug(LogConstants.GET_ALL_PATIENTS_PER_PAGE_SERVICE_OK, pageNumber);

        return patientDTOPage;
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
            return modelMapper.map(patient.get(), PatientDTO.class);

        } else {
            log.error(LogConstants.PATIENT_SERVICE_NOT_FOUND, patientId);
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
    public PatientDTO updatePatient(PatientDTO patientDtoToUpdate)
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
                log.error(LogConstants.PATIENT_SERVICE_ALREADY_EXISTS);
                throw new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS);

            } else {

                Patient updatedPatient = patientRepository.save(modelMapper.map(patientDtoToUpdate, Patient.class));

                log.debug(LogConstants.UPDATE_PATIENT_SERVICE_OK, updatedPatient.getId());
                return modelMapper.map(updatedPatient, PatientDTO.class);
            }

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(LogConstants.UPDATE_PATIENT_SERVICE_NOT_FOUND, patientDtoToUpdate.getId());
            throw patientDoesNotExistException;
        }
    }

    /**
     * add a patient
     *
     * @param patientDtoToAdd information for the patient to add
     * @return added patient (DTO)
     * @throws PatientAlreadyExistException if one patient already exists with the same firstname, lastname and
     *                                      birthdate
     */
    @Override
    public PatientDTO addPatient(PatientDTO patientDtoToAdd) throws PatientAlreadyExistException {

        log.debug(LogConstants.ADD_PATIENT_SERVICE_CALL);

        /* check if a patient with same firstname+lastname+birthdate already exists */
        Optional<Patient> existingPatient =
            patientRepository.findPatientByFirstnameAndLastnameAndBirthDateAllIgnoreCase(
                patientDtoToAdd.getFirstname(), patientDtoToAdd.getLastname(),
                patientDtoToAdd.getBirthDate());

        if (existingPatient.isPresent()) {
            log.error(LogConstants.PATIENT_SERVICE_ALREADY_EXISTS);
            throw new PatientAlreadyExistException(ExceptionConstants.PATIENT_ALREADY_EXISTS);

        } else {
            Patient addedPatient = patientRepository.save(modelMapper.map(patientDtoToAdd, Patient.class));

            log.debug(LogConstants.ADD_PATIENT_SERVICE_OK, addedPatient.getId());
            return modelMapper.map(addedPatient, PatientDTO.class);
        }
    }

    /**
     * delete a patient
     *
     * @param patientId of the patient to delete
     * @throws PatientDoesNotExistException if no patient found to delete
     */
    @Override
    public void deletePatientById(Integer patientId) throws PatientDoesNotExistException {

        log.debug(LogConstants.DELETE_PATIENT_BY_ID_SERVICE_CALL);

        patientRepository.findById(patientId)
                         .orElseThrow(() -> {
                             log.error(LogConstants.PATIENT_SERVICE_NOT_FOUND, patientId);
                             return new PatientDoesNotExistException(ExceptionConstants.PATIENT_NOT_FOUND + patientId);
                         });

        patientRepository.deleteById(patientId);
    }
}
