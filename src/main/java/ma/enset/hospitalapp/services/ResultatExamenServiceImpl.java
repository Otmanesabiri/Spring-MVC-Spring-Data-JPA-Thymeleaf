package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.ResultatExamen;
import ma.enset.hospitalapp.repositories.ResultatExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResultatExamenServiceImpl implements ResultatExamenService {

    @Autowired
    private ResultatExamenRepository resultatExamenRepository;

    @Override
    public Page<ResultatExamen> getAllResultats(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return resultatExamenRepository.findByNomExamenContainingIgnoreCase(keyword, pageable);
        }
        return resultatExamenRepository.findAll(pageable);
    }

    @Override
    public Page<ResultatExamen> getResultatsByDossier(Long dossierId, Pageable pageable) {
        return resultatExamenRepository.findByDossierMedicalId(dossierId, pageable);
    }

    @Override
    public ResultatExamen getResultatById(Long id) {
        return resultatExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Résultat d'examen introuvable avec l'ID: " + id));
    }

    @Override
    public ResultatExamen saveResultat(ResultatExamen resultat) {
        return resultatExamenRepository.save(resultat);
    }

    @Override
    public void deleteResultat(Long id) {
        resultatExamenRepository.deleteById(id);
    }

    @Override
    public List<ResultatExamen> getRecentResultatsByDossier(Long dossierId, int limit) {
        return resultatExamenRepository.findTop5ByDossierMedicalIdOrderByDateExamenDesc(dossierId);
    }
}
