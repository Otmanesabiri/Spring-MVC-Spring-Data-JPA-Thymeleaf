package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.HistoriqueMedical;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.TypeEvenementMedical;
import ma.enset.hospitalapp.entities.NiveauGravite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface HistoriqueMedicalRepository extends JpaRepository<HistoriqueMedical, Long> {
    
    Page<HistoriqueMedical> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<HistoriqueMedical> findByDossierMedicalId(Long dossierId, Pageable pageable);
    
    Page<HistoriqueMedical> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
    
    List<HistoriqueMedical> findTop5ByDossierMedicalIdOrderByDateEvenementDesc(Long dossierId);
    
    Page<HistoriqueMedical> findByMedecin(Medecin medecin, Pageable pageable);
    
    Page<HistoriqueMedical> findByTypeEvenement(TypeEvenementMedical typeEvenement, Pageable pageable);
    
    Page<HistoriqueMedical> findByNiveauGravite(NiveauGravite niveauGravite, Pageable pageable);
    
    @Query("SELECT h FROM HistoriqueMedical h WHERE h.dossierMedical = :dossier AND " +
           "(LOWER(h.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<HistoriqueMedical> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                                    @Param("keyword") String keyword, 
                                                    Pageable pageable);
    
    @Query("SELECT h FROM HistoriqueMedical h WHERE h.dossierMedical = :dossier AND " +
           "h.dateEvenement BETWEEN :dateDebut AND :dateFin ORDER BY h.dateEvenement DESC")
    List<HistoriqueMedical> findByDossierAndDateEvenementBetween(@Param("dossier") DossierMedical dossier,
                                                                @Param("dateDebut") Date dateDebut,
                                                                @Param("dateFin") Date dateFin);
    
    @Query("SELECT h FROM HistoriqueMedical h WHERE h.dossierMedical = :dossier ORDER BY h.dateEvenement DESC")
    List<HistoriqueMedical> findByDossierMedicalOrderByDateEvenementDesc(@Param("dossier") DossierMedical dossier);
}
