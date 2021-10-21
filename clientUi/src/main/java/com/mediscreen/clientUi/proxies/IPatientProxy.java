package com.mediscreen.clientUi.proxies;

import java.util.List;

import com.mediscreen.clientUi.model.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "patient", url = "${patient.feign.url}" + ":" + "${patient.feign.port}")
public interface IPatientProxy {

    @GetMapping(value = "/patient/list")
    List<PatientDTO> getAllPatients();

}
