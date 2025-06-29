package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
    // Recherche par entités liées
    Page<Consultation> findByPatient(Patient patient, Pageable pageable);
    Page<Consultation> findByStatut(StatutConsultation statut, Pageable pageable);
    List<Consultation> findByStatut(StatutConsultation statut); // Version sans pagination
    
    // Recherche par type et urgence
    Page<Consultation> findByType(TypeConsultation type, Pageable pageable);
    Page<Consultation> findByNiveauUrgence(NiveauUrgence niveauUrgence, Pageable pageable);
    
    // Recherche par médecin
    Page<Consultation> findByMedecinContainingIgnoreCase(String medecin, Pageable pageable);
    
    // Recherche par dates
    Page<Consultation> findByDateHeureConsultationBetween(LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable);
    
    // Recherche par mot-clé
    @Query("SELECT c FROM Consultation c WHERE " +
           "LOWER(c.medecin) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.motifConsultation) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.diagnostic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.observationsInfirmiere) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Consultation> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Recherche combinée avec filtres
    @Query("SELECT c FROM Consultation c WHERE " +
           "(:patient IS NULL OR c.patient = :patient) AND " +
           "(:type IS NULL OR c.type = :type) AND " +
           "(:statut IS NULL OR c.statut = :statut) AND " +
           "(:niveauUrgence IS NULL OR c.niveauUrgence = :niveauUrgence) AND " +
           "(:medecin IS NULL OR :medecin = '' OR LOWER(c.medecin) LIKE LOWER(CONCAT('%', :medecin, '%'))) AND " +
           "(:dateDebut IS NULL OR c.dateHeureConsultation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateHeureConsultation <= :dateFin) " +
           "ORDER BY c.dateHeureConsultation DESC")
    Page<Consultation> findConsultationsFiltered(@Param("patient") Patient patient,
                                                @Param("type") TypeConsultation type,
                                                @Param("statut") StatutConsultation statut,
                                                @Param("niveauUrgence") NiveauUrgence niveauUrgence,
                                                @Param("medecin") String medecin,
                                                @Param("dateDebut") LocalDateTime dateDebut,
                                                @Param("dateFin") LocalDateTime dateFin,
                                                Pageable pageable);
    
    // Consultations du jour
    @Query("SELECT c FROM Consultation c WHERE DATE(c.dateHeureConsultation) = DATE(CURRENT_DATE) ORDER BY c.dateHeureConsultation")
    List<Consultation> findConsultationsDuJour();
    
    @Query("SELECT c FROM Consultation c WHERE DATE(c.dateHeureConsultation) = DATE(CURRENT_DATE) ORDER BY c.dateHeureConsultation")
    Page<Consultation> findConsultationsDuJour(Pageable pageable);
    
    // Urgences
    @Query("SELECT c FROM Consultation c WHERE c.type = 'URGENCE' AND c.statut IN ('PROGRAMMEE', 'EN_COURS') ORDER BY c.niveauUrgence DESC, c.dateHeureConsultation")
    List<Consultation> findUrgencesEnAttente();
    
    // Consultations en retard
    @Query("SELECT c FROM Consultation c WHERE c.statut = 'PROGRAMMEE' AND c.dateHeureConsultation < CURRENT_TIMESTAMP ORDER BY c.dateHeureConsultation")
    List<Consultation> findConsultationsEnRetard();
    
    // Hospitalisations actives
    @Query("SELECT c FROM Consultation c WHERE c.type = :hospitalisationType AND c.statut IN (:statutActif1, :statutActif2) ORDER BY c.dateHeureConsultation")
    List<Consultation> findHospitalisationsActives(@Param("hospitalisationType") TypeConsultation hospitalisationType,
                                                   @Param("statutActif1") StatutConsultation statutActif1,
                                                   @Param("statutActif2") StatutConsultation statutActif2);

    // Hospitalisations actives - Version simplifiée pour éviter les problèmes de performance
    List<Consultation> findByTypeAndStatutIn(TypeConsultation type, List<StatutConsultation> statuts);

    // Historique patient
    List<Consultation> findByPatientOrderByDateHeureConsultationDesc(Patient patient);
    
    // Recherche par dossier médical
    Page<Consultation> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    // Statistiques
    long countByType(TypeConsultation type);
    long countByNiveauUrgence(NiveauUrgence niveauUrgence);
    
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") StatutConsultation statut);
}
