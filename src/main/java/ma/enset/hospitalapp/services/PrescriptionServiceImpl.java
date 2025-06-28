package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Prescription;
import ma.enset.hospitalapp.entities.StatutPrescription;
import ma.enset.hospitalapp.repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Override
    public Page<Prescription> getAllPrescriptions(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return prescriptionRepository.findByNomMedicamentContainingIgnoreCase(keyword, pageable);
        }
        return prescriptionRepository.findAll(pageable);
    }

    @Override
    public Page<Prescription> getPrescriptionsByDossier(Long dossierId, Pageable pageable) {
        return prescriptionRepository.findByDossierMedicalId(dossierId, pageable);
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription introuvable avec l'ID: " + id));
    }

    @Override
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    @Override
    public List<Prescription> getActivePrescriptionsByDossier(Long dossierId) {
        return prescriptionRepository.findByDossierMedicalIdAndStatut(dossierId, StatutPrescription.ACTIVE);
    }
}
