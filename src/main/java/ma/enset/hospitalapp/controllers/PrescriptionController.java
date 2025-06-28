package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.PrescriptionService;
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
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/dossier/{dossierId}/prescriptions")
    public String prescriptions(@PathVariable Long dossierId,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword,
                               Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Page<Prescription> pagePrescriptions = prescriptionService.getPrescriptionsByDossier(dossierId, PageRequest.of(page, size));
            
            model.addAttribute("listPrescriptions", pagePrescriptions.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pagePrescriptions.getTotalPages());
            model.addAttribute("totalElements", pagePrescriptions.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("keyword", keyword);
            model.addAttribute("dossier", dossier);
            
            return "prescriptions";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/dossier/{dossierId}/prescription/new")
    public String showAddPrescriptionForm(@PathVariable Long dossierId, Model model) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            Prescription prescription = new Prescription();
            prescription.setDossierMedical(dossier);
            
            model.addAttribute("prescription", prescription);
            model.addAttribute("dossier", dossier);
            model.addAttribute("statutsPrescription", StatutPrescription.values());
            
            return "addPrescriptionForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/dossier/{dossierId}/prescription/save")
    public String savePrescription(@PathVariable Long dossierId,
                                  @Valid @ModelAttribute("prescription") Prescription prescription,
                                  BindingResult bindingResult,
                                  Model model) {
        try {
            if (bindingResult.hasErrors()) {
                DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
                model.addAttribute("dossier", dossier);
                model.addAttribute("statutsPrescription", StatutPrescription.values());
                return "addPrescriptionForm";
            }
            
            DossierMedical dossier = dossierMedicalService.getDossierMedical(dossierId);
            prescription.setDossierMedical(dossier);
            
            prescriptionService.savePrescription(prescription);
            return "redirect:/dossier/" + dossierId + "/prescriptions?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "addPrescriptionForm";
        }
    }

    @GetMapping("/prescription/edit/{id}")
    public String showEditPrescriptionForm(@PathVariable Long id, Model model) {
        try {
            Prescription prescription = prescriptionService.getPrescriptionById(id);
            
            model.addAttribute("prescription", prescription);
            model.addAttribute("dossier", prescription.getDossierMedical());
            model.addAttribute("statutsPrescription", StatutPrescription.values());
            
            return "editPrescriptionForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/prescription/update/{id}")
    public String updatePrescription(@PathVariable Long id,
                                    @Valid @ModelAttribute("prescription") Prescription prescription,
                                    BindingResult bindingResult,
                                    Model model) {
        try {
            if (bindingResult.hasErrors()) {
                Prescription existingPrescription = prescriptionService.getPrescriptionById(id);
                model.addAttribute("dossier", existingPrescription.getDossierMedical());
                model.addAttribute("statutsPrescription", StatutPrescription.values());
                return "editPrescriptionForm";
            }
            
            Prescription existingPrescription = prescriptionService.getPrescriptionById(id);
            prescription.setId(id);
            prescription.setDossierMedical(existingPrescription.getDossierMedical());
            
            prescriptionService.savePrescription(prescription);
            return "redirect:/dossier/" + existingPrescription.getDossierMedical().getId() + "/prescriptions?updated";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
            return "editPrescriptionForm";
        }
    }

    @GetMapping("/prescription/delete/{id}")
    public String deletePrescription(@PathVariable Long id, Model model) {
        try {
            Prescription prescription = prescriptionService.getPrescriptionById(id);
            Long dossierId = prescription.getDossierMedical().getId();
            
            prescriptionService.deletePrescription(id);
            return "redirect:/dossier/" + dossierId + "/prescriptions?deleted";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
            return "error";
        }
    }
}
