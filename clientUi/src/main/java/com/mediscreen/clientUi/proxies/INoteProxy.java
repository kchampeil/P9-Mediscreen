package com.mediscreen.clientUi.proxies;

import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "note", url = "${note.feign.url}" + ":" + "${note.feign.port}")
public interface INoteProxy {

    @GetMapping(value = "/note/list")
    Page<NoteDTO> getAllNotesForPatientByPage(@RequestParam Integer patientId,
                                              @RequestParam int pageNumber, @RequestParam int itemsPerPage,
                                              @RequestParam String sortField, @RequestParam String sortDir);

    @PostMapping(value = "/note/")
    NoteDTO addNote(@RequestBody NoteDTO noteDtoToAdd);

    @GetMapping(value = "/note/")
    NoteDTO getNoteById(@RequestParam String noteId) throws NoteDoesNotExistException;

    @PutMapping(value = "/note/")
    NoteDTO updateNote(@RequestBody NoteDTO noteDtoToUpdate) throws NoteDoesNotExistException;

    @DeleteMapping(value = "/note/")
    Integer deleteNoteById(@RequestParam String noteId) throws NoteDoesNotExistException;
}
