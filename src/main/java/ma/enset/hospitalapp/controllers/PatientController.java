package ma.enset.hospitalapp.controllers;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String patients(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Patient> pagePatients = keyword.isEmpty() ?
                patientService.findAllPatients(PageRequest.of(page, size)) :
                patientService.findPatientsByName(keyword, PageRequest.of(page, size));

        model.addAttribute("patients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);

        // Create page numbers for pagination
        if (pagePatients.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, pagePatients.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", pagePatients.getTotalPages());

        return "patients";
    }

    @GetMapping("/addPatientForm")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatientForm";
    }

    @PostMapping("/savePatient")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addPatientForm";
        }
        patientService.savePatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/editPatient")
    public String editPatient(Model model, @RequestParam(name = "id") Long id) {
        Patient patient = patientService.getPatient(id);
        if(patient == null) throw new RuntimeException("Patient not found");
        model.addAttribute("patient", patient);
        return "editPatientForm";
    }

    @PostMapping("/updatePatient")
    public String updatePatient(@Valid Patient patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editPatientForm";
        }
        patientService.savePatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/deletePatient")
    public String deletePatient(@RequestParam(name = "id") Long id,
                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                @RequestParam(name = "page", defaultValue = "0") int page) {
        patientService.deletePatient(id);
        return "redirect:/patients?page=" + page + "&keyword=" + keyword;
    }
}