package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Allergie;
import ma.enset.hospitalapp.repositories.AllergieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AllergieServiceImpl implements AllergieService {

    @Autowired
    private AllergieRepository allergieRepository;

    @Override
    public Page<Allergie> getAllAllergies(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return allergieRepository.findByAllergeneContainingIgnoreCase(keyword, pageable);
        }
        return allergieRepository.findAll(pageable);
    }

    @Override
    public Page<Allergie> getAllergiesByDossier(Long dossierId, Pageable pageable) {
        return allergieRepository.findByDossierMedicalId(dossierId, pageable);
    }

    @Override
    public Allergie getAllergieById(Long id) {
        return allergieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergie introuvable avec l'ID: " + id));
    }

    @Override
    public Allergie saveAllergie(Allergie allergie) {
        return allergieRepository.save(allergie);
    }

    @Override
    public void deleteAllergie(Long id) {
        allergieRepository.deleteById(id);
    }

    @Override
    public List<Allergie> getAllergiesActivesByDossier(Long dossierId) {
        return allergieRepository.findByDossierMedicalIdAndActiveTrue(dossierId);
    }
}
