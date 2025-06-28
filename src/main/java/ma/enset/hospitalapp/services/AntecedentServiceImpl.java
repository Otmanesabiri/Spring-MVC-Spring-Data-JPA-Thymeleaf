package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.AntecedentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AntecedentServiceImpl implements AntecedentService {

    @Autowired
    private AntecedentRepository antecedentRepository;

    @Override
    public Antecedent saveAntecedent(Antecedent antecedent) {
        return antecedentRepository.save(antecedent);
    }

    @Override
    public Antecedent getAntecedent(Long id) {
        return antecedentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAntecedent(Long id) {
        antecedentRepository.deleteById(id);
    }

    @Override
    public Page<Antecedent> findAllAntecedents(Pageable pageable) {
        return antecedentRepository.findAll(pageable);
    }

    @Override
    public Page<Antecedent> findAntecedentsByKeyword(String keyword, Pageable pageable) {
        // Cette méthode nécessiterait une modification du repository pour une recherche globale
        return antecedentRepository.findAll(pageable);
    }

    @Override
    public Page<Antecedent> findAntecedentsByDossier(DossierMedical dossier, Pageable pageable) {
        return antecedentRepository.findByDossierMedical(dossier, pageable);
    }

    @Override
    public Page<Antecedent> findAntecedentsByType(TypeAntecedent type, Pageable pageable) {
        return antecedentRepository.findByTypeAntecedent(type, pageable);
    }

    @Override
    public Page<Antecedent> findAntecedentsBySeverite(SeveriteAntecedent severite, Pageable pageable) {
        return antecedentRepository.findBySeverite(severite, pageable);
    }

    @Override
    public Page<Antecedent> findAntecedentsByDossierAndKeyword(DossierMedical dossier, String keyword, Pageable pageable) {
        return antecedentRepository.findByDossierAndKeyword(dossier, keyword, pageable);
    }

    @Override
    public List<Antecedent> findActiveAntecedentsByDossier(DossierMedical dossier) {
        return antecedentRepository.findActiveAntecedentsByDossier(dossier);
    }

    @Override
    public List<Antecedent> findFamilialAntecedentsByDossier(DossierMedical dossier) {
        return antecedentRepository.findFamilialAntecedentsByDossier(dossier);
    }

    @Override
    public List<Antecedent> findAntecedentsByDossierAndType(DossierMedical dossier, TypeAntecedent type) {
        return antecedentRepository.findByDossierAndType(dossier, type);
    }

    @Override
    public List<Antecedent> findSevereAntecedents() {
        return antecedentRepository.findSevereAntecedents();
    }

    @Override
    public Antecedent activerAntecedent(Long id) {
        Antecedent antecedent = getAntecedent(id);
        if (antecedent != null) {
            antecedent.setActuel(true);
            return saveAntecedent(antecedent);
        }
        return null;
    }

    @Override
    public Antecedent desactiverAntecedent(Long id) {
        Antecedent antecedent = getAntecedent(id);
        if (antecedent != null) {
            antecedent.setActuel(false);
            return saveAntecedent(antecedent);
        }
        return null;
    }

    @Override
    public boolean hasActiveAntecedentOfType(DossierMedical dossier, TypeAntecedent type) {
        List<Antecedent> antecedents = findAntecedentsByDossierAndType(dossier, type);
        return antecedents.stream().anyMatch(Antecedent::isActuel);
    }

    @Override
    public long countAntecedentsByDossier(DossierMedical dossier) {
        return findActiveAntecedentsByDossier(dossier).size();
    }
}
