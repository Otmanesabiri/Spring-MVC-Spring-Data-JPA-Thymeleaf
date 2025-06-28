package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.CreneauHoraire;
import ma.enset.hospitalapp.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface CreneauHoraireRepository extends JpaRepository<CreneauHoraire, Long> {
    
    Page<CreneauHoraire> findByMedecinNomContainsIgnoreCaseOrMedecinPrenomContainsIgnoreCase(
            String nom, String prenom, Pageable pageable);
    
    List<CreneauHoraire> findByMedecin(Medecin medecin);
    
    List<CreneauHoraire> findByMedecinAndActif(Medecin medecin, boolean actif);
    
    List<CreneauHoraire> findByMedecinAndJourSemaine(Medecin medecin, DayOfWeek jourSemaine);
    
    List<CreneauHoraire> findByJourSemaine(DayOfWeek jourSemaine);
    
    List<CreneauHoraire> findByActif(boolean actif);
}
