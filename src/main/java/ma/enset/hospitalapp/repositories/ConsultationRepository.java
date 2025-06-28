package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Consultation;
import ma.enset.hospitalapp.entities.StatutConsultation;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
    Page<Consultation> findByPatient(Patient patient, Pageable pageable);
    
    Page<Consultation> findByMedecin(Medecin medecin, Pageable pageable);
    
    Page<Consultation> findByStatut(StatutConsultation statut, Pageable pageable);
    
    Page<Consultation> findByDateConsultationBetween(Date dateDebut, Date dateFin, Pageable pageable);
    
    @Query("SELECT c FROM Consultation c WHERE c.dateConsultation >= :dateDebut AND c.dateConsultation <= :dateFin AND c.medecin = :medecin")
    Page<Consultation> findByMedecinAndDateRange(@Param("medecin") Medecin medecin, 
                                                 @Param("dateDebut") Date dateDebut, 
                                                 @Param("dateFin") Date dateFin, 
                                                 Pageable pageable);
    
    @Query("SELECT c FROM Consultation c WHERE c.patient = :patient ORDER BY c.dateConsultation DESC")
    List<Consultation> findRecentConsultationsByPatient(@Param("patient") Patient patient, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") StatutConsultation statut);
    
    @Query("SELECT c FROM Consultation c WHERE c.payee = false")
    Page<Consultation> findUnpaidConsultations(Pageable pageable);
    
    @Query("SELECT SUM(c.tarif) FROM Consultation c WHERE c.payee = true AND c.dateConsultation BETWEEN :dateDebut AND :dateFin")
    Double calculateRevenueBetweenDates(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
}
