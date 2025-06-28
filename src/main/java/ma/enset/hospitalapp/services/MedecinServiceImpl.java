package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.StatutMedecin;
import ma.enset.hospitalapp.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedecinServiceImpl implements MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    @Override
    public Medecin getMedecin(Long id) {
        return medecinRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Medecin> findAllMedecins(Pageable pageable) {
        return medecinRepository.findAll(pageable);
    }

    @Override
    public Page<Medecin> findMedecinsByNom(String nom, Pageable pageable) {
        return medecinRepository.findByNomContainingIgnoreCase(nom, pageable);
    }

    @Override
    public Page<Medecin> findMedecinsBySpecialite(String specialite, Pageable pageable) {
        return medecinRepository.findBySpecialiteContainingIgnoreCase(specialite, pageable);
    }

    @Override
    public Page<Medecin> findMedecinsByStatut(StatutMedecin statut, Pageable pageable) {
        return medecinRepository.findByStatut(statut, pageable);
    }

    @Override
    public Page<Medecin> findActiveMedecins(Pageable pageable) {
        return medecinRepository.findByActif(true, pageable);
    }

    @Override
    public Page<Medecin> findMedecinsByKeyword(String keyword, Pageable pageable) {
        return medecinRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Medecin saveMedecin(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    @Override
    public void deleteMedecin(Long id) {
        medecinRepository.deleteById(id);
    }

    @Override
    public void activerMedecin(Long id) {
        Medecin medecin = getMedecin(id);
        if (medecin != null) {
            medecin.setActif(true);
            medecinRepository.save(medecin);
        }
    }

    @Override
    public void desactiverMedecin(Long id) {
        Medecin medecin = getMedecin(id);
        if (medecin != null) {
            medecin.setActif(false);
            medecinRepository.save(medecin);
        }
    }

    @Override
    public List<Medecin> getMedecinsBySpecialite(String specialite) {
        return medecinRepository.findBySpecialite(specialite);
    }

    @Override
    public long countActiveMedecins() {
        return medecinRepository.countActiveMedecins();
    }

    @Override
    public List<Object[]> getStatistiquesBySpecialite() {
        return medecinRepository.countMedecinsBySpecialite();
    }
}
