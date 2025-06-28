package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Allergie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AllergieService {
    Page<Allergie> getAllAllergies(String keyword, Pageable pageable);
    Page<Allergie> getAllergiesByDossier(Long dossierId, Pageable pageable);
    Allergie getAllergieById(Long id);
    Allergie saveAllergie(Allergie allergie);
    void deleteAllergie(Long id);
    List<Allergie> getAllergiesActivesByDossier(Long dossierId);
}
