package ma.enset.hospitalapp.controllers;

import jakarta.servlet.http.HttpServletResponse;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.ConsultationService;
import ma.enset.hospitalapp.services.SuiviConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/suivis")
public class SuiviConsultationController {

    @Autowired
    private SuiviConsultationService suiviConsultationService;

    @Autowired
    private ConsultationService consultationService;

    // Liste des suivis avec pagination et filtres
    @GetMapping("")
    public String listeSuivis(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size,
                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                             @RequestParam(name = "statut", required = false) StatutSuivi statut,
                             @RequestParam(name = "type", required = false) TypeSuivi type) {
        try {
            Page<SuiviConsultation> suivis;
            if (!keyword.isEmpty() || statut != null || type != null) {
                suivis = suiviConsultationService.rechercherSuivis(keyword, statut, type, PageRequest.of(page, size));
            } else {
                suivis = suiviConsultationService.getAllSuivis(PageRequest.of(page, size));
            }
            model.addAttribute("listSuivis", suivis.getContent());
            model.addAttribute("pages", new int[suivis.getTotalPages()]);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            model.addAttribute("selectedStatut", statut);
            model.addAttribute("selectedType", type);
            model.addAttribute("statutsSuivi", StatutSuivi.values());
            model.addAttribute("typesSuivi", TypeSuivi.values());
            model.addAttribute("totalPages", suivis.getTotalPages());
            // Statistiques pour le dashboard
            SuiviDashboardStats stats = suiviConsultationService.getSuiviDashboardStats();
            model.addAttribute("stats", stats);
            return "suivis-consultation";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur interne: " + e.getMessage());
            return "suivis-consultation";
        }
    }

    // Formulaire d'ajout de suivi
    @GetMapping("/add")
    public String addSuiviForm(Model model, @RequestParam(name = "consultationId", required = false) Long consultationId) {
        model.addAttribute("suivi", new SuiviConsultation());
        
        List<Consultation> consultations = consultationService.getConsultationsTerminees();
        model.addAttribute("consultations", consultations);
        
        if (consultationId != null) {
            Consultation consultation = consultationService.getConsultationById(consultationId);
            model.addAttribute("selectedConsultation", consultation);
        }
        
        model.addAttribute("typesSuivi", TypeSuivi.values());
        model.addAttribute("statutsSuivi", StatutSuivi.values());
        
        return "addSuiviConsultationForm";
    }

    // Sauvegarde d'un nouveau suivi
    @PostMapping("/save")
    public String saveSuivi(@ModelAttribute SuiviConsultation suivi,
                           @RequestParam("consultationId") Long consultationId,
                           RedirectAttributes redirectAttributes) {
        try {
            Consultation consultation = consultationService.getConsultationById(consultationId);
            if (consultation == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Consultation introuvable. Veuillez sélectionner une consultation valide.");
                return "redirect:/suivis/add";
            }
            suivi.setConsultation(consultation);
            suivi.setDateCreation(LocalDateTime.now());

            // Validation manuelle
            if (suivi.getObservations() == null || suivi.getObservations().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Les observations sont obligatoires");
                return "redirect:/suivis/add?consultationId=" + consultationId;
            }

            if (suivi.getTypeSuivi() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le type de suivi est obligatoire");
                return "redirect:/suivis/add?consultationId=" + consultationId;
            }

            suiviConsultationService.saveSuivi(suivi);
            redirectAttributes.addFlashAttribute("successMessage", "Suivi créé avec succès");
            return "redirect:/suivis";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du suivi: " + e.getMessage());
            return "redirect:/suivis/add?consultationId=" + consultationId;
        }
    }

    // Formulaire d'édition
    @GetMapping("/edit/{id}")
    public String editSuiviForm(@PathVariable Long id, Model model) {
        try {
            SuiviConsultation suivi = suiviConsultationService.getSuiviById(id);
            model.addAttribute("suivi", suivi);
            model.addAttribute("typesSuivi", TypeSuivi.values());
            model.addAttribute("statutsSuivi", StatutSuivi.values());
            
            return "editSuiviConsultationForm";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Suivi non trouvé");
            return "redirect:/suivis";
        }
    }

