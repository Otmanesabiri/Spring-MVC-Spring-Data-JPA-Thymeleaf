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

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/rendez-vous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedecinService medecinService;

    @GetMapping("")
    public String listRendezVous(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size,
                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                @RequestParam(name = "statut", required = false) String statut) {
        
        Page<RendezVous> rendezVousPage;
        
        if (statut != null && !statut.isEmpty()) {
            StatutRendezVous statutEnum = StatutRendezVous.valueOf(statut);
            rendezVousPage = rendezVousService.findRendezVousByStatut(statutEnum, PageRequest.of(page, size));
        } else if (keyword != null && !keyword.isEmpty()) {
            rendezVousPage = rendezVousService.findRendezVousByKeyword(keyword, PageRequest.of(page, size));
        } else {
            rendezVousPage = rendezVousService.findAllRendezVous(PageRequest.of(page, size));
        }
        
        model.addAttribute("listRendezVous", rendezVousPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rendezVousPage.getTotalPages());
        model.addAttribute("totalItems", rendezVousPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("statut", statut);
        model.addAttribute("statuts", StatutRendezVous.values());
        
        return "rendez-vous-simple";
    }

    @GetMapping("/delete")
    public String deleteRendezVous(Long id, String keyword, int page) {
        rendezVousService.deleteRendezVous(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formRendezVous(Model model) {
        model.addAttribute("rendezVous", new RendezVous());
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutRendezVous.values());
        model.addAttribute("types", TypeRendezVous.values());
        return "addRendezVousForm";
    }

    @PostMapping("/save")
    public String save(Model model, RendezVous rendezVous, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long patientId,
                       @RequestParam(required = false) Long medecinId) {
        
        try {
            // Ajouter les listes pour le formulaire en cas d'erreur
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutRendezVous.values());
            model.addAttribute("types", TypeRendezVous.values());
            
            // Validation des paramètres requis
            if (patientId == null || medecinId == null) {
                model.addAttribute("error", "Veuillez sélectionner un patient et un médecin");
                return "addRendezVousForm";
            }
            
            // Récupérer le patient et le médecin par leurs IDs
            Patient patient = patientService.getPatient(patientId);
            Medecin medecin = medecinService.getMedecin(medecinId);
            
            if (patient == null || medecin == null) {
                model.addAttribute("error", "Patient ou médecin introuvable");
                return "addRendezVousForm";
            }
            
            // Assigner le patient et le médecin au rendez-vous AVANT la validation
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            
            // Vérifier la date du rendez-vous
            if (rendezVous.getDateHeureRendezVous() == null) {
                model.addAttribute("error", "La date et l'heure du rendez-vous sont obligatoires");
                return "addRendezVousForm";
            }
            
            // Définir un statut par défaut si nécessaire
            if (rendezVous.getStatut() == null) {
                rendezVous.setStatut(StatutRendezVous.PLANIFIE);
            }
            
            // Définir un type par défaut si nécessaire
            if (rendezVous.getType() == null) {
                rendezVous.setType(TypeRendezVous.CONSULTATION);
            }
            
            // Définir une durée par défaut si nécessaire
            if (rendezVous.getDureeMinutes() <= 0) {
                rendezVous.setDureeMinutes(30);
            }
            
            // Vérifier les erreurs de validation après avoir assigné patient et médecin
            if (bindingResult.hasErrors()) {
                StringBuilder errorMsg = new StringBuilder("Erreurs de validation: ");
                bindingResult.getAllErrors().forEach(error -> 
                    errorMsg.append(error.getDefaultMessage()).append("; "));
                model.addAttribute("error", errorMsg.toString());
                return "addRendezVousForm";
            }
            
            // Sauvegarder le rendez-vous
            RendezVous savedRendezVous = rendezVousService.saveRendezVous(rendezVous);
            
            if (savedRendezVous != null) {
                return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
            } else {
                model.addAttribute("error", "Erreur lors de la sauvegarde du rendez-vous");
                return "addRendezVousForm";
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // Pour déboguer dans la console
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
            model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
            model.addAttribute("statuts", StatutRendezVous.values());
            model.addAttribute("types", TypeRendezVous.values());
            return "addRendezVousForm";
        }
    }

    @GetMapping("/edit")
    public String editRendezVous(Model model, Long id, 
                                @RequestParam(defaultValue = "") String keyword, 
                                @RequestParam(defaultValue = "0") int page) {
        RendezVous rendezVous = rendezVousService.getRendezVous(id);
        if (rendezVous == null) throw new RuntimeException("Rendez-vous introuvable");
        
        model.addAttribute("rendezVous", rendezVous);
        model.addAttribute("patients", patientService.findAllPatients(PageRequest.of(0, 100)).getContent());
        model.addAttribute("medecins", medecinService.findAllMedecins(PageRequest.of(0, 100)).getContent());
        model.addAttribute("statuts", StatutRendezVous.values());
        model.addAttribute("types", TypeRendezVous.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editRendezVousForm";
    }

    // Actions de gestion des statuts
    @GetMapping("/confirmer")
    public String confirmerRendezVous(Long id, String keyword, int page) {
        rendezVousService.confirmerRendezVous(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/commencer")
    public String commencerRendezVous(Long id, String keyword, int page) {
        rendezVousService.commencerRendezVous(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/terminer")
    public String terminerRendezVous(Long id, String keyword, int page) {
        rendezVousService.terminerRendezVous(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/annuler")
    public String annulerRendezVous(Long id, String keyword, int page, 
                                   @RequestParam(defaultValue = "Annulation par l'utilisateur") String raison) {
        rendezVousService.annulerRendezVous(id, raison);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/non-presente")
    public String marquerNonPresente(Long id, String keyword, int page) {
        rendezVousService.marquerNonPresente(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/envoyer-rappel")
    public String envoyerRappel(Long id, String keyword, int page) {
        rendezVousService.envoyerRappel(id);
        return "redirect:/rendez-vous?page=" + page + "&keyword=" + keyword;
    }

    // Calendrier des rendez-vous
    @GetMapping("/calendrier")
    public String calendrierRendezVous(Model model, @RequestParam(required = false) String date) {
        Date dateSelectionnee;
        
        if (date != null && !date.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateSelectionnee = sdf.parse(date);
            } catch (Exception e) {
                dateSelectionnee = new Date();
            }
        } else {
            dateSelectionnee = new Date();
        }
        
        model.addAttribute("rendezVousJour", rendezVousService.getRendezVousByDate(dateSelectionnee));
        model.addAttribute("dateSelectionnee", dateSelectionnee);
        
        return "calendrier-rendez-vous";
    }

    // Disponibilités d'un médecin
    @GetMapping("/disponibilites")
    public String disponibilitesMedecin(Model model, 
                                       @RequestParam Long medecinId, 
                                       @RequestParam(required = false) String date) {
        Date dateSelectionnee;
        
        if (date != null && !date.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateSelectionnee = sdf.parse(date);
            } catch (Exception e) {
                dateSelectionnee = new Date();
            }
        } else {
            dateSelectionnee = new Date();
        }
        
        Medecin medecin = medecinService.getMedecin(medecinId);
        if (medecin == null) {
            throw new RuntimeException("Médecin introuvable");
        }
        
        model.addAttribute("medecin", medecin);
        model.addAttribute("creneauxDisponibles", rendezVousService.getCreneauxDisponibles(medecin, dateSelectionnee, 30));
        model.addAttribute("dateSelectionnee", dateSelectionnee);
        
        return "disponibilites-medecin";
    }
}
