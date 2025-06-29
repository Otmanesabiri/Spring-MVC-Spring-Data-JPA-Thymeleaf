package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/consultations")
public class ConsultationController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    private final ConsultationService consultationService;
    private final PatientService patientService;
    private final MedecinService medecinService;

    public ConsultationController(ConsultationService consultationService,
                                 PatientService patientService,
                                 MedecinService medecinService) {
        this.consultationService = consultationService;
        this.patientService = patientService;
        this.medecinService = medecinService;
    }

    @GetMapping("")
    public String listConsultations(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "5") int size,
                                   @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                   @RequestParam(name = "type", required = false) String type,
                                   @RequestParam(name = "statut", required = false) String statut,
                                   @RequestParam(name = "urgence", required = false) String urgence,
                                   @RequestParam(name = "medecin", required = false) String medecin) {

        // Si tous les filtres sont vides et qu'on n'est pas sur la page de base, rediriger
        boolean keywordEmpty = keyword == null || keyword.trim().isEmpty();
        boolean typeEmpty = type == null || type.trim().isEmpty();
        boolean statutEmpty = statut == null || statut.trim().isEmpty();
        boolean urgenceEmpty = urgence == null || urgence.trim().isEmpty();
        boolean medecinEmpty = medecin == null || medecin.trim().isEmpty();

        if (keywordEmpty && typeEmpty && statutEmpty && urgenceEmpty && medecinEmpty && page == 0) {
            // Tous les filtres sont vides, afficher la page consultations de base
            Page<Consultation> consultationsPage = consultationService.findAllConsultations(PageRequest.of(0, size));

            model.addAttribute("listConsultations", consultationsPage.getContent());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", consultationsPage.getTotalPages());
            model.addAttribute("totalItems", consultationsPage.getTotalElements());
            model.addAttribute("keyword", "");
            model.addAttribute("type", "");
            model.addAttribute("statut", "");
            model.addAttribute("urgence", "");
            model.addAttribute("medecin", "");

            // Ajout des enums pour les filtres
            model.addAttribute("types", TypeConsultation.values());
            model.addAttribute("statuts", StatutConsultation.values());
            model.addAttribute("niveauxUrgence", NiveauUrgence.values());

            return "consultations";
        }

        Page<Consultation> consultationsPage;
        
        try {
            TypeConsultation typeEnum = type != null && !type.isEmpty() ? TypeConsultation.valueOf(type) : null;
            StatutConsultation statutEnum = statut != null && !statut.isEmpty() ? StatutConsultation.valueOf(statut) : null;
            NiveauUrgence urgenceEnum = urgence != null && !urgence.isEmpty() ? NiveauUrgence.valueOf(urgence) : null;
            
            if (typeEnum != null || statutEnum != null || urgenceEnum != null ||
                (medecin != null && !medecin.isEmpty())) {
                consultationsPage = consultationService.findConsultationsFiltered(
                    null, typeEnum, statutEnum, urgenceEnum,
                    medecin != null && !medecin.isEmpty() ? medecin : keyword,
                    null, null,
                    PageRequest.of(page, size)
                );
            } else if (keyword != null && !keyword.isEmpty()) {
                consultationsPage = consultationService.findConsultationsByKeyword(keyword, PageRequest.of(page, size));
            } else {
                consultationsPage = consultationService.findAllConsultations(PageRequest.of(page, size));
            }
            
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la conversion des paramètres de recherche", e);
            consultationsPage = consultationService.findAllConsultations(PageRequest.of(page, size));
        }
        
        model.addAttribute("listConsultations", consultationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());
        model.addAttribute("totalItems", consultationsPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);
        model.addAttribute("statut", statut);
        model.addAttribute("urgence", urgence);
        model.addAttribute("medecin", medecin);

        // Ajout des enums pour les filtres
        model.addAttribute("types", TypeConsultation.values());
        model.addAttribute("statuts", StatutConsultation.values());
        model.addAttribute("niveauxUrgence", NiveauUrgence.values());
        
        return "consultations";
    }

    @GetMapping("/add")
    public String formConsultation(Model model) {
        model.addAttribute("consultation", new Consultation());
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
        model.addAttribute("types", TypeConsultation.values());
        model.addAttribute("statuts", StatutConsultation.values());
        model.addAttribute("niveauxUrgence", NiveauUrgence.values());
        return "addConsultationForm";
    }

    @PostMapping("/save")
    public String save(Model model, Consultation consultation, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long patientId,
                       @RequestParam(required = false) Long medecinId,
                       @RequestParam(required = false) String type) {

        try {
            // Ajouter les listes pour le formulaire en cas d'erreur
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
            model.addAttribute("types", TypeConsultation.values());
            model.addAttribute("statuts", StatutConsultation.values());
            model.addAttribute("niveauxUrgence", NiveauUrgence.values());
            
            // Validation du patient
            if (patientId == null) {
                model.addAttribute("error", "Veuillez sélectionner un patient");
                return "addConsultationForm";
            }
            
            // Récupérer le patient
            Patient patient = patientService.getPatient(patientId);
            if (patient == null) {
                model.addAttribute("error", "Patient introuvable");
                return "addConsultationForm";
            }
            
            consultation.setPatient(patient);
            
            // Validation et affectation du médecin
            if (medecinId == null) {
                model.addAttribute("error", "Veuillez sélectionner un médecin");
                return "addConsultationForm";
            }
            
            Medecin medecin = medecinService.getMedecin(medecinId);
            if (medecin == null) {
                model.addAttribute("error", "Médecin introuvable");
                return "addConsultationForm";
            }

            consultation.setMedecin(medecin.getNom() + " " + medecin.getPrenom());

            // Validation et conversion du type de consultation
            if (type == null || type.isEmpty()) {
                model.addAttribute("error", "Le type de consultation est obligatoire");
                return "addConsultationForm";
            }

            try {
                TypeConsultation typeEnum = TypeConsultation.valueOf(type);
                consultation.setType(typeEnum);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Type de consultation invalide");
                return "addConsultationForm";
            }

            if (consultation.getDateHeureConsultation() == null) {
                model.addAttribute("error", "La date et heure de consultation sont obligatoires");
                return "addConsultationForm";
            }
            
            // Sauvegarder la consultation
            Consultation savedConsultation = consultationService.saveConsultation(consultation);
            
            if (savedConsultation != null) {
                return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
            } else {
                model.addAttribute("error", "Erreur lors de la sauvegarde de la consultation");
                return "addConsultationForm";
            }
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement d'une consultation", e);
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addConsultationForm";
        }
    }

    @GetMapping("/edit")
    public String editConsultation(Model model, @RequestParam Long id, 
                                  @RequestParam(defaultValue = "") String keyword, 
                                  @RequestParam(defaultValue = "0") int page) {

        try {
            Consultation consultation = consultationService.getConsultation(id);
            if (consultation == null) {
                model.addAttribute("error", "Consultation introuvable");
                return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
            }

            model.addAttribute("consultation", consultation);
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
            model.addAttribute("types", TypeConsultation.values());
            model.addAttribute("statuts", StatutConsultation.values());
            model.addAttribute("niveauxUrgence", NiveauUrgence.values());
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);

            return "editConsultationForm";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement : " + e.getMessage());
            return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
        }
    }

    @PostMapping("/update")
    @Transactional
    public String updateConsultation(@RequestParam Long id,
                                    @RequestParam String medecin,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String statut,
                                    @RequestParam(required = false) String niveauUrgence,
                                    @RequestParam(required = false) Double tarif,
                                    @RequestParam(required = false) boolean payee,
                                    @RequestParam(required = false) String motifConsultation,
                                    @RequestParam(required = false) String diagnostic,
                                    @RequestParam(required = false) String traitement,
                                    @RequestParam(required = false) String observationsInfirmiere,
                                    @RequestParam String dateHeureConsultation,
                                    @RequestParam(required = false) Long patientId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "") String keyword,
                                    Model model) {
        
        try {
            // Récupérer la consultation existante
            Consultation existingConsultation = consultationService.getConsultation(id);
            if (existingConsultation == null) {
                return "redirect:/consultations?page=" + page + "&keyword=" + keyword + "&error=notfound";
            }
            
            // Mettre à jour les champs de base
            existingConsultation.setMedecin(medecin);

            // Conversion et validation du type de consultation
            if (type != null && !type.isEmpty()) {
                try {
                    TypeConsultation typeEnum = TypeConsultation.valueOf(type);
                    existingConsultation.setType(typeEnum);
                } catch (IllegalArgumentException e) {
                    logger.error("Type de consultation invalide: {}", type);
                }
            }

            // Conversion et validation du statut
            if (statut != null && !statut.isEmpty()) {
                try {
                    StatutConsultation statutEnum = StatutConsultation.valueOf(statut);
                    existingConsultation.setStatut(statutEnum);
                } catch (IllegalArgumentException e) {
                    logger.error("Statut de consultation invalide: {}", statut);
                }
            }

            // Conversion et validation du niveau d'urgence
            if (niveauUrgence != null && !niveauUrgence.isEmpty()) {
                try {
                    NiveauUrgence urgenceEnum = NiveauUrgence.valueOf(niveauUrgence);
                    existingConsultation.setNiveauUrgence(urgenceEnum);
                } catch (IllegalArgumentException e) {
                    logger.error("Niveau d'urgence invalide: {}", niveauUrgence);
                }
            }

            if (tarif != null) {
                existingConsultation.setTarif(tarif);
            }
            existingConsultation.setPayee(payee);
            existingConsultation.setMotifConsultation(motifConsultation);
            existingConsultation.setDiagnostic(diagnostic);
            existingConsultation.setTraitement(traitement);
            existingConsultation.setObservationsInfirmiere(observationsInfirmiere);
            
            // Parser la date
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateHeureConsultation);
                existingConsultation.setDateHeureConsultation(dateTime);
            } catch (Exception e) {
                logger.error("Erreur lors du parsing de la date: {}", e.getMessage());
            }
            
            // Gérer le patient
            if (patientId != null) {
                Patient patient = patientService.getPatient(patientId);
                if (patient != null) {
                    existingConsultation.setPatient(patient);
                }
            }
            
            // Sauvegarder
            Consultation savedConsultation = consultationService.saveConsultation(existingConsultation);
            
            if (savedConsultation != null) {
                return "redirect:/consultations?page=" + page + "&keyword=" + keyword + "&success=updated";
            } else {
                return "redirect:/consultations?page=" + page + "&keyword=" + keyword + "&error=save";
            }
            
        } catch (Exception e) {
            logger.error("Exception lors de la mise à jour d'une consultation ID={}", id, e);
            return "redirect:/consultations?page=" + page + "&keyword=" + keyword + "&error=exception";
        }
    }

    @GetMapping("/details")
    public String detailsConsultation(Model model, Long id) {
        Consultation consultation = consultationService.getConsultation(id);
        if (consultation == null) throw new RuntimeException("Consultation introuvable");
        
        model.addAttribute("consultation", consultation);
        return "detailsConsultation";
    }

    @GetMapping("/delete")
    public String deleteConsultation(Long id, String keyword, int page) {
        consultationService.deleteConsultation(id);
        return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/changer-statut")
    public String changerStatut(Long id, String nouveauStatut, String keyword, int page) {
        try {
            StatutConsultation statut = StatutConsultation.valueOf(nouveauStatut);
            consultationService.changerStatutConsultation(id, statut);
        } catch (IllegalArgumentException e) {
            // Statut invalide, ignorer
        }
        return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/urgences")
    public String urgences(Model model) {
        model.addAttribute("urgences", consultationService.getUrgencesEnAttente());
        return "urgences";
    }

    @GetMapping("/du-jour")
    public String consultationsDuJour(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Consultation> consultationsPage = consultationService.getConsultationsDuJour(PageRequest.of(page, size));
        
        model.addAttribute("listConsultations", consultationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());
        model.addAttribute("totalItems", consultationsPage.getTotalElements());
        
        return "consultations-du-jour";
    }

    @GetMapping("/hospitalisations")
    public String hospitalisations(Model model) {
        try {
            logger.info("Chargement de la page des hospitalisations...");

            // Solution temporaire : récupérer toutes les consultations d'hospitalisation sans filtrage complexe
            List<Consultation> toutes = consultationService.findAllConsultations(PageRequest.of(0, 100)).getContent();
            List<Consultation> hospitalisations = toutes.stream()
                .filter(c -> c.getType() == TypeConsultation.HOSPITALISATION)
                .filter(c -> c.getStatut() == StatutConsultation.PROGRAMMEE || c.getStatut() == StatutConsultation.EN_COURS)
                .toList();

            logger.info("Nombre d'hospitalisations trouvées : " + hospitalisations.size());

            model.addAttribute("hospitalisations", hospitalisations);
            return "hospitalisations";

        } catch (Exception e) {
            logger.error("Erreur lors du chargement des hospitalisations", e);
            model.addAttribute("hospitalisations", List.of());
            model.addAttribute("error", "Erreur lors du chargement des hospitalisations : " + e.getMessage());
            return "hospitalisations";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        ConsultationDashboardStats stats = consultationService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("consultationsRecentes", consultationService.getConsultationsRecentes(10));
        model.addAttribute("urgencesPrioritaires", consultationService.getUrgencesEnAttente());
        return "consultation-dashboard";
    }

    @GetMapping("/patient/{patientId}")
    public String consultationsPatient(@PathVariable Long patientId, Model model,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "5") int size) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient introuvable");
        }
        
        Page<Consultation> consultationsPage = consultationService.getHistoriqueConsultations(patient, PageRequest.of(page, size));
        
        model.addAttribute("patient", patient);
        model.addAttribute("listConsultations", consultationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());
        model.addAttribute("totalItems", consultationsPage.getTotalElements());
        
        return "consultations-patient";
    }

    @GetMapping("/marquer-paye")
    public String marquerCommePayee(@RequestParam Long id, 
                                    @RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(defaultValue = "") String keyword) {
        Consultation consultation = consultationService.getConsultation(id);
        if (consultation != null) {
            consultation.setPayee(true);
            consultationService.saveConsultation(consultation);
        }
        return "redirect:/consultations?page=" + page + "&keyword=" + keyword;
    }
}
