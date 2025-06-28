package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.HistoriqueMedicalService;
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
public class HistoriqueMedicalController {

    @Autowired
    private HistoriqueMedicalService historiqueMedicalService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/dossier/{dossierId}/historiques")
    public String historiques(@PathVariable Long dossierId,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size,
                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                             Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Page<HistoriqueMedical> pageHistoriques = historiqueMedicalService.getHistoriquesByDossier(dossierId, PageRequest.of(page, size));
            
            model.addAttribute("listHistoriques", pageHistoriques.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageHistoriques.getTotalPages());
            model.addAttribute("totalElements", pageHistoriques.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("dossier", dossier);
            
            return "historiques-medicaux";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dossier/{dossierId}/historique/new")
    public String showAddHistoriqueForm(@PathVariable Long dossierId, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            HistoriqueMedical historique = new HistoriqueMedical();
            historique.setDossierMedical(dossier);
            
            model.addAttribute("historique", historique);
            model.addAttribute("dossier", dossier);
            model.addAttribute("typesEvenement", TypeEvenementMedical.values());
            model.addAttribute("niveauxGravite", NiveauGravite.values());
            
            return "addHistoriqueForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dossier/{dossierId}/historique/save")
    public String saveHistorique(@PathVariable Long dossierId,
                                @Valid @ModelAttribute("historique") HistoriqueMedical historique,
                                BindingResult bindingResult,
                                Model model) {
        try {
            if (bindingResult.hasErrors()) {
                DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
                model.addAttribute("dossier", dossier);
                model.addAttribute("typesEvenement", TypeEvenementMedical.values());
                model.addAttribute("niveauxGravite", NiveauGravite.values());
                return "addHistoriqueForm";
            }
            
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            historique.setDossierMedical(dossier);
            
            historiqueMedicalService.saveHistorique(historique);
            return "redirect:/dossier/" + dossierId + "/historiques?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addHistoriqueForm";
        }
    }

    @GetMapping("/historique/edit/{id}")
    public String showEditHistoriqueForm(@PathVariable Long id, Model model) {
        try {
            HistoriqueMedical historique = historiqueMedicalService.getHistoriqueById(id);
            
            model.addAttribute("historique", historique);
            model.addAttribute("dossier", historique.getDossierMedical());
            model.addAttribute("typesEvenement", TypeEvenementMedical.values());
            model.addAttribute("niveauxGravite", NiveauGravite.values());
            
            return "editHistoriqueForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/historique/update/{id}")
    public String updateHistorique(@PathVariable Long id,
                                  @Valid @ModelAttribute("historique") HistoriqueMedical historique,
                                  BindingResult bindingResult,
                                  Model model) {
        try {
            if (bindingResult.hasErrors()) {
                HistoriqueMedical existingHistorique = historiqueMedicalService.getHistoriqueById(id);
                model.addAttribute("dossier", existingHistorique.getDossierMedical());
                model.addAttribute("typesEvenement", TypeEvenementMedical.values());
                model.addAttribute("niveauxGravite", NiveauGravite.values());
                return "editHistoriqueForm";
            }
            
            HistoriqueMedical existingHistorique = historiqueMedicalService.getHistoriqueById(id);
            historique.setId(id);
            historique.setDossierMedical(existingHistorique.getDossierMedical());
            
            historiqueMedicalService.saveHistorique(historique);
            return "redirect:/dossier/" + existingHistorique.getDossierMedical().getId() + "/historiques?updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
            return "editHistoriqueForm";
        }
    }

    @GetMapping("/historique/delete/{id}")
    public String deleteHistorique(@PathVariable Long id, Model model) {
        try {
            HistoriqueMedical historique = historiqueMedicalService.getHistoriqueById(id);
            Long dossierId = historique.getDossierMedical().getId();
            
            historiqueMedicalService.deleteHistorique(id);
            return "redirect:/dossier/" + dossierId + "/historiques?deleted";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "error";
        }
    }
}
