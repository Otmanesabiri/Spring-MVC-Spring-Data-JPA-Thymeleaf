package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/chambres")
public class ChambreController {
    @Autowired
    private ChambreService chambreService;

    @GetMapping("")
    public String listChambres(Model model) {
        List<Chambre> chambres = chambreService.getAllChambres();
        model.addAttribute("chambres", chambres);
        return "chambres";
    }

    @GetMapping("/add")
    public String addChambreForm(Model model) {
        model.addAttribute("chambre", new Chambre());
        model.addAttribute("types", TypeChambre.values());
        model.addAttribute("statuts", StatutChambre.values());
        return "addChambreForm";
    }

    @PostMapping("/save")
    public String saveChambre(@ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        chambreService.saveChambre(chambre);
        redirectAttributes.addFlashAttribute("successMessage", "Chambre enregistrée avec succès");
        return "redirect:/chambres";
    }

    @GetMapping("/edit/{id}")
    public String editChambreForm(@PathVariable Long id, Model model) {
        Chambre chambre = chambreService.getChambre(id);
        model.addAttribute("chambre", chambre);
        model.addAttribute("types", TypeChambre.values());
        model.addAttribute("statuts", StatutChambre.values());
        return "editChambreForm";
    }

    @PostMapping("/update")
    public String updateChambre(@ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        chambreService.saveChambre(chambre);
        redirectAttributes.addFlashAttribute("successMessage", "Chambre modifiée avec succès");
        return "redirect:/chambres";
    }

    @GetMapping("/delete/{id}")
    public String deleteChambre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        chambreService.deleteChambre(id);
        redirectAttributes.addFlashAttribute("successMessage", "Chambre supprimée avec succès");
        return "redirect:/chambres";
    }
}
