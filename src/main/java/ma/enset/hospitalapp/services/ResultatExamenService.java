package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.ResultatExamen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResultatExamenService {
    Page<ResultatExamen> getAllResultats(String keyword, Pageable pageable);
    Page<ResultatExamen> getResultatsByDossier(Long dossierId, Pageable pageable);
    ResultatExamen getResultatById(Long id);
    ResultatExamen saveResultat(ResultatExamen resultat);
    void deleteResultat(Long id);
    List<ResultatExamen> getRecentResultatsByDossier(Long dossierId, int limit);
}
