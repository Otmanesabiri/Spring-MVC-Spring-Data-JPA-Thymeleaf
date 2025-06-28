package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.entities.StatutDossier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    
    Optional<DossierMedical> findByPatient(Patient patient);
    
    Optional<DossierMedical> findByNumeroDossier(String numeroDossier);
    
    Page<DossierMedical> findByStatut(StatutDossier statut, Pageable pageable);
    
    @Query("SELECT d FROM DossierMedical d WHERE " +
           "LOWER(d.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.numeroDossier) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<DossierMedical> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT d FROM DossierMedical d WHERE d.dateCreation BETWEEN :dateDebut AND :dateFin")
    List<DossierMedical> findByDateCreationBetween(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    @Query("SELECT COUNT(d) FROM DossierMedical d WHERE d.statut = :statut")
    long countByStatut(@Param("statut") StatutDossier statut);
    
    @Query("SELECT d FROM DossierMedical d WHERE d.dateDerniereModification < :dateLimit AND d.statut = 'ACTIF'")
    List<DossierMedical> findDossiersInactifs(@Param("dateLimit") Date dateLimit);
}
