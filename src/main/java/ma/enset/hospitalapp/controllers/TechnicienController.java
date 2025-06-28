package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Technicien;
import ma.enset.hospitalapp.entities.TypeTechnicien;
import ma.enset.hospitalapp.entities.HoraireTravail;
import ma.enset.hospitalapp.entities.StatutEmploye;
import ma.enset.hospitalapp.services.TechnicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/techniciens")
public class TechnicienController {

    @Autowired
    private TechnicienService technicienService;

    @GetMapping("")
    public String listTechniciens(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        Page<Technicien> techniciens;
        if (keyword.isEmpty()) {
            techniciens = technicienService.findAllTechniciens(PageRequest.of(page, size));
        } else {
            techniciens = technicienService.findTechniciensByKeyword(keyword, PageRequest.of(page, size));
        }
        
        model.addAttribute("listTechniciens", techniciens.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", techniciens.getTotalPages());
        model.addAttribute("totalItems", techniciens.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "techniciens";
    }

    @GetMapping("/delete")
    public String deleteTechnicien(Long id, String keyword, int page) {
        technicienService.deleteTechnicien(id);
        return "redirect:/techniciens?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formTechnicien(Model model) {
        model.addAttribute("technicien", new Technicien());
        model.addAttribute("typesTechnicien", TypeTechnicien.values());
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        return "addTechnicienForm";
    }

    @PostMapping("/save")
    public String save(Model model, @Valid Technicien technicien, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("typesTechnicien", TypeTechnicien.values());
            model.addAttribute("horaires", HoraireTravail.values());
            model.addAttribute("statuts", StatutEmploye.values());
            return "addTechnicienForm";
        }
        technicienService.saveTechnicien(technicien);
        return "redirect:/techniciens?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/edit")
    public String editTechnicien(Model model, Long id, String keyword, int page) {
        Technicien technicien = technicienService.findTechnicienById(id);
        if (technicien == null) throw new RuntimeException("Technicien introuvable");
        
        model.addAttribute("technicien", technicien);
        model.addAttribute("typesTechnicien", TypeTechnicien.values());
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editTechnicienForm";
    }

    @GetMapping("/activer")
    public String activerTechnicien(Long id, String keyword, int page) {
        technicienService.activerTechnicien(id);
        return "redirect:/techniciens?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/desactiver")
    public String desactiverTechnicien(Long id, String keyword, int page) {
        technicienService.desactiverTechnicien(id);
        return "redirect:/techniciens?page=" + page + "&keyword=" + keyword;
    }
}
