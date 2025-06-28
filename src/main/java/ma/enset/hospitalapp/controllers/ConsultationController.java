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

@Controller
@RequestMapping("/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedecinService medecinService;

    @GetMapping("")
    public String listConsultations(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "5") int size,
                                   @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        Page<Consultation> consultations = consultationService.findAllConsultations(PageRequest.of(page, size));
        
        model.addAttribute("listConsultations", consultations.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultations.getTotalPages());
        model.addAttribute("totalItems", consultations.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "consultations";
    }

    @GetMapping("/delete")
    public String deleteConsultation(Long id, String keyword, int page) {
        consultationService.deleteConsultation(id);
        return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formConsultation(Model model) {
        model.addAttribute("consultation", new Consultation());
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutConsultation.values());
        return "addConsultationForm";
    }

    @PostMapping("/save")
    public String save(Model model, Consultation consultation, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long patientId,
                       @RequestParam(required = false) Long medecinId) {
        
        try {
            // Vérifier que les IDs sont fournis
            if (patientId == null || medecinId == null) {
                model.addAttribute("error", "Veuillez sélectionner un patient et un médecin");
                model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
                model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
                model.addAttribute("statuts", StatutConsultation.values());
                return "addConsultationForm";
            }
            
            // Récupérer le patient et le médecin par leurs IDs
            Patient patient = patientService.getPatient(patientId);
            Medecin medecin = medecinService.getMedecin(medecinId);
            
            if (patient == null || medecin == null) {
                model.addAttribute("error", "Patient ou médecin introuvable");
                model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
                model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
                model.addAttribute("statuts", StatutConsultation.values());
                return "addConsultationForm";
            }
            
            // Assigner le patient et le médecin à la consultation
            consultation.setPatient(patient);
            consultation.setMedecin(medecin);
            
            // Vérifier les erreurs de validation après avoir assigné patient et médecin
            if (consultation.getDateConsultation() == null) {
                model.addAttribute("error", "La date de consultation est obligatoire");
                model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
                model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
                model.addAttribute("statuts", StatutConsultation.values());
                return "addConsultationForm";
            }
            
            // Si pas de statut sélectionné, mettre PLANIFIEE par défaut
            if (consultation.getStatut() == null) {
                consultation.setStatut(StatutConsultation.PLANIFIEE);
            }
            
            consultationService.saveConsultation(consultation);
            return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
            
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutConsultation.values());
            return "addConsultationForm";
        }
    }

    @GetMapping("/edit")
    public String editConsultation(Model model, Long id, 
                                  @RequestParam(defaultValue = "") String keyword, 
                                  @RequestParam(defaultValue = "0") int page) {
        Consultation consultation = consultationService.getConsultation(id);
        if (consultation == null) throw new RuntimeException("Consultation introuvable");
        
        model.addAttribute("consultation", consultation);
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutConsultation.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editConsultationForm";
    }

    @GetMapping("/marquer-paye")
    public String marquerCommePaye(Long id, String keyword, int page) {
        consultationService.marquerCommePaye(id);
        return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/non-payees")
    public String consultationsNonPayees(Model model,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        
        Page<Consultation> consultations = consultationService.findUnpaidConsultations(PageRequest.of(page, size));
        
        model.addAttribute("listConsultations", consultations.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultations.getTotalPages());
        model.addAttribute("totalItems", consultations.getTotalElements());
        
        return "consultations-non-payees";
    }
}
