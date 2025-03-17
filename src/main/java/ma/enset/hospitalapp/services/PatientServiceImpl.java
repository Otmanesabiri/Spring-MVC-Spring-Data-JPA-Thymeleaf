package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient getPatient(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Patient> findAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Page<Patient> findPatientsByName(String name, Pageable pageable) {
        return patientRepository.findByNameContains(name, pageable);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}