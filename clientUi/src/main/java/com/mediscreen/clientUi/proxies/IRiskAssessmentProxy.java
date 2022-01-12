package com.mediscreen.clientUi.proxies;

import com.mediscreen.commons.dto.RiskAssessmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "riskAssessment", url = "${riskAssessment.feign.url}" + ":" + "${riskAssessment.feign.port}")
public interface IRiskAssessmentProxy {

    @PostMapping(value = "/assess/")
    String getRiskForPatient(@RequestBody RiskAssessmentDTO riskAssessmentDTO);
}
