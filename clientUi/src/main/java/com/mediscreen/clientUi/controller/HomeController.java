package com.mediscreen.clientUi.controller;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage(Model model) {

        log.debug(LogConstants.HOME_REQUEST_RECEIVED);
        return ViewNameConstants.HOME;
    }


}
