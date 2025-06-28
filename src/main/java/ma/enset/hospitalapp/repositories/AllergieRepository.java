package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Allergie;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.TypeAllergie;
import ma.enset.hospitalapp.entities.SeveriteAllergie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AllergieRepository extends JpaRepository<Allergie, Long> {
    
    Page<Allergie> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<Allergie> findByDossierMedicalId(Long dossierId, Pageable pageable);
    
    Page<Allergie> findByAllergeneContainingIgnoreCase(String keyword, Pageable pageable);
    
    List<Allergie> findByDossierMedicalIdAndActiveTrue(Long dossierId);
    
    Page<Allergie> findByTypeAllergie(TypeAllergie typeAllergie, Pageable pageable);
    
    Page<Allergie> findBySeverite(SeveriteAllergie severite, Pageable pageable);
    
    @Query("SELECT a FROM Allergie a WHERE a.dossierMedical = :dossier AND " +
           "(LOWER(a.allergene) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.symptomes) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Allergie> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                          @Param("keyword") String keyword, 
                                          Pageable pageable);
    
    @Query("SELECT a FROM Allergie a WHERE a.dossierMedical = :dossier AND a.active = true")
    List<Allergie> findActiveAllergiesByDossier(@Param("dossier") DossierMedical dossier);
    
    @Query("SELECT a FROM Allergie a WHERE a.severite IN ('SEVERE', 'ANAPHYLACTIQUE') AND a.active = true")
    List<Allergie> findSevereAllergies();
    
    @Query("SELECT a FROM Allergie a WHERE LOWER(a.allergene) LIKE LOWER(CONCAT('%', :allergene, '%')) AND a.active = true")
    List<Allergie> findByAllergeneContainingIgnoreCase(@Param("allergene") String allergene);
}
