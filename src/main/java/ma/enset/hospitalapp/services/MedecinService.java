package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.StatutMedecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedecinService {
    Medecin getMedecin(Long id);
    Page<Medecin> findAllMedecins(Pageable pageable);
    Page<Medecin> findMedecinsByNom(String nom, Pageable pageable);
    Page<Medecin> findMedecinsBySpecialite(String specialite, Pageable pageable);
    Page<Medecin> findMedecinsByStatut(StatutMedecin statut, Pageable pageable);
    Page<Medecin> findActiveMedecins(Pageable pageable);
    Page<Medecin> findMedecinsByKeyword(String keyword, Pageable pageable);
    Medecin saveMedecin(Medecin medecin);
    void deleteMedecin(Long id);
    void activerMedecin(Long id);
    void desactiverMedecin(Long id);
    List<Medecin> getMedecinsBySpecialite(String specialite);
    long countActiveMedecins();
    List<Object[]> getStatistiquesBySpecialite();
}
