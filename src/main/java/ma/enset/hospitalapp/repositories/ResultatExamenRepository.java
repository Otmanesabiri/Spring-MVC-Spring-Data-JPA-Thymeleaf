package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.ResultatExamen;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.TypeExamen;
import ma.enset.hospitalapp.entities.StatutResultat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResultatExamenRepository extends JpaRepository<ResultatExamen, Long> {
    
    Page<ResultatExamen> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<ResultatExamen> findByDossierMedicalId(Long dossierId, Pageable pageable);
    
    Page<ResultatExamen> findByNomExamenContainingIgnoreCase(String keyword, Pageable pageable);
    
    List<ResultatExamen> findTop5ByDossierMedicalIdOrderByDateExamenDesc(Long dossierId);
    
    Page<ResultatExamen> findByMedecinPrescripteur(Medecin medecin, Pageable pageable);
    
    Page<ResultatExamen> findByTypeExamen(TypeExamen typeExamen, Pageable pageable);
    
    Page<ResultatExamen> findByStatut(StatutResultat statut, Pageable pageable);
    
    @Query("SELECT r FROM ResultatExamen r WHERE r.dossierMedical = :dossier AND " +
           "(LOWER(r.nomExamen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.resultats) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.interpretation) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ResultatExamen> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                                 @Param("keyword") String keyword, 
                                                 Pageable pageable);
    
    @Query("SELECT r FROM ResultatExamen r WHERE r.dossierMedical = :dossier AND " +
           "r.dateExamen BETWEEN :dateDebut AND :dateFin ORDER BY r.dateExamen DESC")
    List<ResultatExamen> findByDossierAndDateExamenBetween(@Param("dossier") DossierMedical dossier,
                                                          @Param("dateDebut") Date dateDebut,
                                                          @Param("dateFin") Date dateFin);
    
    @Query("SELECT r FROM ResultatExamen r WHERE r.urgent = true AND r.statut IN ('ANORMAL', 'PATHOLOGIQUE', 'URGENT')")
    List<ResultatExamen> findUrgentResults();
    
    @Query("SELECT r FROM ResultatExamen r WHERE r.dossierMedical = :dossier ORDER BY r.dateExamen DESC")
    List<ResultatExamen> findByDossierMedicalOrderByDateExamenDesc(@Param("dossier") DossierMedical dossier);
}
