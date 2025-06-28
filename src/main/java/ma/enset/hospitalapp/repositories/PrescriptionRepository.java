package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Prescription;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.StatutPrescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    Page<Prescription> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<Prescription> findByDossierMedicalId(Long dossierId, Pageable pageable);
    
    Page<Prescription> findByNomMedicamentContainingIgnoreCase(String keyword, Pageable pageable);
    
    List<Prescription> findByDossierMedicalIdAndStatut(Long dossierId, StatutPrescription statut);
    
    Page<Prescription> findByMedecin(Medecin medecin, Pageable pageable);
    
    Page<Prescription> findByStatut(StatutPrescription statut, Pageable pageable);
    
    @Query("SELECT p FROM Prescription p WHERE p.dossierMedical = :dossier AND " +
           "(LOWER(p.nomMedicament) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.instructions) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Prescription> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                              @Param("keyword") String keyword, 
                                              Pageable pageable);
    
    @Query("SELECT p FROM Prescription p WHERE p.dossierMedical = :dossier AND p.statut = 'ACTIVE'")
    List<Prescription> findActivePrescriptionsByDossier(@Param("dossier") DossierMedical dossier);
    
    @Query("SELECT p FROM Prescription p WHERE p.dateFin < :dateActuelle AND p.statut = 'ACTIVE'")
    List<Prescription> findExpiredPrescriptions(@Param("dateActuelle") Date dateActuelle);
    
    @Query("SELECT p FROM Prescription p WHERE p.dateFin BETWEEN :dateDebut AND :dateFin AND p.statut = 'ACTIVE'")
    List<Prescription> findPrescriptionsExpiringBetween(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    @Query("SELECT p FROM Prescription p WHERE p.dossierMedical = :dossier ORDER BY p.datePrescription DESC")
    List<Prescription> findByDossierMedicalOrderByDatePrescriptionDesc(@Param("dossier") DossierMedical dossier);
}
