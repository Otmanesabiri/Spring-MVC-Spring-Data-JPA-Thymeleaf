package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SuiviConsultationService {
    
    // CRUD de base
    SuiviConsultation saveSuiviConsultation(SuiviConsultation suiviConsultation);
    SuiviConsultation saveSuivi(SuiviConsultation suivi); // Alias pour compatibilité
    SuiviConsultation getSuiviConsultation(Long id);
    SuiviConsultation getSuiviById(Long id); // Alias pour compatibilité
    void deleteSuiviConsultation(Long id);
    void deleteSuivi(Long id); // Alias pour compatibilité
    
    // Recherche et pagination
    Page<SuiviConsultation> findAllSuivis(Pageable pageable);
    Page<SuiviConsultation> getAllSuivis(Pageable pageable); // Alias pour compatibilité
    Page<SuiviConsultation> findSuivisByKeyword(String keyword, Pageable pageable);
    Page<SuiviConsultation> rechercherSuivis(String keyword, StatutSuivi statut, TypeSuivi type, Pageable pageable);
    
    // Recherche par consultation
    Page<SuiviConsultation> findByConsultation(Consultation consultation, Pageable pageable);
    List<SuiviConsultation> getHistoriqueSuivis(Consultation consultation);
    List<SuiviConsultation> getSuivisByConsultation(Long consultationId); // Alias pour compatibilité
    
    // Recherche par patient
    Page<SuiviConsultation> findByPatient(Patient patient, Pageable pageable);
    
    // Recherche par critères
    Page<SuiviConsultation> findByTypeSuivi(TypeSuivi typeSuivi, Pageable pageable);
    Page<SuiviConsultation> findByStatut(StatutSuivi statut, Pageable pageable);
    Page<SuiviConsultation> findByPersonnel(String personnel, Pageable pageable);
    
    // Créer des suivis spécialisés
    SuiviConsultation creerSuiviRoutine(Consultation consultation, String personnel, String observations);
    SuiviConsultation creerSuiviUrgent(Consultation consultation, String personnel, String observations, String actions);
    SuiviConsultation creerSuiviPostOperatoire(Consultation consultation, String personnel, String observations);
    
    // Gestion des statuts
    SuiviConsultation changerStatutSuivi(Long id, StatutSuivi nouveauStatut);
    SuiviConsultation marquerAmelioration(Long id, String observations);
    SuiviConsultation signalerDeterioration(Long id, String observations, String actionsUrgentes);
    
    // Suivis spéciaux
    List<SuiviConsultation> getSuivisUrgents();
    List<SuiviConsultation> getSuivisDuJour();
    List<SuiviConsultation> getSuivisNecessitantAttention();
    List<SuiviConsultation> getSuivisAvecProchainRdv();
    
    // Planification
    SuiviConsultation planifierProchainSuivi(Long suiviId, LocalDateTime prochainRendezVous, String consignes);
    List<SuiviConsultation> getSuivisAPlanifier();
    
    // Statistiques
    long countByStatut(StatutSuivi statut);
    long countByTypeSuivi(TypeSuivi typeSuivi);
    
    // Rapport et dashboard
    SuiviDashboardStats getSuiviDashboardStats();
    List<SuiviConsultation> getSuivisRecents(int limit);
    List<SuiviConsultation> getDerniersSuivis(int limit); // Alias pour compatibilité
    
    // Validation
    boolean peutCreerSuivi(Consultation consultation);
    boolean necessiteAttentionUrgente(SuiviConsultation suivi);
    
    // Dernier suivi pour une consultation
    SuiviConsultation getDernierSuivi(Consultation consultation);
    
    // Pour export CSV (liste complète sans pagination)
    List<SuiviConsultation> getAllSuivis();
}
