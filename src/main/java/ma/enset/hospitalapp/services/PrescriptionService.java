package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PrescriptionService {
    Page<Prescription> getAllPrescriptions(String keyword, Pageable pageable);
    Page<Prescription> getPrescriptionsByDossier(Long dossierId, Pageable pageable);
    Prescription getPrescriptionById(Long id);
    Prescription savePrescription(Prescription prescription);
    void deletePrescription(Long id);
    List<Prescription> getActivePrescriptionsByDossier(Long dossierId);
}
