package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    Patient getPatient(Long id);
    Page<Patient> findAllPatients(Pageable pageable);
    Page<Patient> findPatientsByName(String name, Pageable pageable);
    Patient savePatient(Patient patient);
    void deletePatient(Long id);
}