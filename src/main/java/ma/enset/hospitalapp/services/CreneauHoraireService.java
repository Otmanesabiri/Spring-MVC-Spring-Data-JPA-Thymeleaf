package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.CreneauHoraire;
import ma.enset.hospitalapp.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.List;

public interface CreneauHoraireService {
    
    CreneauHoraire saveCreneauHoraire(CreneauHoraire creneauHoraire);
    
    Page<CreneauHoraire> findAllCreneauxHoraires(Pageable pageable);
    
    Page<CreneauHoraire> findCreneauxHorairesByKeyword(String keyword, Pageable pageable);
    
    CreneauHoraire getCreneauHoraire(Long id);
    
    void deleteCreneauHoraire(Long id);
    
    List<CreneauHoraire> getCreneauxByMedecin(Medecin medecin);
    
    List<CreneauHoraire> getCreneauxActifsByMedecin(Medecin medecin);
    
    List<CreneauHoraire> getCreneauxByMedecinAndJour(Medecin medecin, DayOfWeek jour);
    
    List<CreneauHoraire> getCreneauxByJour(DayOfWeek jour);
    
    CreneauHoraire activerCreneauHoraire(Long id);
    
    CreneauHoraire desactiverCreneauHoraire(Long id);
}
