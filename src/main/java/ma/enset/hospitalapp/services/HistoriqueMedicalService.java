package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.HistoriqueMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HistoriqueMedicalService {
    Page<HistoriqueMedical> getAllHistoriques(String keyword, Pageable pageable);
    Page<HistoriqueMedical> getHistoriquesByDossier(Long dossierId, Pageable pageable);
    HistoriqueMedical getHistoriqueById(Long id);
    HistoriqueMedical saveHistorique(HistoriqueMedical historique);
    void deleteHistorique(Long id);
    List<HistoriqueMedical> getRecentHistoriquesByDossier(Long dossierId, int limit);
}
