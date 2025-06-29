package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Medecin> getAllMedecins() {
        return medecinRepository.findAll();
    }

    @Override
    public Page<Medecin> findAllMedecins(Pageable pageable) {
        return medecinRepository.findAll(pageable);
    }

    @Override
    public Page<Medecin> findMedecinsByName(String name, Pageable pageable) {
        return medecinRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(name, name, pageable);
    }

    @Override
    public Page<Medecin> findMedecinsBySpecialite(String specialite, Pageable pageable) {
        return medecinRepository.findBySpecialiteContainingIgnoreCase(specialite, pageable);
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
    public Medecin activerMedecin(Long id) {
        Medecin medecin = getMedecin(id);
        if (medecin != null) {
            medecin.setActif(true);
            return medecinRepository.save(medecin);
        }
        return null;
    }

    @Override
    public Medecin desactiverMedecin(Long id) {
        Medecin medecin = getMedecin(id);
        if (medecin != null) {
            medecin.setActif(false);
            return medecinRepository.save(medecin);
        }
        return null;
    }

    @Override
    public long countActiveMedecins() {
        return medecinRepository.countByActifTrue();
    }

    @Override
    public Map<String, Long> getStatistiquesBySpecialite() {
        List<Object[]> results = medecinRepository.countBySpecialite();
        return results.stream()
                .collect(Collectors.toMap(
                    result -> (String) result[0],
                    result -> (Long) result[1]
                ));
    }
}
