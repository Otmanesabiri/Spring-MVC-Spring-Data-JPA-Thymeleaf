package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Override
    public Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Consultation> findAllConsultations(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByPatient(Patient patient, Pageable pageable) {
        return consultationRepository.findByPatient(patient, pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByMedecin(Medecin medecin, Pageable pageable) {
        return consultationRepository.findByMedecin(medecin, pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByStatut(StatutConsultation statut, Pageable pageable) {
        return consultationRepository.findByStatut(statut, pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByDateRange(Date dateDebut, Date dateFin, Pageable pageable) {
        return consultationRepository.findByDateConsultationBetween(dateDebut, dateFin, pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByMedecinAndDateRange(Medecin medecin, Date dateDebut, Date dateFin, Pageable pageable) {
        return consultationRepository.findByMedecinAndDateRange(medecin, dateDebut, dateFin, pageable);
    }

    @Override
    public List<Consultation> getRecentConsultationsByPatient(Patient patient, int limit) {
        return consultationRepository.findRecentConsultationsByPatient(patient, Pageable.ofSize(limit));
    }

    @Override
    public Page<Consultation> findUnpaidConsultations(Pageable pageable) {
        return consultationRepository.findUnpaidConsultations(pageable);
    }

    @Override
    public Consultation saveConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }

    @Override
    public void marquerCommePaye(Long id) {
        Consultation consultation = getConsultation(id);
        if (consultation != null) {
            consultation.setPayee(true);
            saveConsultation(consultation);
        }
    }

    @Override
    public void changerStatut(Long id, StatutConsultation nouveauStatut) {
        Consultation consultation = getConsultation(id);
        if (consultation != null) {
            consultation.setStatut(nouveauStatut);
            saveConsultation(consultation);
        }
    }

    @Override
    public long countConsultationsByStatut(StatutConsultation statut) {
        return consultationRepository.countByStatut(statut);
    }

    @Override
    public Double calculateRevenueBetweenDates(Date dateDebut, Date dateFin) {
        return consultationRepository.calculateRevenueBetweenDates(dateDebut, dateFin);
    }
}
