package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.SuiviConsultationRepository;
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
public class SuiviConsultationServiceImpl implements SuiviConsultationService {

    private final SuiviConsultationRepository suiviConsultationRepository;

    public SuiviConsultationServiceImpl(SuiviConsultationRepository suiviConsultationRepository) {
        this.suiviConsultationRepository = suiviConsultationRepository;
    }

    @Override
    public SuiviConsultation saveSuiviConsultation(SuiviConsultation suiviConsultation) {
        // Valider et préparer le suivi avant sauvegarde
        if (suiviConsultation.getDateSuivi() == null) {
            suiviConsultation.setDateSuivi(LocalDateTime.now());
        }
        if (suiviConsultation.getStatut() == null) {
            suiviConsultation.setStatut(StatutSuivi.EN_COURS);
        }
        
        return suiviConsultationRepository.save(suiviConsultation);
    }

    @Override
    public SuiviConsultation saveSuivi(SuiviConsultation suivi) {
        return saveSuiviConsultation(suivi);
    }

    @Override
    public SuiviConsultation getSuiviConsultation(Long id) {
        return suiviConsultationRepository.findById(id).orElse(null);
    }

    @Override
    public SuiviConsultation getSuiviById(Long id) {
        return getSuiviConsultation(id);
    }

    @Override
    public void deleteSuiviConsultation(Long id) {
        suiviConsultationRepository.deleteById(id);
    }

    @Override
    public void deleteSuivi(Long id) {
        deleteSuiviConsultation(id);
    }

    @Override
    public Page<SuiviConsultation> findAllSuivis(Pageable pageable) {
        return suiviConsultationRepository.findAll(pageable);
    }

    @Override
    public Page<SuiviConsultation> getAllSuivis(Pageable pageable) {
        return findAllSuivis(pageable);
    }

    // Pour export CSV (liste complète sans pagination)
    @Override
    public List<SuiviConsultation> getAllSuivis() {
        return suiviConsultationRepository.findAll();
    }

