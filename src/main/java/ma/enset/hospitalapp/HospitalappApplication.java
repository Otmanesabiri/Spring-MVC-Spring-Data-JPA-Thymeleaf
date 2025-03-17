package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class HospitalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }

    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            // Add sample data
            patientRepository.save(new Patient(null, "Hassan", new Date(), true, 120));
            patientRepository.save(new Patient(null, "Mohammed", new Date(), false, 321));
            patientRepository.save(new Patient(null, "Yasmine", new Date(), true, 165));
            patientRepository.save(new Patient(null, "Hanae", new Date(), false, 132));

            // Add more patients for pagination testing
            for (int i = 0; i < 20; i++) {
                patientRepository.save(new Patient(null, "Patient " + i, new Date(), Math.random() > 0.5, (int)(Math.random() * 100)));
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}