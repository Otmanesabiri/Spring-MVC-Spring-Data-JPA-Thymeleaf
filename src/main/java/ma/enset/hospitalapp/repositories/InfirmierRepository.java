package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Infirmier;
import ma.enset.hospitalapp.entities.EquipeType;
import ma.enset.hospitalapp.entities.StatutEmploye;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfirmierRepository extends JpaRepository<Infirmier, Long> {
    
    Page<Infirmier> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    
    Page<Infirmier> findBySpecialiteContainingIgnoreCase(String specialite, Pageable pageable);
    
    Page<Infirmier> findByEquipe(EquipeType equipe, Pageable pageable);
    
    Page<Infirmier> findByStatut(StatutEmploye statut, Pageable pageable);
    
    Page<Infirmier> findByActif(boolean actif, Pageable pageable);
    
    Page<Infirmier> findByService(String service, Pageable pageable);
    
    @Query("SELECT i FROM Infirmier i WHERE i.nom LIKE %:keyword% OR i.prenom LIKE %:keyword% OR i.specialite LIKE %:keyword%")
    Page<Infirmier> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Infirmier i WHERE i.actif = true")
    long countActiveInfirmiers();
    
    @Query("SELECT i.equipe, COUNT(i) FROM Infirmier i WHERE i.actif = true GROUP BY i.equipe")
    List<Object[]> countInfirmiersByEquipe();
    
    List<Infirmier> findByEquipeAndActif(EquipeType equipe, boolean actif);
}
