package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.AllergieService;
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
public class AllergieController {

    @Autowired
    private AllergieService allergieService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/dossier/{dossierId}/allergies")
    public String allergies(@PathVariable Long dossierId,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Page<Allergie> pageAllergies = allergieService.getAllergiesByDossier(dossierId, PageRequest.of(page, size));
            
            model.addAttribute("listAllergies", pageAllergies.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageAllergies.getTotalPages());
            model.addAttribute("totalElements", pageAllergies.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("dossier", dossier);
            
            return "allergies";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dossier/{dossierId}/allergie/new")
    public String showAddAllergieForm(@PathVariable Long dossierId, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Allergie allergie = new Allergie();
            allergie.setDossierMedical(dossier);
            
            model.addAttribute("allergie", allergie);
            model.addAttribute("dossier", dossier);
            model.addAttribute("typesAllergie", TypeAllergie.values());
            model.addAttribute("severitesAllergie", SeveriteAllergie.values());
            
            return "addAllergieForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dossier/{dossierId}/allergie/save")
    public String saveAllergie(@PathVariable Long dossierId,
                              @Valid @ModelAttribute("allergie") Allergie allergie,
                              BindingResult bindingResult,
                              Model model) {
        try {
            if (bindingResult.hasErrors()) {
                DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
                model.addAttribute("dossier", dossier);
                model.addAttribute("typesAllergie", TypeAllergie.values());
                model.addAttribute("severitesAllergie", SeveriteAllergie.values());
                return "addAllergieForm";
            }
            
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            allergie.setDossierMedical(dossier);
            
            allergieService.saveAllergie(allergie);
            return "redirect:/dossier/" + dossierId + "/allergies?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addAllergieForm";
        }
    }

    @GetMapping("/allergie/edit/{id}")
    public String showEditAllergieForm(@PathVariable Long id, Model model) {
        try {
            Allergie allergie = allergieService.getAllergieById(id);
            
            model.addAttribute("allergie", allergie);
            model.addAttribute("dossier", allergie.getDossierMedical());
            model.addAttribute("typesAllergie", TypeAllergie.values());
            model.addAttribute("severitesAllergie", SeveriteAllergie.values());
            
            return "editAllergieForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/allergie/update/{id}")
    public String updateAllergie(@PathVariable Long id,
                                @Valid @ModelAttribute("allergie") Allergie allergie,
                                BindingResult bindingResult,
                                Model model) {
        try {
            if (bindingResult.hasErrors()) {
                Allergie existingAllergie = allergieService.getAllergieById(id);
                model.addAttribute("dossier", existingAllergie.getDossierMedical());
                model.addAttribute("typesAllergie", TypeAllergie.values());
                model.addAttribute("severitesAllergie", SeveriteAllergie.values());
                return "editAllergieForm";
            }
            
            Allergie existingAllergie = allergieService.getAllergieById(id);
            allergie.setId(id);
            allergie.setDossierMedical(existingAllergie.getDossierMedical());
            
            allergieService.saveAllergie(allergie);
            return "redirect:/dossier/" + existingAllergie.getDossierMedical().getId() + "/allergies?updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
            return "editAllergieForm";
        }
    }

    @GetMapping("/allergie/delete/{id}")
    public String deleteAllergie(@PathVariable Long id, Model model) {
        try {
            Allergie allergie = allergieService.getAllergieById(id);
            Long dossierId = allergie.getDossierMedical().getId();
            
            allergieService.deleteAllergie(id);
            return "redirect:/dossier/" + dossierId + "/allergies?deleted";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "error";
        }
    }
}
