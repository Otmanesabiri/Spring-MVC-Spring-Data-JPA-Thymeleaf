package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.services.LitService;
import ma.enset.hospitalapp.services.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/lits")
public class LitController {
    @Autowired
    private LitService litService;
    @Autowired
    private ChambreService chambreService;

    @GetMapping("/chambre/{chambreId}")
    public String litsParChambre(@PathVariable Long chambreId, Model model) {
        List<Lit> lits = litService.findByChambre(chambreId);
        Chambre chambre = chambreService.getChambre(chambreId);
        model.addAttribute("lits", lits);
        model.addAttribute("chambre", chambre);
        return "lits";
    }

    @GetMapping("")
    public String allLits(Model model) {
        List<Lit> lits = litService.getAllLits();
        model.addAttribute("lits", lits);
        model.addAttribute("globalView", true);
        return "lits";
    }

    @GetMapping("/add/{chambreId}")
    public String addLitForm(@PathVariable Long chambreId, Model model) {
        Lit lit = new Lit();
        Chambre chambre = chambreService.getChambre(chambreId);
        lit.setChambre(chambre);
        List<Chambre> chambres = chambreService.getAllChambres();
        model.addAttribute("lit", lit);
        model.addAttribute("chambres", chambres);
        model.addAttribute("statuts", StatutLit.values());
        return "addLitForm";
    }

    @GetMapping("/add")
    public String addLitFormGlobal(Model model) {
        Lit lit = new Lit();
        List<Chambre> chambres = chambreService.getAllChambres();
        model.addAttribute("lit", lit);
        model.addAttribute("chambres", chambres);
        model.addAttribute("statuts", StatutLit.values());
        return "addLitForm";
    }

    @PostMapping("/save")
    public String saveLit(@ModelAttribute Lit lit, RedirectAttributes redirectAttributes) {
        try {
            litService.saveLit(lit);
            redirectAttributes.addFlashAttribute("successMessage", "Lit enregistré avec succès");
            return "redirect:/lits";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lits/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editLitForm(@PathVariable Long id, Model model) {
        Lit lit = litService.getLit(id);
        List<Chambre> chambres = chambreService.getAllChambres();
        model.addAttribute("lit", lit);
        model.addAttribute("chambres", chambres);
        model.addAttribute("statuts", StatutLit.values());
        return "editLitForm";
    }

    @PostMapping("/update")
    public String updateLit(@ModelAttribute Lit lit, RedirectAttributes redirectAttributes) {
        try {
            litService.saveLit(lit);
            redirectAttributes.addFlashAttribute("successMessage", "Lit modifié avec succès");
            return "redirect:/lits";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/lits/edit/" + lit.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteLit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        litService.deleteLit(id);
        redirectAttributes.addFlashAttribute("successMessage", "Lit supprimé avec succès");
        return "redirect:/lits";
    }
}
