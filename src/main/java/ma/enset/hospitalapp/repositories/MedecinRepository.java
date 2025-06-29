package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    
    Page<Medecin> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
    Page<Medecin> findBySpecialiteContainingIgnoreCase(String specialite, Pageable pageable);
    
    @Query("SELECT m FROM Medecin m WHERE " +
           "LOWER(m.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.specialite) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Medecin> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    long countByActifTrue();

    @Query("SELECT m.specialite, COUNT(m) FROM Medecin m GROUP BY m.specialite")
    List<Object[]> countBySpecialite();
}
