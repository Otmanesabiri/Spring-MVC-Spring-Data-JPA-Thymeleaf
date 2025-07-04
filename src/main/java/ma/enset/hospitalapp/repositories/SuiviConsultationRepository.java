package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SuiviConsultationRepository extends JpaRepository<SuiviConsultation, Long> {
    
    // Recherche par consultation
    Page<SuiviConsultation> findByConsultation(Consultation consultation, Pageable pageable);
    List<SuiviConsultation> findByConsultationOrderByDateSuiviDesc(Consultation consultation);
    
    // Recherche par type de suivi
    Page<SuiviConsultation> findByTypeSuivi(TypeSuivi typeSuivi, Pageable pageable);
    
    // Recherche par statut
    Page<SuiviConsultation> findByStatut(StatutSuivi statut, Pageable pageable);
    
    // Recherche par personnel
    Page<SuiviConsultation> findByPersonnelContainingIgnoreCase(String personnel, Pageable pageable);
    
    // Recherche par période
    Page<SuiviConsultation> findByDateSuiviBetween(LocalDateTime debut, LocalDateTime fin, Pageable pageable);
    
    // Recherche par patient (via consultation)
    @Query("SELECT s FROM SuiviConsultation s WHERE s.consultation.patient = :patient ORDER BY s.dateSuivi DESC")
    Page<SuiviConsultation> findByPatient(@Param("patient") Patient patient, Pageable pageable);
    
    // Recherche par mot-clé
    @Query("SELECT s FROM SuiviConsultation s WHERE " +
           "LOWER(s.personnel) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.observations) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.actions) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.consignes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SuiviConsultation> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Suivis urgents
    @Query("SELECT s FROM SuiviConsultation s WHERE s.typeSuivi = 'CONTROLE_URGENT' OR s.statut = 'DETERIORATION' ORDER BY s.dateSuivi DESC")
    List<SuiviConsultation> findSuivisUrgents();
    
    // Suivis du jour
    @Query("SELECT s FROM SuiviConsultation s WHERE CAST(s.dateSuivi AS date) = CURRENT_DATE ORDER BY s.dateSuivi DESC")
    List<SuiviConsultation> findSuivisDuJour();
    
    // Suivis nécessitant attention
    @Query("SELECT s FROM SuiviConsultation s WHERE s.statut = 'DETERIORATION' AND s.dateSuivi >= :depuis ORDER BY s.dateSuivi DESC")
    List<SuiviConsultation> findSuivisNecessitantAttention(@Param("depuis") LocalDateTime depuis);
    
    // Statistiques
    long countByStatut(StatutSuivi statut);
    long countByTypeSuivi(TypeSuivi typeSuivi);
    
    // Dernier suivi pour une consultation
    @Query("SELECT s FROM SuiviConsultation s WHERE s.consultation = :consultation ORDER BY s.dateSuivi DESC")
    List<SuiviConsultation> findDernierSuivi(@Param("consultation") Consultation consultation, Pageable pageable);
    
    // Suivis avec prochain rendez-vous
    @Query("SELECT s FROM SuiviConsultation s WHERE s.prochainRendezVous IS NOT NULL AND s.prochainRendezVous >= CURRENT_TIMESTAMP ORDER BY s.prochainRendezVous")
    List<SuiviConsultation> findSuivisAvecProchainRdv();
    
    // Recherche par consultation ID
    @Query("SELECT s FROM SuiviConsultation s WHERE s.consultation.id = :consultationId ORDER BY s.dateSuivi DESC")
    List<SuiviConsultation> findByConsultationId(@Param("consultationId") Long consultationId);
    
    // Recherche avec filtres multiples
    @Query("SELECT s FROM SuiviConsultation s WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(s.personnel) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.observations) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:statut IS NULL OR s.statut = :statut) AND " +
           "(:type IS NULL OR s.typeSuivi = :type) " +
           "ORDER BY s.dateSuivi DESC")
    Page<SuiviConsultation> findByFilters(@Param("keyword") String keyword, 
                                         @Param("statut") StatutSuivi statut, 
                                         @Param("type") TypeSuivi type, 
                                         Pageable pageable);
}
