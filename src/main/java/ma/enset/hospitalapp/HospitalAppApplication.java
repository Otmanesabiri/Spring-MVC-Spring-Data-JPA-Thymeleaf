package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class HospitalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }

    // Add this bean to log the port
    @Bean
    public ApplicationListener<WebServerInitializedEvent> webServerInitializedListener() {
        return event -> {
            int port = event.getWebServer().getPort();
            System.out.println("====================================================");
            System.out.println("Application is running on port: " + port);
            System.out.println("Access the application at: http://localhost:" + port);
            System.out.println("====================================================");
        };
    }

    @Bean
    CommandLineRunner start(PatientRepository patientRepository,
                           AppUserRepository appUserRepository,
                           AppRoleRepository appRoleRepository,
                           ConsultationRepository consultationRepository,
                           SuiviConsultationRepository suiviConsultationRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            // Add sample data for patients
            patientRepository.save(new Patient(null, "Hassan", "Alami", "Hassan", "hassan.alami@email.com", "0612345678", new Date(), true, 120));
            patientRepository.save(new Patient(null, "Mohammed", "Benjelloun", "Mohammed", "mohammed.benjelloun@email.com", "0623456789", new Date(), false, 321));
            patientRepository.save(new Patient(null, "Yasmine", "Kabbaj", "Yasmine", "yasmine.kabbaj@email.com", "0634567890", new Date(), true, 165));
            patientRepository.save(new Patient(null, "Hanae", "Chraibi", "Hanae", "hanae.chraibi@email.com", "0645678901", new Date(), false, 132));

            // Add more patients for pagination testing
            // Modified to ensure scores are at least 100 (meeting the @Min(100) constraint)
            for (int i = 0; i < 20; i++) {
                // Generate random scores between 100-199 instead of 0-99
                int score = 100 + (int)(Math.random() * 100);
                patientRepository.save(new Patient(null, "Patient " + i, "Nom" + i, "Prenom" + i, 
                    "patient" + i + "@email.com", "06" + String.format("%08d", i), new Date(), Math.random() > 0.5, score));
            }
            
            // Initialize roles
            if (appRoleRepository.count() == 0) {
                appRoleRepository.save(new AppRole("USER", "Standard user"));
                appRoleRepository.save(new AppRole("ADMIN", "Administrator"));
                System.out.println("Roles have been initialized");
            }
            
            // Initialize users
            if (appUserRepository.count() == 0) {
                // Create users with encoded passwords
                AppUser user1 = new AppUser("user1Id", "user1", passwordEncoder.encode("1234"), true, null);
                user1.setRoles(Arrays.asList(appRoleRepository.findById("USER").get()));
                
                AppUser user2 = new AppUser("user2Id", "user2", passwordEncoder.encode("1234"), true, null);
                user2.setRoles(Arrays.asList(appRoleRepository.findById("USER").get()));
                
                AppUser adminUser = new AppUser("adminId", "admin", passwordEncoder.encode("1234"), true, null);
                adminUser.setRoles(Arrays.asList(
                    appRoleRepository.findById("USER").get(),
                    appRoleRepository.findById("ADMIN").get()
                ));
                
                appUserRepository.save(user1);
                appUserRepository.save(user2);
                appUserRepository.save(adminUser);
                
                System.out.println("Users have been initialized");
            }
            
            // Create sample consultations
            if (consultationRepository.count() == 0) {
                List<Patient> patients = patientRepository.findAll();
                if (!patients.isEmpty()) {
                    // Consultation externe
                    Consultation consultation1 = new Consultation();
                    consultation1.setPatient(patients.get(0));
                    consultation1.setType(TypeConsultation.CONSULTATION_EXTERNE);
                    consultation1.setDateHeureConsultation(LocalDateTime.now().minusDays(1));
                    consultation1.setMedecin("Dr. Ahmed Bennani");
                    consultation1.setMotifConsultation("Contrôle de routine");
                    consultation1.setDiagnostic("Patient en bonne santé");
                    consultation1.setStatut(StatutConsultation.TERMINEE);
                    consultation1.setNiveauUrgence(NiveauUrgence.NORMAL);
                    consultationRepository.save(consultation1);

                    // Urgence
                    Consultation consultation2 = new Consultation();
                    consultation2.setPatient(patients.get(1));
                    consultation2.setType(TypeConsultation.URGENCE);
                    consultation2.setDateHeureConsultation(LocalDateTime.now().minusHours(2));
                    consultation2.setMedecin("Dr. Fatima Zouaoui");
                    consultation2.setMotifConsultation("Douleurs thoraciques");
                    consultation2.setDiagnostic("Crise d'angoisse");
                    consultation2.setStatut(StatutConsultation.TERMINEE);
                    consultation2.setNiveauUrgence(NiveauUrgence.ELEVE);
                    consultationRepository.save(consultation2);

                    // Consultation en cours
                    Consultation consultation3 = new Consultation();
                    consultation3.setPatient(patients.get(2));
                    consultation3.setType(TypeConsultation.CONSULTATION_SPECIALISTE);
                    consultation3.setDateHeureConsultation(LocalDateTime.now().plusHours(1));
                    consultation3.setMedecin("Dr. Khalid Amrani");
                    consultation3.setMotifConsultation("Consultation cardiologie");
                    consultation3.setStatut(StatutConsultation.PROGRAMMEE);
                    consultation3.setNiveauUrgence(NiveauUrgence.NORMAL);
                    consultationRepository.save(consultation3);

                    System.out.println("Sample consultations have been created");
                    
                    // Create sample follow-ups for terminated consultations
                    if (suiviConsultationRepository.count() == 0) {
                        SuiviConsultation suivi1 = new SuiviConsultation();
                        suivi1.setConsultation(consultation1);
                        suivi1.setTypeSuivi(TypeSuivi.CONTROLE_ROUTINE);
                        suivi1.setDateSuivi(LocalDateTime.now());
                        suivi1.setPersonnel("Infirmière Sarah Khalil");
                        suivi1.setObservations("Patient se porte bien, aucun symptôme inquiétant");
                        suivi1.setStatut(StatutSuivi.AMELIORATION);
                        suivi1.setTension(12.5);
                        suivi1.setTemperature(36.8);
                        suivi1.setPouls(72);
                        suiviConsultationRepository.save(suivi1);

                        SuiviConsultation suivi2 = new SuiviConsultation();
                        suivi2.setConsultation(consultation2);
                        suivi2.setTypeSuivi(TypeSuivi.CONTROLE_URGENT);
                        suivi2.setDateSuivi(LocalDateTime.now().minusMinutes(30));
                        suivi2.setPersonnel("Dr. Fatima Zouaoui");
                        suivi2.setObservations("Suivi post-urgence, patient calme et stable");
                        suivi2.setActions("Prescription d'anxiolytiques légers");
                        suivi2.setStatut(StatutSuivi.EN_COURS);
                        suivi2.setProchainRendezVous(LocalDateTime.now().plusDays(7));
                        suiviConsultationRepository.save(suivi2);

                        System.out.println("Sample follow-ups have been created");
                    }
                }
            }
        };
    }
}