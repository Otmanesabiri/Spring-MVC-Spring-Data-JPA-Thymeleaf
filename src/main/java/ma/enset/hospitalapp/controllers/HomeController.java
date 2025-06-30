package ma.enset.hospitalapp.controllers;

import ma.enset.hospitalapp.repositories.ConsultationRepository;
import ma.enset.hospitalapp.repositories.InfirmierRepository;
import ma.enset.hospitalapp.repositories.MedecinRepository;
import ma.enset.hospitalapp.repositories.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final MedecinRepository medecinRepository;
    private final InfirmierRepository infirmierRepository;

    public HomeController(PatientRepository patientRepository,
                         ConsultationRepository consultationRepository,
                         MedecinRepository medecinRepository,
                         InfirmierRepository infirmierRepository) {
        this.patientRepository = patientRepository;
        this.consultationRepository = consultationRepository;
        this.medecinRepository = medecinRepository;
        this.infirmierRepository = infirmierRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("statPatients", patientRepository.count());
        model.addAttribute("statConsultations", consultationRepository.count());
        model.addAttribute("statMedecins", medecinRepository.count());
        model.addAttribute("statInfirmiers", infirmierRepository.count());
        return "dashboard";
    }
}

