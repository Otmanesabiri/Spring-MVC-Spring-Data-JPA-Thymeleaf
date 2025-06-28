package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Infirmier;
import ma.enset.hospitalapp.entities.EquipeType;
import ma.enset.hospitalapp.entities.HoraireTravail;
import ma.enset.hospitalapp.entities.StatutEmploye;
import ma.enset.hospitalapp.services.InfirmierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/infirmiers")
public class InfirmierController {

    @Autowired
    private InfirmierService infirmierService;

    @GetMapping("")
    public String listInfirmiers(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        Page<Infirmier> infirmiers;
        if (keyword.isEmpty()) {
            infirmiers = infirmierService.findAllInfirmiers(PageRequest.of(page, size));
        } else {
            infirmiers = infirmierService.findInfirmiersByKeyword(keyword, PageRequest.of(page, size));
        }
        
        model.addAttribute("listInfirmiers", infirmiers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", infirmiers.getTotalPages());
        model.addAttribute("totalItems", infirmiers.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "infirmiers";
    }

    @GetMapping("/delete")
    public String deleteInfirmier(Long id, String keyword, int page) {
        infirmierService.deleteInfirmier(id);
        return "redirect:/infirmiers?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formInfirmier(Model model) {
        model.addAttribute("infirmier", new Infirmier());
        model.addAttribute("equipes", EquipeType.values());
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        return "addInfirmierForm";
    }

    @PostMapping("/save")
    public String save(Model model, @Valid Infirmier infirmier, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("equipes", EquipeType.values());
            model.addAttribute("horaires", HoraireTravail.values());
            model.addAttribute("statuts", StatutEmploye.values());
            return "addInfirmierForm";
        }
        infirmierService.saveInfirmier(infirmier);
        return "redirect:/infirmiers?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/edit")
    public String editInfirmier(Model model, Long id, String keyword, int page) {
        Infirmier infirmier = infirmierService.findInfirmierById(id);
        if (infirmier == null) throw new RuntimeException("Infirmier introuvable");
        
        model.addAttribute("infirmier", infirmier);
        model.addAttribute("equipes", EquipeType.values());
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editInfirmierForm";
    }

    @GetMapping("/activer")
    public String activerInfirmier(Long id, String keyword, int page) {
        infirmierService.activerInfirmier(id);
        return "redirect:/infirmiers?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/desactiver")
    public String desactiverInfirmier(Long id, String keyword, int page) {
        infirmierService.desactiverInfirmier(id);
        return "redirect:/infirmiers?page=" + page + "&keyword=" + keyword;
    }
}
