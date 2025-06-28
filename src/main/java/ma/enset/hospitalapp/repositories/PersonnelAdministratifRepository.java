package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.PersonnelAdministratif;
import ma.enset.hospitalapp.entities.StatutEmploye;
import ma.enset.hospitalapp.entities.NiveauAcces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelAdministratifRepository extends JpaRepository<PersonnelAdministratif, Long> {
    
    Page<PersonnelAdministratif> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    
    Page<PersonnelAdministratif> findByPosteContainingIgnoreCase(String poste, Pageable pageable);
    
    Page<PersonnelAdministratif> findByDepartement(String departement, Pageable pageable);
    
    Page<PersonnelAdministratif> findByStatut(StatutEmploye statut, Pageable pageable);
    
    Page<PersonnelAdministratif> findByActif(boolean actif, Pageable pageable);
    
    Page<PersonnelAdministratif> findByNiveauAcces(NiveauAcces niveauAcces, Pageable pageable);
    
    @Query("SELECT p FROM PersonnelAdministratif p WHERE p.nom LIKE %:keyword% OR p.prenom LIKE %:keyword% OR p.poste LIKE %:keyword%")
    Page<PersonnelAdministratif> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM PersonnelAdministratif p WHERE p.actif = true")
    long countActivePersonnel();
    
    @Query("SELECT p.departement, COUNT(p) FROM PersonnelAdministratif p WHERE p.actif = true GROUP BY p.departement")
    List<Object[]> countPersonnelByDepartement();
}