    // Mise à jour d'un suivi
    @PostMapping("/update")
    public String updateSuivi(@ModelAttribute SuiviConsultation suivi,
                             RedirectAttributes redirectAttributes) {
        try {
            // Récupérer le suivi existant pour conserver les données non modifiables
            SuiviConsultation existingSuivi = suiviConsultationService.getSuiviById(suivi.getId());
            
            // Conserver les données d'origine
            suivi.setConsultation(existingSuivi.getConsultation());
            suivi.setDateCreation(existingSuivi.getDateCreation());
            suivi.setDateModification(LocalDateTime.now());
            
            // Validation manuelle
            if (suivi.getObservations() == null || suivi.getObservations().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Les observations sont obligatoires");
                return "redirect:/suivis/edit/" + suivi.getId();
            }
            
            suiviConsultationService.saveSuivi(suivi);
            redirectAttributes.addFlashAttribute("successMessage", "Suivi mis à jour avec succès");
            return "redirect:/suivis";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            return "redirect:/suivis/edit/" + suivi.getId();
        }
    }

    // Détails d'un suivi
    @GetMapping("/details/{id}")
    public String detailsSuivi(@PathVariable Long id, Model model) {
        try {
            SuiviConsultation suivi = suiviConsultationService.getSuiviById(id);
            model.addAttribute("suivi", suivi);
            
            return "detailsSuiviConsultation";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Suivi non trouvé");
            return "redirect:/suivis";
        }
    }

    // Suppression d'un suivi
    @GetMapping("/delete/{id}")
    public String deleteSuivi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            suiviConsultationService.deleteSuivi(id);
            redirectAttributes.addFlashAttribute("successMessage", "Suivi supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/suivis";
    }

    // Suivis par consultation
    @GetMapping("/consultation/{consultationId}")
    public String suivisParConsultation(@PathVariable Long consultationId, Model model) {
        try {
            Consultation consultation = consultationService.getConsultationById(consultationId);
            List<SuiviConsultation> suivis = suiviConsultationService.getSuivisByConsultation(consultationId);
            
            model.addAttribute("consultation", consultation);
            model.addAttribute("suivis", suivis);
            
            return "suivis-par-consultation";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Consultation non trouvée");
            return "redirect:/consultations";
        }
    }

    // Marquer un suivi comme terminé
    @PostMapping("/terminer/{id}")
    public String terminerSuivi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            SuiviConsultation suivi = suiviConsultationService.getSuiviById(id);
            suivi.setStatutSuivi(StatutSuivi.TERMINE);
            suivi.setDateModification(LocalDateTime.now());
            suiviConsultationService.saveSuivi(suivi);
            
            redirectAttributes.addFlashAttribute("successMessage", "Suivi marqué comme terminé");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur: " + e.getMessage());
        }
        return "redirect:/suivis";
    }

    // Dashboard des suivis
    @GetMapping("/dashboard")
    public String dashboardSuivis(Model model) {
        SuiviDashboardStats stats = suiviConsultationService.getSuiviDashboardStats();
        model.addAttribute("stats", stats);
        
        // Derniers suivis
        List<SuiviConsultation> derniersSuivis = suiviConsultationService.getDerniersSuivis(5);
        model.addAttribute("derniersSuivis", derniersSuivis);
        
        // Suivis urgents
        List<SuiviConsultation> suivisUrgents = suiviConsultationService.getSuivisUrgents();
        model.addAttribute("suivisUrgents", suivisUrgents);
        
        return "suivi-dashboard";
    }

    // Exporter les suivis au format CSV
    @GetMapping("/export")
    @ResponseBody
    public void exportSuivisCsv(HttpServletResponse response) throws java.io.IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=suivis.csv");
        java.io.PrintWriter writer = response.getWriter();
        // En-tête CSV
        writer.println("ID;Date Suivi;Personnel;Type;Statut;Observations;Actions;Consignes;Prochain RDV");
        List<SuiviConsultation> suivis = suiviConsultationService.getAllSuivis();
        for (SuiviConsultation s : suivis) {
            writer.printf("%d;%s;%s;%s;%s;%s;%s;%s;%s\n",
                s.getId(),
                s.getDateSuivi() != null ? s.getDateSuivi().toString() : "",
                s.getPersonnel() != null ? s.getPersonnel().replace(";", ",") : "",
                s.getTypeSuivi() != null ? s.getTypeSuivi().name() : "",
                s.getStatutSuivi() != null ? s.getStatutSuivi().name() : "",
                s.getObservations() != null ? s.getObservations().replace(";", ",") : "",
                s.getActions() != null ? s.getActions().replace(";", ",") : "",
                s.getConsignes() != null ? s.getConsignes().replace(";", ",") : "",
                s.getProchainRendezVous() != null ? s.getProchainRendezVous().toString() : ""
            );
        }
        writer.flush();
        writer.close();
    }
}
