package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Technicien;
import ma.enset.hospitalapp.entities.TypeTechnicien;
import ma.enset.hospitalapp.entities.StatutEmploye;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {
    
    Page<Technicien> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    
    Page<Technicien> findByTypeTechnicien(TypeTechnicien typeTechnicien, Pageable pageable);
    
    Page<Technicien> findBySpecialiteContainingIgnoreCase(String specialite, Pageable pageable);
    
    Page<Technicien> findByStatut(StatutEmploye statut, Pageable pageable);
    
    Page<Technicien> findByActif(boolean actif, Pageable pageable);
    
    @Query("SELECT t FROM Technicien t WHERE t.nom LIKE %:keyword% OR t.prenom LIKE %:keyword% OR t.specialite LIKE %:keyword%")
    Page<Technicien> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Technicien t WHERE t.actif = true")
    long countActiveTechniciens();
    
    @Query("SELECT t.typeTechnicien, COUNT(t) FROM Technicien t WHERE t.actif = true GROUP BY t.typeTechnicien")
    List<Object[]> countTechniciensByType();
    
    // Techniciens avec certification bientôt expirée
    @Query("SELECT t FROM Technicien t WHERE t.dateExpirationCertification <= :dateLimit AND t.actif = true")
    List<Technicien> findWithExpiringCertification(@Param("dateLimit") Date dateLimit);
    
    List<Technicien> findByTypeTechnicienAndActif(TypeTechnicien typeTechnicien, boolean actif);
}
