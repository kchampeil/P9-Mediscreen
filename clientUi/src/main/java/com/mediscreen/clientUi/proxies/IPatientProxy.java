package com.mediscreen.clientUi.proxies;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "patient", url = "${patient.feign.url}" + ":" + "${patient.feign.port}")
public interface IPatientProxy {

    @GetMapping(value = "/patient/list")
    Page<PatientDTO> getAllPatientsByPage(@RequestParam int pageNumber, @RequestParam int itemsPerPage,
                                          @RequestParam String sortField, @RequestParam String sortDir);

    @GetMapping(value = "/patient/")
    PatientDTO getPatientById(@RequestParam Integer patientId) throws PatientDoesNotExistException;

    @PutMapping(value = "/patient/")
    PatientDTO updatePatient(@RequestBody PatientDTO patientDtoToUpdate)
        throws PatientDoesNotExistException, PatientAlreadyExistException;

    @PostMapping(value = "/patient/")
    PatientDTO addPatient(@RequestBody PatientDTO patientDtoToAdd) throws PatientAlreadyExistException;

    @DeleteMapping(value = "/patient/")
    Integer deletePatientById(@RequestParam Integer patientId) throws PatientDoesNotExistException;
}
