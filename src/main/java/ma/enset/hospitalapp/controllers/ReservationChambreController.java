package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.ReservationChambreService;
import ma.enset.hospitalapp.services.ChambreService;
import ma.enset.hospitalapp.services.LitService;
import ma.enset.hospitalapp.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationChambreController {
    @Autowired
    private ReservationChambreService reservationService;
    @Autowired
    private ChambreService chambreService;
    @Autowired
    private LitService litService;
    @Autowired
    private PatientService patientService;

    @GetMapping("")
    public String allReservations(Model model) {
        List<ReservationChambre> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    @GetMapping("/add")
    public String addReservationForm(Model model) {
        ReservationChambre reservation = new ReservationChambre();
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        
        List<Chambre> chambres = chambreService.getAllChambres();
        List<Lit> lits = litService.getAllLits();
        List<Patient> patients = patientService.findAllPatients(PageRequest.of(0, 1000)).getContent();
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("chambres", chambres);
        model.addAttribute("lits", lits);
        model.addAttribute("patients", patients);
        model.addAttribute("statuts", StatutReservation.values());
        return "addReservationForm";
    }

    @PostMapping("/save")
    public String saveReservation(@ModelAttribute ReservationChambre reservation, RedirectAttributes redirectAttributes) {
        try {
            // Récupérer les entités depuis la base de données
            if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
                Chambre chambre = chambreService.getChambre(reservation.getChambre().getId());
                reservation.setChambre(chambre);
            }
            
            if (reservation.getLit() != null && reservation.getLit().getId() != null && reservation.getLit().getId() > 0) {
                Lit lit = litService.getLit(reservation.getLit().getId());
                reservation.setLit(lit);
            } else {
                reservation.setLit(null); // Lit optionnel
            }
            
            if (reservation.getPatient() != null && reservation.getPatient().getId() != null) {
                Patient patient = patientService.getPatient(reservation.getPatient().getId());
                reservation.setPatient(patient);
            }
            
            reservationService.saveReservation(reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation enregistrée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        ReservationChambre reservation = reservationService.getReservation(id);
        
        List<Chambre> chambres = chambreService.getAllChambres();
        List<Lit> lits = litService.getAllLits();
        List<Patient> patients = patientService.findAllPatients(PageRequest.of(0, 1000)).getContent();
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("chambres", chambres);
        model.addAttribute("lits", lits);
        model.addAttribute("patients", patients);
        model.addAttribute("statuts", StatutReservation.values());
        return "editReservationForm";
    }

    @PostMapping("/update")
    public String updateReservation(@ModelAttribute ReservationChambre reservation, RedirectAttributes redirectAttributes) {
        try {
            // Récupérer les entités depuis la base de données
            if (reservation.getChambre() != null && reservation.getChambre().getId() != null) {
                Chambre chambre = chambreService.getChambre(reservation.getChambre().getId());
                reservation.setChambre(chambre);
            }
            
            if (reservation.getLit() != null && reservation.getLit().getId() != null && reservation.getLit().getId() > 0) {
                Lit lit = litService.getLit(reservation.getLit().getId());
                reservation.setLit(lit);
            } else {
                reservation.setLit(null); // Lit optionnel
            }
            
            if (reservation.getPatient() != null && reservation.getPatient().getId() != null) {
                Patient patient = patientService.getPatient(reservation.getPatient().getId());
                reservation.setPatient(patient);
            }
            
            reservationService.saveReservation(reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation modifiée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.deleteReservation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/reservations";
    }
}
