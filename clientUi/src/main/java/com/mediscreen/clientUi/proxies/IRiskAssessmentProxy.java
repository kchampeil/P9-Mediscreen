package com.mediscreen.clientUi.proxies;

import com.mediscreen.commons.constants.RiskLevel;
import com.mediscreen.commons.dto.RiskAssessmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "riskAssessment", url = "${riskAssessment.feign.url}" + ":" + "${riskAssessment.feign.port}")
public interface IRiskAssessmentProxy {

    @PostMapping(value = "/assess/diabetes")
    RiskLevel getDiabetesRiskForPatient(@RequestBody RiskAssessmentDTO riskAssessmentDTO);
}
