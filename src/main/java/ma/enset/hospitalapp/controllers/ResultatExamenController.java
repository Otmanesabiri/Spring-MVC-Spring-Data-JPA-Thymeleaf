package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.ResultatExamenService;
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
public class ResultatExamenController {

    @Autowired
    private ResultatExamenService resultatExamenService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/dossier/{dossierId}/examens")
    public String examens(@PathVariable Long dossierId,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size,
                         @RequestParam(name = "keyword", defaultValue = "") String keyword,
                         Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Page<ResultatExamen> pageExamens = resultatExamenService.getResultatsByDossier(dossierId, PageRequest.of(page, size));
            
            model.addAttribute("listExamens", pageExamens.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageExamens.getTotalPages());
            model.addAttribute("totalElements", pageExamens.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("dossier", dossier);
            
            return "examens";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dossier/{dossierId}/examen/new")
    public String showAddExamenForm(@PathVariable Long dossierId, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            ResultatExamen examen = new ResultatExamen();
            examen.setDossierMedical(dossier);
            
            model.addAttribute("examen", examen);
            model.addAttribute("dossier", dossier);
            model.addAttribute("typesExamen", TypeExamen.values());
            model.addAttribute("statutsResultat", StatutResultat.values());
            
            return "addExamenForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dossier/{dossierId}/examen/save")
    public String saveExamen(@PathVariable Long dossierId,
                            @Valid @ModelAttribute("examen") ResultatExamen examen,
                            BindingResult bindingResult,
                            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
                model.addAttribute("dossier", dossier);
                model.addAttribute("typesExamen", TypeExamen.values());
                model.addAttribute("statutsResultat", StatutResultat.values());
                return "addExamenForm";
            }
            
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            examen.setDossierMedical(dossier);
            
            resultatExamenService.saveResultat(examen);
            return "redirect:/dossier/" + dossierId + "/examens?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addExamenForm";
        }
    }

    @GetMapping("/examen/edit/{id}")
    public String showEditExamenForm(@PathVariable Long id, Model model) {
        try {
            ResultatExamen examen = resultatExamenService.getResultatById(id);
            
            model.addAttribute("examen", examen);
            model.addAttribute("dossier", examen.getDossierMedical());
            model.addAttribute("typesExamen", TypeExamen.values());
            model.addAttribute("statutsResultat", StatutResultat.values());
            
            return "editExamenForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/examen/update/{id}")
    public String updateExamen(@PathVariable Long id,
                              @Valid @ModelAttribute("examen") ResultatExamen examen,
                              BindingResult bindingResult,
                              Model model) {
        try {
            if (bindingResult.hasErrors()) {
                ResultatExamen existingExamen = resultatExamenService.getResultatById(id);
                model.addAttribute("dossier", existingExamen.getDossierMedical());
                model.addAttribute("typesExamen", TypeExamen.values());
                model.addAttribute("statutsResultat", StatutResultat.values());
                return "editExamenForm";
            }
            
            ResultatExamen existingExamen = resultatExamenService.getResultatById(id);
            examen.setId(id);
            examen.setDossierMedical(existingExamen.getDossierMedical());
            
            resultatExamenService.saveResultat(examen);
            return "redirect:/dossier/" + existingExamen.getDossierMedical().getId() + "/examens?updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
            return "editExamenForm";
        }
    }

    @GetMapping("/examen/delete/{id}")
    public String deleteExamen(@PathVariable Long id, Model model) {
        try {
            ResultatExamen examen = resultatExamenService.getResultatById(id);
            Long dossierId = examen.getDossierMedical().getId();
            
            resultatExamenService.deleteResultat(id);
            return "redirect:/dossier/" + dossierId + "/examens?deleted";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "error";
        }
    }
}
