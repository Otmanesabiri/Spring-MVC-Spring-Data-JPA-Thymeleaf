package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/dossiers-medicaux")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;
    
    @Autowired
    private PatientService patientService;

    @GetMapping("")
    public String listDossiersMedicaux(Model model,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "5") int size,
                                      @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                      @RequestParam(name = "statut", required = false) String statut) {
        
        Page<DossierMedical> dossiersPage;
        
        if (statut != null && !statut.isEmpty()) {
            StatutDossier statutEnum = StatutDossier.valueOf(statut);
            dossiersPage = dossierMedicalService.findDossiersByStatut(statutEnum, PageRequest.of(page, size));
        } else if (keyword != null && !keyword.isEmpty()) {
            dossiersPage = dossierMedicalService.findDossiersByKeyword(keyword, PageRequest.of(page, size));
        } else {
            dossiersPage = dossierMedicalService.findAllDossiersMedicaux(PageRequest.of(page, size));
        }
        
        model.addAttribute("listDossiers", dossiersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dossiersPage.getTotalPages());
        model.addAttribute("totalItems", dossiersPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("statut", statut);
        model.addAttribute("statuts", StatutDossier.values());
        
        return "dossiers-medicaux";
    }

    @GetMapping("/form")
    public String formDossierMedical(Model model) {
        model.addAttribute("dossierMedical", new DossierMedical());
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutDossier.values());
        return "addDossierMedicalForm";
    }

    @PostMapping("/save")
    public String save(Model model, DossierMedical dossierMedical, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long patientId) {
        
        try {
            // Ajouter les listes pour le formulaire en cas d'erreur
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutDossier.values());
            
            // Validation du patient
            if (patientId == null) {
                model.addAttribute("error", "Veuillez sélectionner un patient");
                return "addDossierMedicalForm";
            }
            
            // Récupérer le patient
            Patient patient = patientService.getPatient(patientId);
            if (patient == null) {
                model.addAttribute("error", "Patient introuvable");
                return "addDossierMedicalForm";
            }
            
            // Vérifier si un dossier existe déjà pour ce patient
            if (dossierMedical.getId() == null && dossierMedicalService.isDossierExistForPatient(patient)) {
                model.addAttribute("error", "Un dossier médical existe déjà pour ce patient");
                return "addDossierMedicalForm";
            }
            
            // Assigner le patient au dossier
            dossierMedical.setPatient(patient);
            
            // Générer automatiquement le numéro de dossier si vide
            if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().trim().isEmpty()) {
                String numeroGenere = "DOS-" + System.currentTimeMillis();
                dossierMedical.setNumeroDossier(numeroGenere);
            }
            
            // Définir des valeurs par défaut si nécessaire
            if (dossierMedical.getStatut() == null) {
                dossierMedical.setStatut(StatutDossier.ACTIF);
            }
            
            // Définir la date de création si elle n'est pas définie
            if (dossierMedical.getDateCreation() == null) {
                dossierMedical.setDateCreation(new Date());
            }
            
            // Validation manuelle des champs obligatoires
            if (dossierMedical.getPatient() == null) {
                model.addAttribute("error", "Le patient est obligatoire");
                return "addDossierMedicalForm";
            }
            
            if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().trim().isEmpty()) {
                model.addAttribute("error", "Le numéro de dossier est obligatoire");
                return "addDossierMedicalForm";
            }
            
            if (dossierMedical.getDateCreation() == null) {
                model.addAttribute("error", "La date de création est obligatoire");
                return "addDossierMedicalForm";
            }
            
            // Sauvegarder le dossier
            DossierMedical savedDossier = dossierMedicalService.saveDossierMedical(dossierMedical);
            
            if (savedDossier != null) {
                return "redirect:/dossiers-medicaux?page=" + page + "&keyword=" + keyword;
            } else {
                model.addAttribute("error", "Erreur lors de la sauvegarde du dossier médical");
                return "addDossierMedicalForm";
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // Pour déboguer dans la console
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutDossier.values());
            return "addDossierMedicalForm";
        }
    }

    @GetMapping("/edit")
    public String editDossierMedical(Model model, Long id, 
                                    @RequestParam(defaultValue = "") String keyword, 
                                    @RequestParam(defaultValue = "0") int page) {
        DossierMedical dossier = dossierMedicalService.getDossierMedical(id);
        if (dossier == null) throw new RuntimeException("Dossier médical introuvable");
        
        model.addAttribute("dossierMedical", dossier);
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutDossier.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editDossierMedicalForm";
    }

    @GetMapping("/details")
    public String detailsDossierMedical(Model model, Long id) {
        DossierMedical dossier = dossierMedicalService.getDossierMedical(id);
        if (dossier == null) throw new RuntimeException("Dossier médical introuvable");
        
        model.addAttribute("dossier", dossier);
        return "detailsDossierMedical";
    }

    @GetMapping("/delete")
    public String deleteDossierMedical(Long id, String keyword, int page) {
        dossierMedicalService.deleteDossierMedical(id);
        return "redirect:/dossiers-medicaux?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/archiver")
    public String archiverDossier(Long id, String keyword, int page) {
        dossierMedicalService.archiverDossier(id);
        return "redirect:/dossiers-medicaux?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/activer")
    public String activerDossier(Long id, String keyword, int page) {
        dossierMedicalService.activerDossier(id);
        return "redirect:/dossiers-medicaux?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/patient/{patientId}")
    public String dossierByPatient(@PathVariable Long patientId, Model model) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient introuvable");
        }
        
        DossierMedical dossier = dossierMedicalService.findByPatient(patient);
        if (dossier == null) {
            // Rediriger vers la création d'un nouveau dossier
            return "redirect:/dossiers-medicaux/form?patientId=" + patientId;
        }
        
        return "redirect:/dossiers-medicaux/details?id=" + dossier.getId();
    }
    
    @GetMapping("/dossier-medical/{id}")
    public String viewDossierDetails(@PathVariable Long id, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(id);
            
            model.addAttribute("dossier", dossier);
            
            // TODO: Ajouter les données récentes des sous-modules si nécessaire
            // model.addAttribute("recentHistoriques", historiqueService.getRecentByDossier(id, 5));
            // model.addAttribute("recentPrescriptions", prescriptionService.getActiveByDossier(id));
            
            return "dossier-medical-details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/update")
    public String updateDossierMedical(Model model, DossierMedical dossierMedical, BindingResult bindingResult,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "") String keyword,
                                      @RequestParam(required = false) Long patientId) {
        
        try {
            // Ajouter les listes pour le formulaire en cas d'erreur
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutDossier.values());
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
            
            // Validation du patient
            if (patientId == null) {
                model.addAttribute("error", "Veuillez sélectionner un patient");
                return "editDossierMedicalForm";
            }
            
            // Récupérer le patient
            Patient patient = patientService.getPatient(patientId);
            if (patient == null) {
                model.addAttribute("error", "Patient introuvable");
                return "editDossierMedicalForm";
            }
            
            // Assigner le patient au dossier
            dossierMedical.setPatient(patient);
            
            // Assurer que le numéro de dossier n'est pas vide
            if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().trim().isEmpty()) {
                String numeroGenere = "DOS-" + System.currentTimeMillis();
                dossierMedical.setNumeroDossier(numeroGenere);
            }
            
            // Définir des valeurs par défaut si nécessaire
            if (dossierMedical.getStatut() == null) {
                dossierMedical.setStatut(StatutDossier.ACTIF);
            }
            
            // Mettre à jour la date de modification
            dossierMedical.setDateDerniereModification(new Date());
            
            // Validation manuelle des champs obligatoires
            if (dossierMedical.getPatient() == null) {
                model.addAttribute("error", "Le patient est obligatoire");
                return "editDossierMedicalForm";
            }
            
            if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().trim().isEmpty()) {
                model.addAttribute("error", "Le numéro de dossier est obligatoire");
                return "editDossierMedicalForm";
            }
            
            // Sauvegarder le dossier
            DossierMedical savedDossier = dossierMedicalService.saveDossierMedical(dossierMedical);
            
            if (savedDossier != null) {
                return "redirect:/dossiers-medicaux?page=" + page + "&keyword=" + keyword + "&updated=true";
            } else {
                model.addAttribute("error", "Erreur lors de la mise à jour du dossier médical");
                return "editDossierMedicalForm";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "editDossierMedicalForm";
        }
    }
}