    @Override
    public Page<SuiviConsultation> findSuivisByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllSuivis(pageable);
        }
        return suiviConsultationRepository.findByKeyword(keyword.trim(), pageable);
    }

    @Override
    public Page<SuiviConsultation> findByConsultation(Consultation consultation, Pageable pageable) {
        return suiviConsultationRepository.findByConsultation(consultation, pageable);
    }

    @Override
    public List<SuiviConsultation> getHistoriqueSuivis(Consultation consultation) {
        return suiviConsultationRepository.findByConsultationOrderByDateSuiviDesc(consultation);
    }

    @Override
    public Page<SuiviConsultation> findByPatient(Patient patient, Pageable pageable) {
        return suiviConsultationRepository.findByPatient(patient, pageable);
    }

    @Override
    public Page<SuiviConsultation> findByTypeSuivi(TypeSuivi typeSuivi, Pageable pageable) {
        return suiviConsultationRepository.findByTypeSuivi(typeSuivi, pageable);
    }

    @Override
    public Page<SuiviConsultation> findByStatut(StatutSuivi statut, Pageable pageable) {
        return suiviConsultationRepository.findByStatut(statut, pageable);
    }

    @Override
    public Page<SuiviConsultation> findByPersonnel(String personnel, Pageable pageable) {
        return suiviConsultationRepository.findByPersonnelContainingIgnoreCase(personnel, pageable);
    }

    @Override
    public SuiviConsultation creerSuiviRoutine(Consultation consultation, String personnel, String observations) {
        SuiviConsultation suivi = new SuiviConsultation();
        suivi.setConsultation(consultation);
        suivi.setTypeSuivi(TypeSuivi.CONTROLE_ROUTINE);
        suivi.setPersonnel(personnel);
        suivi.setObservations(observations);
        suivi.setDateSuivi(LocalDateTime.now());
        suivi.setStatut(StatutSuivi.EN_COURS);
        
        return saveSuiviConsultation(suivi);
    }

    @Override
    public SuiviConsultation creerSuiviUrgent(Consultation consultation, String personnel, 
                                             String observations, String actions) {
        SuiviConsultation suivi = new SuiviConsultation();
        suivi.setConsultation(consultation);
        suivi.setTypeSuivi(TypeSuivi.CONTROLE_URGENT);
        suivi.setPersonnel(personnel);
        suivi.setObservations(observations);
        suivi.setActions(actions);
        suivi.setDateSuivi(LocalDateTime.now());
        suivi.setStatut(StatutSuivi.EN_COURS);
        
        return saveSuiviConsultation(suivi);
    }

    @Override
    public SuiviConsultation creerSuiviPostOperatoire(Consultation consultation, String personnel, String observations) {
        SuiviConsultation suivi = new SuiviConsultation();
        suivi.setConsultation(consultation);
        suivi.setTypeSuivi(TypeSuivi.SUIVI_POST_OPERATOIRE);
        suivi.setPersonnel(personnel);
        suivi.setObservations(observations);
        suivi.setDateSuivi(LocalDateTime.now());
        suivi.setStatut(StatutSuivi.EN_COURS);
        
        return saveSuiviConsultation(suivi);
    }

    @Override
    public SuiviConsultation changerStatutSuivi(Long id, StatutSuivi nouveauStatut) {
        SuiviConsultation suivi = getSuiviConsultation(id);
        if (suivi != null) {
            suivi.setStatut(nouveauStatut);
            return saveSuiviConsultation(suivi);
        }
        return null;
    }

    @Override
    public SuiviConsultation marquerAmelioration(Long id, String observations) {
        SuiviConsultation suivi = getSuiviConsultation(id);
        if (suivi != null) {
            suivi.setStatut(StatutSuivi.AMELIORATION);
            if (observations != null && !observations.trim().isEmpty()) {
                String observationsActuelles = suivi.getObservations() != null ? suivi.getObservations() : "";
                suivi.setObservations(observationsActuelles + "\n[AMÉLIORATION] " + observations);
            }
            return saveSuiviConsultation(suivi);
        }
        return null;
    }

    @Override
    public SuiviConsultation signalerDeterioration(Long id, String observations, String actionsUrgentes) {
        SuiviConsultation suivi = getSuiviConsultation(id);
        if (suivi != null) {
            suivi.setStatut(StatutSuivi.DETERIORATION);
            if (observations != null && !observations.trim().isEmpty()) {
                String observationsActuelles = suivi.getObservations() != null ? suivi.getObservations() : "";
                suivi.setObservations(observationsActuelles + "\n[DÉTÉRIORATION] " + observations);
            }
            if (actionsUrgentes != null && !actionsUrgentes.trim().isEmpty()) {
                String actionsActuelles = suivi.getActions() != null ? suivi.getActions() : "";
                suivi.setActions(actionsActuelles + "\n[URGENT] " + actionsUrgentes);
            }
            return saveSuiviConsultation(suivi);
        }
        return null;
    }

    @Override
    public List<SuiviConsultation> getSuivisUrgents() {
        return suiviConsultationRepository.findSuivisUrgents();
    }

    @Override
    public List<SuiviConsultation> getSuivisDuJour() {
        return suiviConsultationRepository.findSuivisDuJour();
    }

    @Override
    public List<SuiviConsultation> getSuivisNecessitantAttention() {
        LocalDateTime depuis24h = LocalDateTime.now().minusDays(1);
        return suiviConsultationRepository.findSuivisNecessitantAttention(depuis24h);
    }

    @Override
    public List<SuiviConsultation> getSuivisAvecProchainRdv() {
        return suiviConsultationRepository.findSuivisAvecProchainRdv();
    }

    @Override
    public SuiviConsultation planifierProchainSuivi(Long suiviId, LocalDateTime prochainRendezVous, String consignes) {
        SuiviConsultation suivi = getSuiviConsultation(suiviId);
        if (suivi != null) {
            suivi.setProchainRendezVous(prochainRendezVous);
            suivi.setConsignes(consignes);
            return saveSuiviConsultation(suivi);
        }
        return null;
    }

    @Override
    public List<SuiviConsultation> getSuivisAPlanifier() {
        // Retourner les suivis qui nécessitent une planification
        return suiviConsultationRepository.findByStatut(StatutSuivi.EN_COURS, Pageable.unpaged()).getContent();
    }

    @Override
    public long countByStatut(StatutSuivi statut) {
        return suiviConsultationRepository.countByStatut(statut);
    }

    @Override
    public long countByTypeSuivi(TypeSuivi typeSuivi) {
        return suiviConsultationRepository.countByTypeSuivi(typeSuivi);
    }

    @Override
    public SuiviDashboardStats getSuiviDashboardStats() {
        SuiviDashboardStats stats = new SuiviDashboardStats();
        
        // Statistiques générales
        stats.setTotalSuivis(suiviConsultationRepository.count());
        stats.setSuivisDuJour(getSuivisDuJour().size());
        stats.setSuivisEnCours(countByStatut(StatutSuivi.EN_COURS));
        stats.setSuivisTermines(countByStatut(StatutSuivi.TERMINE));
        
        // Suivis par statut
        stats.setSuivisAmelioration(countByStatut(StatutSuivi.AMELIORATION));
        stats.setSuivisStables(countByStatut(StatutSuivi.STABLE));
        stats.setSuivisDeterioration(countByStatut(StatutSuivi.DETERIORATION));
        stats.setSuivisGueris(countByStatut(StatutSuivi.GUERI));
        
        // Suivis par type
        stats.setSuivisRoutine(countByTypeSuivi(TypeSuivi.CONTROLE_ROUTINE));
        stats.setSuivisUrgents(countByTypeSuivi(TypeSuivi.CONTROLE_URGENT));
        stats.setSuivisPostOperatoires(countByTypeSuivi(TypeSuivi.SUIVI_POST_OPERATOIRE));
        stats.setReevaluations(countByTypeSuivi(TypeSuivi.REEVALUATION));
        
        // Alertes
        stats.setSuivisNecessitantAttention(getSuivisNecessitantAttention().size());
        stats.setSuivisAvecProchainRdv(getSuivisAvecProchainRdv().size());
        
        // Répartitions
        Map<StatutSuivi, Long> repartitionStatut = new HashMap<>();
        for (StatutSuivi statut : StatutSuivi.values()) {
            repartitionStatut.put(statut, countByStatut(statut));
        }
        stats.setRepartitionParStatut(repartitionStatut);
        
        Map<TypeSuivi, Long> repartitionType = new HashMap<>();
        for (TypeSuivi type : TypeSuivi.values()) {
            repartitionType.put(type, countByTypeSuivi(type));
        }
        stats.setRepartitionParType(repartitionType);
        
        // Calcul des taux
        if (stats.getTotalSuivis() > 0) {
            stats.setTauxAmelioration((double) stats.getSuivisAmelioration() / stats.getTotalSuivis() * 100);
            stats.setTauxDeterioration((double) stats.getSuivisDeterioration() / stats.getTotalSuivis() * 100);
        }
        
        return stats;
    }

    @Override
    public List<SuiviConsultation> getSuivisRecents(int limit) {
        return suiviConsultationRepository.findAll(Pageable.ofSize(limit)).getContent();
    }

    @Override
    public List<SuiviConsultation> getDerniersSuivis(int limit) {
        return getSuivisRecents(limit);
    }

    @Override
    public List<SuiviConsultation> getSuivisByConsultation(Long consultationId) {
        return suiviConsultationRepository.findByConsultationId(consultationId);
    }

    @Override
    public Page<SuiviConsultation> rechercherSuivis(String keyword, StatutSuivi statut, TypeSuivi type, Pageable pageable) {
        return suiviConsultationRepository.findByFilters(keyword, statut, type, pageable);
    }

    @Override
    public boolean peutCreerSuivi(Consultation consultation) {
        return consultation != null && !consultation.getStatut().isTermine();
    }

    @Override
    public boolean necessiteAttentionUrgente(SuiviConsultation suivi) {
        return suivi.getTypeSuivi() == TypeSuivi.CONTROLE_URGENT || 
               suivi.getStatut() == StatutSuivi.DETERIORATION;
    }

    @Override
    public SuiviConsultation getDernierSuivi(Consultation consultation) {
        List<SuiviConsultation> suivis = suiviConsultationRepository.findDernierSuivi(consultation, Pageable.ofSize(1));
        return suivis.isEmpty() ? null : suivis.get(0);
    }
}
