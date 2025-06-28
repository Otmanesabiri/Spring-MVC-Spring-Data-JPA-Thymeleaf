package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.NoteMedicaleService;
import ma.enset.hospitalapp.services.DossierMedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class NoteMedicaleController {

    @Autowired
    private NoteMedicaleService noteMedicaleService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/dossier/{dossierId}/notes")
    public String notes(@PathVariable Long dossierId,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Page<NoteMedicale> pageNotes = noteMedicaleService.getNotesByDossier(dossierId, PageRequest.of(page, size));
            
            model.addAttribute("listNotes", pageNotes.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageNotes.getTotalPages());
            model.addAttribute("totalElements", pageNotes.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("dossier", dossier);
            
            return "notes-medicales";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dossier/{dossierId}/note/new")
    public String showAddNoteForm(@PathVariable Long dossierId, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            NoteMedicale note = new NoteMedicale();
            note.setDossierMedical(dossier);
            
            model.addAttribute("note", note);
            model.addAttribute("dossier", dossier);
            model.addAttribute("typesNote", TypeNote.values());
            model.addAttribute("niveauxConfidentialite", NiveauConfidentialite.values());
            
            return "addNoteForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dossier/{dossierId}/note/save")
    public String saveNote(@PathVariable Long dossierId,
                          @Valid @ModelAttribute("note") NoteMedicale note,
                          BindingResult bindingResult,
                          Model model) {
        try {
            if (bindingResult.hasErrors()) {
                DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
                model.addAttribute("dossier", dossier);
                model.addAttribute("typesNote", TypeNote.values());
                model.addAttribute("niveauxConfidentialite", NiveauConfidentialite.values());
                return "addNoteForm";
            }
            
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            note.setDossierMedical(dossier);
            
            noteMedicaleService.saveNote(note);
            return "redirect:/dossier/" + dossierId + "/notes?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addNoteForm";
        }
    }

    @GetMapping("/note/edit/{id}")
    public String showEditNoteForm(@PathVariable Long id, Model model) {
        try {
            NoteMedicale note = noteMedicaleService.getNoteById(id);
            
            model.addAttribute("note", note);
            model.addAttribute("dossier", note.getDossierMedical());
            model.addAttribute("typesNote", TypeNote.values());
            model.addAttribute("niveauxConfidentialite", NiveauConfidentialite.values());
            
            return "editNoteForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/note/update/{id}")
    public String updateNote(@PathVariable Long id,
                            @Valid @ModelAttribute("note") NoteMedicale note,
                            BindingResult bindingResult,
                            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                NoteMedicale existingNote = noteMedicaleService.getNoteById(id);
                model.addAttribute("dossier", existingNote.getDossierMedical());
                model.addAttribute("typesNote", TypeNote.values());
                model.addAttribute("niveauxConfidentialite", NiveauConfidentialite.values());
                return "editNoteForm";
            }
            
            NoteMedicale existingNote = noteMedicaleService.getNoteById(id);
            note.setId(id);
            note.setDossierMedical(existingNote.getDossierMedical());
            
            noteMedicaleService.saveNote(note);
            return "redirect:/dossier/" + existingNote.getDossierMedical().getId() + "/notes?updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
            return "editNoteForm";
        }
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable Long id, Model model) {
        try {
            NoteMedicale note = noteMedicaleService.getNoteById(id);
            Long dossierId = note.getDossierMedical().getId();
            
            noteMedicaleService.deleteNote(id);
            return "redirect:/dossier/" + dossierId + "/notes?deleted";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "error";
        }
    }
}
