package com.mediscreen.clientUi.controller;

import static com.mediscreen.clientUi.utils.MessageUtil.formatOutputMessage;

import javax.validation.Valid;

import com.mediscreen.clientUi.constants.LogConstants;
import com.mediscreen.clientUi.constants.ProfileConstants;
import com.mediscreen.clientUi.constants.ViewNameConstants;
import com.mediscreen.clientUi.proxies.INoteProxy;
import com.mediscreen.clientUi.proxies.IPatientProxy;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.exceptions.NoteDoesNotExistException;
import com.mediscreen.commons.exceptions.PatientDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class NoteController {

    private final INoteProxy noteProxy;
    private final IPatientProxy patientProxy;

    @Autowired
    public NoteController(INoteProxy noteProxy, IPatientProxy patientProxy) {
        this.noteProxy = noteProxy;
        this.patientProxy = patientProxy;
    }

    @GetMapping("/note/{patientId}/list/{page}")
    public String showAllNotesForPatientByPage(Model model, RedirectAttributes redirectAttributes,
                                               @PathVariable("patientId") Integer patientId,
                                               @PathVariable("page") int currentPage,
                                               @RequestParam(value = "sortField",
                                                             defaultValue =
                                                                 ProfileConstants.DOCTOR_DEFAULT_SORT_FIELD_FOR_NOTES) String sortField,
                                               @RequestParam(value = "sortDir",
                                                             defaultValue =
                                                                 ProfileConstants.DOCTOR_DEFAULT_SORT_DIRECTION) String sortDir,
                                               @RequestParam(value = "itemsPerPage",
                                                             defaultValue = ProfileConstants.DEFAULT_ITEMS_PER_PAGE) int itemsPerPage) {

        log.debug(LogConstants.SHOW_NOTES_PER_PAGE_REQUEST_RECEIVED, patientId, currentPage, sortField, sortDir);

        try {
            model.addAttribute("patient", patientProxy.getPatientById(patientId));

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     patientId.toString()));
            return "redirect:" + ViewNameConstants.HOME_ORGANIZER;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }

        Page<NoteDTO> noteDTOPage = noteProxy.getAllNotesForPatientByPage(patientId, currentPage, itemsPerPage,
                                                                          sortField, sortDir);

        if (noteDTOPage.isEmpty()) {
            redirectAttributes.addFlashAttribute("infoMessage",
                                                 formatOutputMessage("note.list.not.found", patientId.toString()));
            return "redirect:" + ViewNameConstants.HOME_DOCTOR;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }

        model.addAttribute("noteDtoList", noteDTOPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", noteDTOPage.getTotalPages());
        model.addAttribute("totalItems", noteDTOPage.getTotalElements());
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return ViewNameConstants.SHOW_ALL_NOTES_FOR_PATIENT;
    }

    @GetMapping("/note/{patientId}/add")
    public String showAddForm(Model model, RedirectAttributes redirectAttributes,
                              @PathVariable("patientId") Integer patientId) {

        log.debug(LogConstants.SHOW_ADD_NOTE_FORM_RECEIVED);

        try {
            model.addAttribute("patient", patientProxy.getPatientById(patientId));
            model.addAttribute("note", new NoteDTO());
            return ViewNameConstants.ADD_NOTE;

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     patientId.toString()));
            return "redirect:" + ViewNameConstants.HOME_DOCTOR;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }
    }

    @PostMapping("/note/{patientId}/add") //TODO voir si mutualisable avec l'update
    public String addNote(@PathVariable("patientId") Integer patientId,
                          @ModelAttribute("note") @Valid NoteDTO noteDTO,
                          BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.debug(LogConstants.ADD_NOTE_RECEIVED, patientId);

        try {
            PatientDTO patientDTO = patientProxy.getPatientById(patientId);

            if (result.hasErrors()) {
                log.error(LogConstants.ADD_NOTE_REQUEST_NOT_VALID);
                model.addAttribute("patient", patientDTO);
                return ViewNameConstants.ADD_NOTE;
            }

            noteDTO.setPatientId(patientId);
            noteDTO = noteProxy.addNote(noteDTO);
            log.info(LogConstants.ADD_NOTE_REQUEST_OK, noteDTO.getId());

            redirectAttributes.addFlashAttribute("infoMessage",
                                                 formatOutputMessage("note.add.ok",
                                                                     noteDTO.getCreationDate().toString()));
            return "redirect:/note/" + patientId + "/list/1";
        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     patientId.toString()));
            return "redirect:" + ViewNameConstants.HOME_DOCTOR;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }
    }

    @GetMapping("/note/update/{id}")
    public String showUpdateForm(@PathVariable("id") String noteId, Model model,
                                 RedirectAttributes redirectAttributes) {

        log.debug(LogConstants.SHOW_UPDATE_NOTE_FORM_RECEIVED, noteId);

        try {
            NoteDTO noteDTO = noteProxy.getNoteById(noteId);
            model.addAttribute("note", noteDTO);
            model.addAttribute("patient", patientProxy.getPatientById(noteDTO.getPatientId()));
            return ViewNameConstants.UPDATE_NOTE;

        } catch (NoteDoesNotExistException noteDoesNotExistException) {
            log.error(noteDoesNotExistException.getMessage() + noteId);
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("note.not.found", noteId));
            return "redirect:" + ViewNameConstants.HOME_ORGANIZER;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage() + "for note: " + noteId);
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     noteId));
            return "redirect:" + ViewNameConstants.HOME_ORGANIZER;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }
    }

    @PostMapping("/note/update/{id}")
    public String updateNote(@PathVariable("id") String noteId,
                             @ModelAttribute("note") @Valid NoteDTO noteDTO,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        log.debug(LogConstants.UPDATE_NOTE_RECEIVED, noteId);

        try {
            PatientDTO patientDTO = patientProxy.getPatientById(noteDTO.getPatientId());
            model.addAttribute("patient", patientDTO);

            if (result.hasErrors()) {
                log.error(LogConstants.UPDATE_NOTE_REQUEST_NOT_VALID + noteId + "\n");
                return ViewNameConstants.UPDATE_NOTE;
            }

            try {
                NoteDTO noteDtoUpdated = noteProxy.updateNote(noteDTO);

                log.info(LogConstants.UPDATE_NOTE_REQUEST_OK, noteId);

                redirectAttributes.addFlashAttribute("infoMessage",
                                                     formatOutputMessage("note.update.ok",
                                                                         noteDtoUpdated.getLastUpdateDate()
                                                                                       .toString()));
                return "redirect:/note/" + noteDTO.getPatientId() + "/list/1";

            } catch (NoteDoesNotExistException noteDoesNotExistException) {
                log.error(LogConstants.UPDATE_NOTE_REQUEST_KO, noteId, noteDoesNotExistException.getMessage());
                model.addAttribute("errorMessage",
                                   formatOutputMessage("note.update.ko", noteId)
                                   + noteDoesNotExistException.getMessage());

                return ViewNameConstants.UPDATE_NOTE;
            }

        } catch (PatientDoesNotExistException patientDoesNotExistException) {
            log.error(patientDoesNotExistException.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 formatOutputMessage("patient.not.found",
                                                                     noteDTO.getPatientId().toString()));
            return "redirect:" + ViewNameConstants.HOME_DOCTOR;
            //TODO à voir pour appeler une méthode qui choisi le home en fonction du profil
        }
    }
}
