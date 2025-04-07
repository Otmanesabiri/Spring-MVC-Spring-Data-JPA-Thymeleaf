package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.AppRole;
import ma.enset.hospitalapp.entities.AppUser;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repositories.AppRoleRepository;
import ma.enset.hospitalapp.repositories.AppUserRepository;
import ma.enset.hospitalapp.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;

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
                           PasswordEncoder passwordEncoder) {
        return args -> {
            // Add sample data for patients
            patientRepository.save(new Patient(null, "Hassan", new Date(), true, 120));
            patientRepository.save(new Patient(null, "Mohammed", new Date(), false, 321));
            patientRepository.save(new Patient(null, "Yasmine", new Date(), true, 165));
            patientRepository.save(new Patient(null, "Hanae", new Date(), false, 132));

            // Add more patients for pagination testing
            // Modified to ensure scores are at least 100 (meeting the @Min(100) constraint)
            for (int i = 0; i < 20; i++) {
                // Generate random scores between 100-199 instead of 0-99
                int score = 100 + (int)(Math.random() * 100);
                patientRepository.save(new Patient(null, "Patient " + i, new Date(), Math.random() > 0.5, score));
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
        };
    }
}