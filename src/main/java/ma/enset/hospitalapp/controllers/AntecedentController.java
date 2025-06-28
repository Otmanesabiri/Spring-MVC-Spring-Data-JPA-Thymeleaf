package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/antecedents")
public class AntecedentController {

    @Autowired
    private AntecedentService antecedentService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("")
    public String listAntecedents(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                 @RequestParam(name = "dossierId", required = false) Long dossierId,
                                 @RequestParam(name = "type", required = false) String type) {
        
        Page<Antecedent> antecedentsPage;
        DossierMedical dossier = null;
        
        if (dossierId != null) {
            dossier = dossierMedicalService.getDossierMedical(dossierId);
            if (dossier != null) {
                if (keyword != null && !keyword.isEmpty()) {
                    antecedentsPage = antecedentService.findAntecedentsByDossierAndKeyword(dossier, keyword, PageRequest.of(page, size));
                } else {
                    antecedentsPage = antecedentService.findAntecedentsByDossier(dossier, PageRequest.of(page, size));
                }
            } else {
                antecedentsPage = antecedentService.findAllAntecedents(PageRequest.of(page, size));
            }
        } else if (type != null && !type.isEmpty()) {
            TypeAntecedent typeEnum = TypeAntecedent.valueOf(type);
            antecedentsPage = antecedentService.findAntecedentsByType(typeEnum, PageRequest.of(page, size));
        } else {
            antecedentsPage = antecedentService.findAllAntecedents(PageRequest.of(page, size));
        }
        
        model.addAttribute("listAntecedents", antecedentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", antecedentsPage.getTotalPages());
        model.addAttribute("totalItems", antecedentsPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("dossierId", dossierId);
        model.addAttribute("type", type);
        model.addAttribute("dossier", dossier);
        model.addAttribute("types", TypeAntecedent.values());
        
        return "antecedents";
    }

    @GetMapping("/form")
    public String formAntecedent(Model model, @RequestParam(required = false) Long dossierId) {
        model.addAttribute("antecedent", new Antecedent());
        model.addAttribute("types", TypeAntecedent.values());
        model.addAttribute("severites", SeveriteAntecedent.values());
        
        if (dossierId != null) {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            model.addAttribute("dossierSelectionne", dossier);
            model.addAttribute("dossierId", dossierId);
        }
        
        // Pour la liste déroulante des dossiers
        model.addAttribute("dossiers", dossierMedicalService.findAllDossiersMedicaux(PageRequest.of(0, 100)).getContent());
        
        return "addAntecedentForm";
    }

    @PostMapping("/save")
    public String save(Model model, @Valid Antecedent antecedent, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long dossierId) {
        
        try {
            // Ajouter les listes pour le formulaire en cas d'erreur
            model.addAttribute("types", TypeAntecedent.values());
            model.addAttribute("severites", SeveriteAntecedent.values());
            model.addAttribute("dossiers", dossierMedicalService.findAllDossiersMedicaux(PageRequest.of(0, 100)).getContent());
            
            // Validation du dossier médical
            if (dossierId == null) {
                model.addAttribute("error", "Veuillez sélectionner un dossier médical");
                return "addAntecedentForm";
            }
            
            // Récupérer le dossier médical
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            if (dossier == null) {
                model.addAttribute("error", "Dossier médical introuvable");
                return "addAntecedentForm";
            }
            
            // Assigner le dossier médical à l'antécédent
            antecedent.setDossierMedical(dossier);
            
            // Définir des valeurs par défaut si nécessaire
            if (antecedent.getSeverite() == null) {
                antecedent.setSeverite(SeveriteAntecedent.NORMAL);
            }
            
            // Vérifier les erreurs de validation
            if (bindingResult.hasErrors()) {
                StringBuilder errorMsg = new StringBuilder("Erreurs de validation: ");
                bindingResult.getAllErrors().forEach(error -> 
                    errorMsg.append(error.getDefaultMessage()).append("; "));
                model.addAttribute("error", errorMsg.toString());
                model.addAttribute("dossierSelectionne", dossier);
                model.addAttribute("dossierId", dossierId);
                return "addAntecedentForm";
            }
            
            // Sauvegarder l'antécédent
            Antecedent savedAntecedent = antecedentService.saveAntecedent(antecedent);
            
            if (savedAntecedent != null) {
                // Mettre à jour la date de dernière modification du dossier
                dossierMedicalService.updateLastModificationDate(dossier.getId());
                
                return "redirect:/antecedents?page=" + page + "&keyword=" + keyword + "&dossierId=" + dossierId;
            } else {
                model.addAttribute("error", "Erreur lors de la sauvegarde de l'antécédent");
                return "addAntecedentForm";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("types", TypeAntecedent.values());
            model.addAttribute("severites", SeveriteAntecedent.values());
            model.addAttribute("dossiers", dossierMedicalService.findAllDossiersMedicaux(PageRequest.of(0, 100)).getContent());
            return "addAntecedentForm";
        }
    }

    @GetMapping("/edit")
    public String editAntecedent(Model model, Long id, 
                                @RequestParam(defaultValue = "") String keyword, 
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(required = false) Long dossierId) {
        Antecedent antecedent = antecedentService.getAntecedent(id);
        if (antecedent == null) throw new RuntimeException("Antécédent introuvable");
        
        model.addAttribute("antecedent", antecedent);
        model.addAttribute("types", TypeAntecedent.values());
        model.addAttribute("severites", SeveriteAntecedent.values());
        model.addAttribute("dossiers", dossierMedicalService.findAllDossiersMedicaux(PageRequest.of(0, 100)).getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("dossierId", dossierId);
        return "editAntecedentForm";
    }

    @GetMapping("/delete")
    public String deleteAntecedent(Long id, String keyword, int page, Long dossierId) {
        antecedentService.deleteAntecedent(id);
        return "redirect:/antecedents?page=" + page + "&keyword=" + keyword + 
               (dossierId != null ? "&dossierId=" + dossierId : "");
    }

    @GetMapping("/activer")
    public String activerAntecedent(Long id, String keyword, int page, Long dossierId) {
        antecedentService.activerAntecedent(id);
        return "redirect:/antecedents?page=" + page + "&keyword=" + keyword + 
               (dossierId != null ? "&dossierId=" + dossierId : "");
    }

    @GetMapping("/desactiver")
    public String desactiverAntecedent(Long id, String keyword, int page, Long dossierId) {
        antecedentService.desactiverAntecedent(id);
        return "redirect:/antecedents?page=" + page + "&keyword=" + keyword + 
               (dossierId != null ? "&dossierId=" + dossierId : "");
    }

    @GetMapping("/dossier/{dossierId}")
    public String antecedentsByDossier(@PathVariable Long dossierId, Model model) {
        return "redirect:/antecedents?dossierId=" + dossierId;
    }
}
