package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.StatutMedecin;
import ma.enset.hospitalapp.services.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medecins")
public class MedecinController {

    @Autowired
    private MedecinService medecinService;

    @GetMapping("")
    public String listMedecins(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "5") int size,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        Page<Medecin> medecins;
        if (keyword.isEmpty()) {
            medecins = medecinService.findAllMedecins(PageRequest.of(page, size));
        } else {
            medecins = medecinService.findMedecinsByKeyword(keyword, PageRequest.of(page, size));
        }
        
        model.addAttribute("listMedecins", medecins.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", medecins.getTotalPages());
        model.addAttribute("totalItems", medecins.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "medecins";
    }

    @GetMapping("/delete")
    public String deleteMedecin(Long id, String keyword, int page) {
        medecinService.deleteMedecin(id);
        return "redirect:/medecins?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formMedecin(Model model) {
        model.addAttribute("medecin", new Medecin());
        model.addAttribute("statutOptions", StatutMedecin.values());
        return "addMedecinForm";
    }

    @PostMapping("/save")
    public String saveMedecin(@Valid Medecin medecin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statutOptions", StatutMedecin.values());
            return "addMedecinForm";
        }
        medecinService.saveMedecin(medecin);
        return "redirect:/medecins";
    }

    @GetMapping("/edit")
    public String editMedecin(Model model, Long id, String keyword, int page) {
        Medecin medecin = medecinService.getMedecin(id);
        if (medecin == null) throw new RuntimeException("Médecin introuvable");
        model.addAttribute("medecin", medecin);
        model.addAttribute("statutOptions", StatutMedecin.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editMedecinForm";
    }

    @GetMapping("/activer")
    public String activerMedecin(Long id, String keyword, int page) {
        medecinService.activerMedecin(id);
        return "redirect:/medecins?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/desactiver")
    public String desactiverMedecin(Long id, String keyword, int page) {
        medecinService.desactiverMedecin(id);
        return "redirect:/medecins?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/specialite/{specialite}")
    public String medecinsBySpecialite(@PathVariable String specialite, Model model,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        
        Page<Medecin> medecins = medecinService.findMedecinsBySpecialite(specialite, PageRequest.of(page, size));
        
        model.addAttribute("listMedecins", medecins.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", medecins.getTotalPages());
        model.addAttribute("totalItems", medecins.getTotalElements());
        model.addAttribute("specialite", specialite);
        
        return "medecinsBySpecialite";
    }

    @GetMapping("/statistiques")
    public String statistiques(Model model) {
        model.addAttribute("totalMedecins", medecinService.countActiveMedecins());
        model.addAttribute("statistiquesSpecialites", medecinService.getStatistiquesBySpecialite());
        return "statistiquesMedecins";
    }
}
