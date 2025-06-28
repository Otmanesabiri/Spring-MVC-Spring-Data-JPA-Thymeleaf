package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.CreneauHoraire;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.repositories.CreneauHoraireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@Transactional
public class CreneauHoraireServiceImpl implements CreneauHoraireService {

    @Autowired
    private CreneauHoraireRepository creneauHoraireRepository;

    @Override
    public CreneauHoraire saveCreneauHoraire(CreneauHoraire creneauHoraire) {
        return creneauHoraireRepository.save(creneauHoraire);
    }

    @Override
    public Page<CreneauHoraire> findAllCreneauxHoraires(Pageable pageable) {
        return creneauHoraireRepository.findAll(pageable);
    }

    @Override
    public Page<CreneauHoraire> findCreneauxHorairesByKeyword(String keyword, Pageable pageable) {
        return creneauHoraireRepository.findByMedecinNomContainsIgnoreCaseOrMedecinPrenomContainsIgnoreCase(
                keyword, keyword, pageable);
    }

    @Override
    public CreneauHoraire getCreneauHoraire(Long id) {
        return creneauHoraireRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCreneauHoraire(Long id) {
        creneauHoraireRepository.deleteById(id);
    }

    @Override
    public List<CreneauHoraire> getCreneauxByMedecin(Medecin medecin) {
        return creneauHoraireRepository.findByMedecin(medecin);
    }

    @Override
    public List<CreneauHoraire> getCreneauxActifsByMedecin(Medecin medecin) {
        return creneauHoraireRepository.findByMedecinAndActif(medecin, true);
    }

    @Override
    public List<CreneauHoraire> getCreneauxByMedecinAndJour(Medecin medecin, DayOfWeek jour) {
        return creneauHoraireRepository.findByMedecinAndJourSemaine(medecin, jour);
    }

    @Override
    public List<CreneauHoraire> getCreneauxByJour(DayOfWeek jour) {
        return creneauHoraireRepository.findByJourSemaine(jour);
    }

    @Override
    public CreneauHoraire activerCreneauHoraire(Long id) {
        CreneauHoraire creneau = getCreneauHoraire(id);
        if (creneau != null) {
            creneau.setActif(true);
            return saveCreneauHoraire(creneau);
        }
        return null;
    }

    @Override
    public CreneauHoraire desactiverCreneauHoraire(Long id) {
        CreneauHoraire creneau = getCreneauHoraire(id);
        if (creneau != null) {
            creneau.setActif(false);
            return saveCreneauHoraire(creneau);
        }
        return null;
    }
}
