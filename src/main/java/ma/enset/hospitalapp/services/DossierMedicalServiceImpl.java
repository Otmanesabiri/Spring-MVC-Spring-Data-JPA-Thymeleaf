package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.DossierMedicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Override
    public DossierMedical saveDossierMedical(DossierMedical dossierMedical) {
        return dossierMedicalRepository.save(dossierMedical);
    }

    @Override
    public DossierMedical getDossierMedical(Long id) {
        return dossierMedicalRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDossierMedical(Long id) {
        dossierMedicalRepository.deleteById(id);
    }

    @Override
    public Page<DossierMedical> findAllDossiersMedicaux(Pageable pageable) {
        return dossierMedicalRepository.findAll(pageable);
    }

    @Override
    public Page<DossierMedical> findDossiersByKeyword(String keyword, Pageable pageable) {
        return dossierMedicalRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Page<DossierMedical> findDossiersByStatut(StatutDossier statut, Pageable pageable) {
        return dossierMedicalRepository.findByStatut(statut, pageable);
    }

    @Override
    public DossierMedical findByPatient(Patient patient) {
        return dossierMedicalRepository.findByPatient(patient).orElse(null);
    }

    @Override
    public DossierMedical createDossierForPatient(Patient patient) {
        // Vérifier si un dossier existe déjà pour ce patient
        if (isDossierExistForPatient(patient)) {
            throw new RuntimeException("Un dossier médical existe déjà pour ce patient");
        }
        
        DossierMedical dossier = new DossierMedical();
        dossier.setPatient(patient);
        dossier.setNumeroDossier(generateNumeroDossier());
        dossier.setDateCreation(new Date());
        dossier.setStatut(StatutDossier.ACTIF);
        
        return saveDossierMedical(dossier);
    }

    @Override
    public DossierMedical findByNumeroDossier(String numeroDossier) {
        return dossierMedicalRepository.findByNumeroDossier(numeroDossier).orElse(null);
    }

    @Override
    public DossierMedical changerStatutDossier(Long id, StatutDossier nouveauStatut) {
        DossierMedical dossier = getDossierMedical(id);
        if (dossier != null) {
            dossier.setStatut(nouveauStatut);
            return saveDossierMedical(dossier);
        }
        return null;
    }

    @Override
    public DossierMedical archiverDossier(Long id) {
        return changerStatutDossier(id, StatutDossier.ARCHIVE);
    }

    @Override
    public DossierMedical activerDossier(Long id) {
        return changerStatutDossier(id, StatutDossier.ACTIF);
    }

    @Override
    public long countDossiersByStatut(StatutDossier statut) {
        return dossierMedicalRepository.countByStatut(statut);
    }

    @Override
    public List<DossierMedical> findDossiersCreatedBetween(Date dateDebut, Date dateFin) {
        return dossierMedicalRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    @Override
    public List<DossierMedical> findDossiersInactifs(Date dateLimit) {
        return dossierMedicalRepository.findDossiersInactifs(dateLimit);
    }

    @Override
    public String generateNumeroDossier() {
        String prefix = "DOS";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String numeroDossier;
        
        do {
            numeroDossier = prefix + timestamp.substring(timestamp.length() - 8);
            timestamp = String.valueOf(System.currentTimeMillis());
        } while (!isNumeroDossierUnique(numeroDossier));
        
        return numeroDossier;
    }

    @Override
    public void updateLastModificationDate(Long id) {
        DossierMedical dossier = getDossierMedical(id);
        if (dossier != null) {
            dossier.setDateDerniereModification(new Date());
            saveDossierMedical(dossier);
        }
    }

    @Override
    public boolean isDossierExistForPatient(Patient patient) {
        return dossierMedicalRepository.findByPatient(patient).isPresent();
    }

    @Override
    public boolean isNumeroDossierUnique(String numeroDossier) {
        return !dossierMedicalRepository.findByNumeroDossier(numeroDossier).isPresent();
    }
}
