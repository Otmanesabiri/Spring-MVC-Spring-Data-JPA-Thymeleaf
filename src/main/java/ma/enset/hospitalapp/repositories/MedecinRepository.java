package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.StatutMedecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    
    Page<Medecin> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    
    Page<Medecin> findBySpecialiteContainingIgnoreCase(String specialite, Pageable pageable);
    
    Page<Medecin> findByStatut(StatutMedecin statut, Pageable pageable);
    
    Page<Medecin> findByActif(boolean actif, Pageable pageable);
    
    List<Medecin> findBySpecialite(String specialite);
    
    @Query("SELECT m FROM Medecin m WHERE m.nom LIKE %:keyword% OR m.prenom LIKE %:keyword% OR m.specialite LIKE %:keyword%")
    Page<Medecin> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Medecin m WHERE m.actif = true")
    long countActiveMedecins();
    
    @Query("SELECT m.specialite, COUNT(m) FROM Medecin m WHERE m.actif = true GROUP BY m.specialite")
    List<Object[]> countMedecinsBySpecialite();
}
