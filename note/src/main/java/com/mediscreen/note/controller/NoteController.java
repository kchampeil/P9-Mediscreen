package com.mediscreen.note.controller;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.note.constants.LogConstants;
import com.mediscreen.note.service.contracts.INoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "API for CRUD operations on medical notes")
@RestController
@RequestMapping("/note") //TODO "/" à ajouter dans controller Patient aussi
public class NoteController {
    private final INoteService noteService;

    @Autowired
    public NoteController(INoteService noteService) {
        this.noteService = noteService;
    }

    @ApiOperation(
        value = "Get all registered notes for one patient by page for a given number of items per page, " +
                "sorted by one field in one direction")
    @GetMapping(value = "/list")
    public Page<NoteDTO> getAllNotesForPatientByPage(@RequestParam Integer patientId,
                                                     @RequestParam int pageNumber, @RequestParam int itemsPerPage,
                                                     @RequestParam String sortField, @RequestParam String sortDir) {
        log.debug(LogConstants.GET_ALL_NOTES_REQUEST_RECEIVED);

        return noteService.getAllNotesForPatientPageable(patientId, pageNumber, itemsPerPage, sortField, sortDir);
    }
}
