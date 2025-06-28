package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Antecedent;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.TypeAntecedent;
import ma.enset.hospitalapp.entities.SeveriteAntecedent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AntecedentRepository extends JpaRepository<Antecedent, Long> {
    
    Page<Antecedent> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<Antecedent> findByTypeAntecedent(TypeAntecedent typeAntecedent, Pageable pageable);
    
    Page<Antecedent> findBySeverite(SeveriteAntecedent severite, Pageable pageable);
    
    @Query("SELECT a FROM Antecedent a WHERE a.dossierMedical = :dossier AND " +
           "(LOWER(a.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Antecedent> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                            @Param("keyword") String keyword, 
                                            Pageable pageable);
    
    @Query("SELECT a FROM Antecedent a WHERE a.dossierMedical = :dossier AND a.actuel = true")
    List<Antecedent> findActiveAntecedentsByDossier(@Param("dossier") DossierMedical dossier);
    
    @Query("SELECT a FROM Antecedent a WHERE a.dossierMedical = :dossier AND a.typeAntecedent = 'FAMILIAL'")
    List<Antecedent> findFamilialAntecedentsByDossier(@Param("dossier") DossierMedical dossier);
    
    @Query("SELECT a FROM Antecedent a WHERE a.dossierMedical = :dossier AND a.typeAntecedent = :type")
    List<Antecedent> findByDossierAndType(@Param("dossier") DossierMedical dossier, 
                                         @Param("type") TypeAntecedent type);
    
    @Query("SELECT a FROM Antecedent a WHERE a.severite IN ('SEVERE', 'CRITIQUE') AND a.actuel = true")
    List<Antecedent> findSevereAntecedents();
}
