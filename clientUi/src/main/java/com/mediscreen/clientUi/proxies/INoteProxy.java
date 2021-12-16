package com.mediscreen.clientUi.proxies;

import com.mediscreen.commons.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "note", url = "${note.feign.url}" + ":" + "${note.feign.port}")
public interface INoteProxy {

    @GetMapping(value = "/note/list")
    Page<NoteDTO> getAllNotesForPatientByPage(@RequestParam Integer patientId,
                                              @RequestParam int pageNumber, @RequestParam int itemsPerPage,
                                              @RequestParam String sortField, @RequestParam String sortDir);

    @PostMapping(value = "/note/", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    NoteDTO addNote(@RequestBody NoteDTO noteDtoToAdd);
}
