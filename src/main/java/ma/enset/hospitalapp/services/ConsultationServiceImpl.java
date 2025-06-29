package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.ConsultationRepository;
import ma.enset.hospitalapp.repositories.DossierMedicalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationServiceImpl.class);
    private final ConsultationRepository consultationRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository,
                                  DossierMedicalRepository dossierMedicalRepository) {
        this.consultationRepository = consultationRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    @Override
    public Consultation saveConsultation(Consultation consultation) {
        logger.debug("Sauvegarde d'une consultation");

        try {
            // Valider et préparer la consultation avant sauvegarde
            if (consultation.getDateHeureConsultation() == null) {
                consultation.setDateHeureConsultation(LocalDateTime.now());
            }
            if (consultation.getStatut() == null) {
                consultation.setStatut(StatutConsultation.PROGRAMMEE);
            }
            if (consultation.getNiveauUrgence() == null) {
                consultation.setNiveauUrgence(NiveauUrgence.NORMAL);
            }
            
            return consultationRepository.save(consultation);
        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde d'une consultation", e);
            throw e;
        }
    }

    @Override
    public Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }

    @Override
    public Consultation getConsultationById(Long id) {
        return getConsultation(id); // Alias pour getConsultation
    }

    @Override
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }

    @Override
    public Page<Consultation> findAllConsultations(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }

    @Override
    public Page<Consultation> findConsultationsByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllConsultations(pageable);
        }
        return consultationRepository.findByKeyword(keyword.trim(), pageable);
    }

    @Override
    public Page<Consultation> findByPatient(Patient patient, Pageable pageable) {
        return consultationRepository.findByPatient(patient, pageable);
    }

    @Override
    public Page<Consultation> findByType(TypeConsultation type, Pageable pageable) {
        return consultationRepository.findByType(type, pageable);
    }

    @Override
    public Page<Consultation> findByStatut(StatutConsultation statut, Pageable pageable) {
        return consultationRepository.findByStatut(statut, pageable);
    }

    @Override
    public Page<Consultation> findByNiveauUrgence(NiveauUrgence niveauUrgence, Pageable pageable) {
        return consultationRepository.findByNiveauUrgence(niveauUrgence, pageable);
    }

    @Override
    public Page<Consultation> findByMedecin(String medecin, Pageable pageable) {
        return consultationRepository.findByMedecinContainingIgnoreCase(medecin, pageable);
    }

    @Override
    public Page<Consultation> findConsultationsFiltered(Patient patient, TypeConsultation type, 
                                                       StatutConsultation statut, NiveauUrgence niveauUrgence, 
                                                       String medecin, LocalDateTime dateDebut, 
                                                       LocalDateTime dateFin, Pageable pageable) {
        return consultationRepository.findConsultationsFiltered(
            patient, type, statut, niveauUrgence, medecin, dateDebut, dateFin, pageable
        );
    }

    @Override
    public Consultation changerStatutConsultation(Long id, StatutConsultation nouveauStatut) {
        Consultation consultation = getConsultation(id);
        if (consultation != null) {
            consultation.setStatut(nouveauStatut);
            return saveConsultation(consultation);
        }
        return null;
    }

    @Override
    public Consultation marquerCommeTerminee(Long id) {
        return changerStatutConsultation(id, StatutConsultation.TERMINEE);
    }

    @Override
    public Consultation annulerConsultation(Long id, String motif) {
        Consultation consultation = getConsultation(id);
        if (consultation != null) {
            consultation.setStatut(StatutConsultation.ANNULEE);
            if (motif != null && !motif.trim().isEmpty()) {
                String observationsActuelles = consultation.getObservationsInfirmiere() != null 
                    ? consultation.getObservationsInfirmiere() : "";
                consultation.setObservationsInfirmiere(observationsActuelles + "\nMotif d'annulation: " + motif);
            }
            return saveConsultation(consultation);
        }
        return null;
    }

    @Override
    public Consultation reporterConsultation(Long id, LocalDateTime nouvelleDate) {
        Consultation consultation = getConsultation(id);
        if (consultation != null) {
            consultation.setDateHeureConsultation(nouvelleDate);
            consultation.setStatut(StatutConsultation.REPORTEE);
            return saveConsultation(consultation);
        }
        return null;
    }

    @Override
    public Consultation creerConsultationUrgence(Patient patient, String medecin, 
                                                NiveauUrgence niveauUrgence, String motif) {
        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        consultation.setMedecin(medecin);
        consultation.setType(TypeConsultation.URGENCE);
        consultation.setNiveauUrgence(niveauUrgence);
        consultation.setMotifConsultation(motif);
        consultation.setDateHeureConsultation(LocalDateTime.now());
        consultation.setStatut(StatutConsultation.EN_ATTENTE);
        
        return saveConsultation(consultation);
    }

    @Override
    public List<Consultation> getConsultationsDuJour() {
        return consultationRepository.findConsultationsDuJour();
    }

    @Override
    public Page<Consultation> getConsultationsDuJour(Pageable pageable) {
        return consultationRepository.findConsultationsDuJour(pageable);
    }

    @Override
    public List<Consultation> getUrgencesEnAttente() {
        return consultationRepository.findUrgencesEnAttente();
    }

    @Override
    public List<Consultation> getConsultationsEnRetard() {
        return consultationRepository.findConsultationsEnRetard();
    }

    @Override
    public List<Consultation> getHospitalisationsActives() {
        // Utilisation de la méthode simplifiée pour éviter les problèmes de performance
        List<StatutConsultation> statutsActifs = List.of(
            StatutConsultation.PROGRAMMEE,
            StatutConsultation.EN_COURS
        );
        return consultationRepository.findByTypeAndStatutIn(
            TypeConsultation.HOSPITALISATION,
            statutsActifs
        );
    }

    @Override
    public List<Consultation> getConsultationsTerminees() {
        return consultationRepository.findByStatut(StatutConsultation.TERMINEE);
    }

    @Override
    public List<Consultation> getHistoriqueConsultations(Patient patient) {
        return consultationRepository.findByPatientOrderByDateHeureConsultationDesc(patient);
    }

    @Override
    public Page<Consultation> getHistoriqueConsultations(Patient patient, Pageable pageable) {
        return consultationRepository.findByPatient(patient, pageable);
    }

    @Override
    public Page<Consultation> getConsultationsByDossier(DossierMedical dossierMedical, Pageable pageable) {
        return consultationRepository.findByDossierMedical(dossierMedical, pageable);
    }

    @Override
    public Consultation lierAuDossierMedical(Long consultationId, Long dossierId) {
        Consultation consultation = getConsultation(consultationId);
        DossierMedical dossier = dossierMedicalRepository.findById(dossierId).orElse(null);
        
        if (consultation != null && dossier != null) {
            consultation.setDossierMedical(dossier);
            return saveConsultation(consultation);
        }
        return null;
    }

    @Override
    public long countByStatut(StatutConsultation statut) {
        return consultationRepository.countByStatut(statut);
    }

    @Override
    public long countByType(TypeConsultation type) {
        return consultationRepository.countByType(type);
    }

    @Override
    public long countByNiveauUrgence(NiveauUrgence niveauUrgence) {
        return consultationRepository.countByNiveauUrgence(niveauUrgence);
    }

    @Override
    public ConsultationDashboardStats getDashboardStats() {
        ConsultationDashboardStats stats = new ConsultationDashboardStats();
        
        // Statistiques générales
        stats.setTotalConsultations(consultationRepository.count());
        stats.setConsultationsDuJour(consultationRepository.findConsultationsDuJour().size());
        stats.setConsultationsEnCours(countByStatut(StatutConsultation.EN_COURS));
        stats.setConsultationsTerminees(countByStatut(StatutConsultation.TERMINEE));
        
        // Urgences
        stats.setUrgencesEnAttente(consultationRepository.findUrgencesEnAttente().size());
        stats.setUrgencesCritiques(countByNiveauUrgence(NiveauUrgence.CRITIQUE));
        stats.setConsultationsEnRetard(consultationRepository.findConsultationsEnRetard().size());
        
        // Hospitalisations
        stats.setHospitalisationsActives(getHospitalisationsActives().size());

        // Consultations par type
        stats.setConsultationsExternes(countByType(TypeConsultation.CONSULTATION_EXTERNE));
        stats.setSuiviPostConsultation(countByType(TypeConsultation.SUIVI_POST_CONSULTATION));
        stats.setTeleconsultations(countByType(TypeConsultation.TELECONSULTATION));
        
        // Répartitions
        Map<StatutConsultation, Long> repartitionStatut = new HashMap<>();
        for (StatutConsultation statut : StatutConsultation.values()) {
            repartitionStatut.put(statut, countByStatut(statut));
        }
        stats.setRepartitionParStatut(repartitionStatut);
        
        Map<TypeConsultation, Long> repartitionType = new HashMap<>();
        for (TypeConsultation type : TypeConsultation.values()) {
            repartitionType.put(type, countByType(type));
        }
        stats.setRepartitionParType(repartitionType);
        
        Map<NiveauUrgence, Long> repartitionUrgence = new HashMap<>();
        for (NiveauUrgence niveau : NiveauUrgence.values()) {
            repartitionUrgence.put(niveau, countByNiveauUrgence(niveau));
        }
        stats.setRepartitionParUrgence(repartitionUrgence);
        
        // Calcul du taux d'occupation
        long consultationsActives = countByStatut(StatutConsultation.EN_COURS) +
                                   countByStatut(StatutConsultation.EN_ATTENTE);
        stats.setTauxOccupation((double) consultationsActives / Math.max(stats.getConsultationsDuJour(), 1) * 100);
        
        return stats;
    }

    @Override
    public List<Consultation> getConsultationsRecentes(int limit) {
        return consultationRepository.findAll(Pageable.ofSize(limit)).getContent();
    }

    @Override
    public List<Consultation> getUrgencesPrioritaires() {
        return getUrgencesEnAttente(); // Même logique, pas besoin de dupliquer
    }
}
