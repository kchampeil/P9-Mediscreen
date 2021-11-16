package com.mediscreen.clientUi.proxies;

import java.util.List;

import com.mediscreen.clientUi.model.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "patient", url = "${patient.feign.url}" + ":" + "${patient.feign.port}")
public interface IPatientProxy {

    @GetMapping(value = "/patient/list")
    List<PatientDTO> getAllPatients();

    @GetMapping(value = "/patient/get")
    ResponseEntity<PatientDTO> getPatientById(@RequestParam Integer patientId);

    @PutMapping(value = "/patient/update")
    ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO patientDtoToUpdate);
}
