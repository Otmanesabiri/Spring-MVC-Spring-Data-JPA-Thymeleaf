package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface DossierMedicalService {
    
    // CRUD de base
    DossierMedical saveDossierMedical(DossierMedical dossierMedical);
    DossierMedical getDossierMedical(Long id);
    void deleteDossierMedical(Long id);
    
    // Recherche et pagination
    Page<DossierMedical> findAllDossiersMedicaux(Pageable pageable);
    Page<DossierMedical> findDossiersByKeyword(String keyword, Pageable pageable);
    Page<DossierMedical> findDossiersByStatut(StatutDossier statut, Pageable pageable);
    
    // Gestion par patient
    DossierMedical findByPatient(Patient patient);
    DossierMedical createDossierForPatient(Patient patient);
    
    // Gestion par numéro de dossier
    DossierMedical findByNumeroDossier(String numeroDossier);
    
    // Gestion des statuts
    DossierMedical changerStatutDossier(Long id, StatutDossier nouveauStatut);
    DossierMedical archiverDossier(Long id);
    DossierMedical activerDossier(Long id);
    
    // Statistiques et rapports
    long countDossiersByStatut(StatutDossier statut);
    List<DossierMedical> findDossiersCreatedBetween(Date dateDebut, Date dateFin);
    List<DossierMedical> findDossiersInactifs(Date dateLimit);
    
    // Méthodes utilitaires
    String generateNumeroDossier();
    void updateLastModificationDate(Long id);
    
    // Validation
    boolean isDossierExistForPatient(Patient patient);
    boolean isNumeroDossierUnique(String numeroDossier);
}
