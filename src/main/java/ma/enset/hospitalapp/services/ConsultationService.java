package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Consultation;
import ma.enset.hospitalapp.entities.StatutConsultation;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ConsultationService {
    Consultation getConsultation(Long id);
    Page<Consultation> findAllConsultations(Pageable pageable);
    Page<Consultation> findConsultationsByPatient(Patient patient, Pageable pageable);
    Page<Consultation> findConsultationsByMedecin(Medecin medecin, Pageable pageable);
    Page<Consultation> findConsultationsByStatut(StatutConsultation statut, Pageable pageable);
    Page<Consultation> findConsultationsByDateRange(Date dateDebut, Date dateFin, Pageable pageable);
    Page<Consultation> findConsultationsByMedecinAndDateRange(Medecin medecin, Date dateDebut, Date dateFin, Pageable pageable);
    List<Consultation> getRecentConsultationsByPatient(Patient patient, int limit);
    Page<Consultation> findUnpaidConsultations(Pageable pageable);
    Consultation saveConsultation(Consultation consultation);
    void deleteConsultation(Long id);
    void marquerCommePaye(Long id);
    void changerStatut(Long id, StatutConsultation nouveauStatut);
    long countConsultationsByStatut(StatutConsultation statut);
    Double calculateRevenueBetweenDates(Date dateDebut, Date dateFin);
}
