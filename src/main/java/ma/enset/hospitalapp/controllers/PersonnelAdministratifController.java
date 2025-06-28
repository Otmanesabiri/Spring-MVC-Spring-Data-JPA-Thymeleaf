package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.PersonnelAdministratif;
import ma.enset.hospitalapp.entities.HoraireTravail;
import ma.enset.hospitalapp.entities.StatutEmploye;
import ma.enset.hospitalapp.entities.NiveauAcces;
import ma.enset.hospitalapp.services.PersonnelAdministratifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personnel-administratif")
public class PersonnelAdministratifController {

    @Autowired
    private PersonnelAdministratifService personnelAdministratifService;

    @GetMapping("")
    public String listPersonnelAdministratif(Model model,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size,
                                            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        Page<PersonnelAdministratif> personnel;
        if (keyword.isEmpty()) {
            personnel = personnelAdministratifService.findAllPersonnelAdministratif(PageRequest.of(page, size));
        } else {
            personnel = personnelAdministratifService.findPersonnelAdministratifByKeyword(keyword, PageRequest.of(page, size));
        }
        
        model.addAttribute("listPersonnelAdministratif", personnel.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personnel.getTotalPages());
        model.addAttribute("totalItems", personnel.getTotalElements());
        model.addAttribute("keyword", keyword);
        
        return "personnel-administratif";
    }

    @GetMapping("/delete")
    public String deletePersonnelAdministratif(Long id, String keyword, int page) {
        personnelAdministratifService.deletePersonnelAdministratif(id);
        return "redirect:/personnel-administratif?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/form")
    public String formPersonnelAdministratif(Model model) {
        model.addAttribute("personnel", new PersonnelAdministratif());
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        model.addAttribute("niveauxAcces", NiveauAcces.values());
        return "addPersonnelAdministratifForm";
    }

    @PostMapping("/save")
    public String save(Model model, @Valid PersonnelAdministratif personnel, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("horaires", HoraireTravail.values());
            model.addAttribute("statuts", StatutEmploye.values());
            model.addAttribute("niveauxAcces", NiveauAcces.values());
            return "addPersonnelAdministratifForm";
        }
        personnelAdministratifService.savePersonnelAdministratif(personnel);
        return "redirect:/personnel-administratif?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/edit")
    public String editPersonnelAdministratif(Model model, Long id, String keyword, int page) {
        PersonnelAdministratif personnel = personnelAdministratifService.findPersonnelAdministratifById(id);
        if (personnel == null) throw new RuntimeException("Personnel administratif introuvable");
        
        model.addAttribute("personnel", personnel);
        model.addAttribute("horaires", HoraireTravail.values());
        model.addAttribute("statuts", StatutEmploye.values());
        model.addAttribute("niveauxAcces", NiveauAcces.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editPersonnelAdministratifForm";
    }

    @GetMapping("/activer")
    public String activerPersonnelAdministratif(Long id, String keyword, int page) {
        personnelAdministratifService.activerPersonnelAdministratif(id);
        return "redirect:/personnel-administratif?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/desactiver")
    public String desactiverPersonnelAdministratif(Long id, String keyword, int page) {
        personnelAdministratifService.desactiverPersonnelAdministratif(id);
        return "redirect:/personnel-administratif?page=" + page + "&keyword=" + keyword;
    }
}
