package com.mediscreen.note.controller;

import java.util.List;

import com.mediscreen.commons.constants.ExceptionConstants;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.note.constants.LogConstants;
import com.mediscreen.note.service.contracts.INoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Api(value = "API for CRUD operations on medical notes")
@RestController
@RequestMapping("/note")
public class NoteController {
    private static final String NOTE_DTO_EXAMPLE = "{\n" +
                                                   "    \"_id\" : \"61b85fe5fa8e508d4860c7e9\",\n" +
                                                   "    \"patientId\" : 1,\n" +
                                                   "    \"note\" : \"notes de janvier sur Emma Stone\",\n" +
                                                   "    \"creationDate\" : \"2021-01-01\"\n" +
                                                   "}";
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
        log.debug(LogConstants.GET_ALL_NOTES_BY_PAGE_REQUEST_RECEIVED);

        return noteService.getAllNotesForPatientPageable(patientId, pageNumber, itemsPerPage, sortField, sortDir);
    }

    @ApiOperation(
        value = "Get all registered notes for one patient")
    @GetMapping(value = "/all")
    public List<NoteDTO> getAllNotesForPatient(@RequestParam Integer patientId) {
        log.debug(LogConstants.GET_ALL_NOTES_REQUEST_RECEIVED);

        return noteService.getAllNotesForPatient(patientId);
    }

    @ApiOperation(value = "Get note by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Note found"),
        @ApiResponse(code = 404, message = ExceptionConstants.NOTE_NOT_FOUND)
    })
    @GetMapping(value = "/")
    public ResponseEntity<NoteDTO> getNoteById(
        @ApiParam(name = "id", type = "String", value = "id of note",
                  example = "61b85fe5fa8e508d4860c7e9", required = true)
        @RequestParam String noteId) {

        log.debug(LogConstants.GET_NOTE_BY_ID_REQUEST_RECEIVED, noteId);

        try {
            NoteDTO noteDTO = noteService.getNoteById(noteId);

            log.debug(LogConstants.GET_NOTE_BY_ID_REQUEST_OK, noteId);
            return new ResponseEntity<>(noteDTO, HttpStatus.OK);

        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(noteDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, noteDoesNotExistException.getMessage(),
                                              noteDoesNotExistException);
        }
    }

    @ApiOperation(value = "Add medical note to the patient")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Note created", examples = @Example(value =
            {@ExampleProperty(mediaType = "application/json",
                              value = NOTE_DTO_EXAMPLE)})),
    })
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NoteDTO> addNote(@ApiParam(value = "Medical note to create")
                                           @RequestBody NoteDTO noteDtoToAdd) {

        log.debug(LogConstants.ADD_NOTE_REQUEST_RECEIVED, noteDtoToAdd.getPatientId());

        NoteDTO noteDtoAdded = noteService.addNote(noteDtoToAdd);

        return noteDtoAdded != null ? new ResponseEntity<>(noteDtoAdded, HttpStatus.CREATED)
                                    : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Update medical note of the patient")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Note updated", examples = @Example(value =
            {@ExampleProperty(mediaType = "application/json",
                              value = NOTE_DTO_EXAMPLE)})),
    })
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> updateNote(@ApiParam(value = "Medical note to update")
                                              @RequestBody NoteDTO noteDtoToUpdate) {

        log.debug(LogConstants.UPDATE_NOTE_REQUEST_RECEIVED, noteDtoToUpdate.getPatientId());

        try {
            NoteDTO noteDtoUpdated = noteService.updateNote(noteDtoToUpdate);

            return noteDtoUpdated != null ? new ResponseEntity<>(noteDtoUpdated, HttpStatus.OK)
                                          : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(noteDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, noteDoesNotExistException.getMessage());
        }
    }

    @ApiOperation(value = "Delete note by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Note deleted"),
        @ApiResponse(code = 404, message = ExceptionConstants.NOTE_NOT_FOUND)
    })
    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Integer> deleteNoteById(
        @ApiParam(name = "id", type = "String", value = "id of note", example = "61b85fe5fa8e508d4860c7e9",
                  required = true)
        @RequestParam String noteId) {

        log.debug(LogConstants.DELETE_NOTE_BY_ID_REQUEST_RECEIVED, noteId);

        try {
            noteService.deleteNoteById(noteId);

            log.debug(LogConstants.DELETE_NOTE_BY_ID_REQUEST_OK, noteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(noteDoesNotExistException.getMessage() + " \n");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, noteDoesNotExistException.getMessage(),
                                              noteDoesNotExistException);
        }
    }
}
