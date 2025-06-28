package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.HistoriqueMedical;
import ma.enset.hospitalapp.repositories.HistoriqueMedicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HistoriqueMedicalServiceImpl implements HistoriqueMedicalService {

    @Autowired
    private HistoriqueMedicalRepository historiqueMedicalRepository;

    @Override
    public Page<HistoriqueMedical> getAllHistoriques(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return historiqueMedicalRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
        }
        return historiqueMedicalRepository.findAll(pageable);
    }

    @Override
    public Page<HistoriqueMedical> getHistoriquesByDossier(Long dossierId, Pageable pageable) {
        return historiqueMedicalRepository.findByDossierMedicalId(dossierId, pageable);
    }

    @Override
    public HistoriqueMedical getHistoriqueById(Long id) {
        return historiqueMedicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historique médical introuvable avec l'ID: " + id));
    }

    @Override
    public HistoriqueMedical saveHistorique(HistoriqueMedical historique) {
        return historiqueMedicalRepository.save(historique);
    }

    @Override
    public void deleteHistorique(Long id) {
        historiqueMedicalRepository.deleteById(id);
    }

    @Override
    public List<HistoriqueMedical> getRecentHistoriquesByDossier(Long dossierId, int limit) {
        return historiqueMedicalRepository.findTop5ByDossierMedicalIdOrderByDateEvenementDesc(dossierId);
    }
}
