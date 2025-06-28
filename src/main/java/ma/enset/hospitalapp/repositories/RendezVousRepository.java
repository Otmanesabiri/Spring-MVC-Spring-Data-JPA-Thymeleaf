package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.RendezVous;
import ma.enset.hospitalapp.entities.StatutRendezVous;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    @Query("SELECT r FROM RendezVous r WHERE " +
           "LOWER(r.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.medecin.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.medecin.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<RendezVous> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    Page<RendezVous> findByStatut(StatutRendezVous statut, Pageable pageable);
    
    Page<RendezVous> findByMedecin(Medecin medecin, Pageable pageable);
    
    Page<RendezVous> findByPatient(Patient patient, Pageable pageable);
    
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeureRendezVous BETWEEN :dateDebut AND :dateFin")
    List<RendezVous> findByDateHeureRendezVousBetween(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    @Query("SELECT r FROM RendezVous r WHERE r.medecin = :medecin AND r.dateHeureRendezVous BETWEEN :dateDebut AND :dateFin AND r.statut NOT IN ('ANNULE', 'REPORTE')")
    List<RendezVous> findByMedecinAndDateHeureRendezVousBetweenAndStatutNotIn(
            @Param("medecin") Medecin medecin, 
            @Param("dateDebut") Date dateDebut, 
            @Param("dateFin") Date dateFin);
    
    @Query("SELECT r FROM RendezVous r WHERE r.rappelEnvoye = false AND r.dateHeureRendezVous > :maintenant AND r.statut = 'PLANIFIE'")
    List<RendezVous> findRendezVousPourRappel(@Param("maintenant") Date maintenant);
    
    @Query("SELECT r FROM RendezVous r WHERE DATE(r.dateHeureRendezVous) = DATE(:date)")
    List<RendezVous> findByDateRendezVous(@Param("date") Date date);
}
