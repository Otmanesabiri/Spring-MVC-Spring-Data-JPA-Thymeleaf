package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MedecinService {
    Medecin saveMedecin(Medecin medecin);
    Medecin getMedecin(Long id);
    List<Medecin> getAllMedecins();
    Page<Medecin> findAllMedecins(Pageable pageable);
    Page<Medecin> findMedecinsByName(String name, Pageable pageable);
    Page<Medecin> findMedecinsByKeyword(String keyword, Pageable pageable);
    Page<Medecin> findMedecinsBySpecialite(String specialite, Pageable pageable);
    void deleteMedecin(Long id);
    Medecin activerMedecin(Long id);
    Medecin desactiverMedecin(Long id);
    long countActiveMedecins();
    Map<String, Long> getStatistiquesBySpecialite();
}
