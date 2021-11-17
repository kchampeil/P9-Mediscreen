package com.mediscreen.clientUi.proxies;

import java.util.List;

import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.PatientAlreadyExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "patient", url = "${patient.feign.url}" + ":" + "${patient.feign.port}")
public interface IPatientProxy {

    @GetMapping(value = "/patient/list")
    List<PatientDTO> getAllPatients();

    @GetMapping(value = "/patient/get")
    PatientDTO getPatientById(@RequestParam Integer patientId) throws PatientDoesNotExistException;

    @PutMapping(value = "/patient/update")
    PatientDTO updatePatient(@RequestBody PatientDTO oldPatientDtoToUpdate)
        throws PatientDoesNotExistException, PatientAlreadyExistException;
}
