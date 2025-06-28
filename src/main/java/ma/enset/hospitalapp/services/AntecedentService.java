package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AntecedentService {
    
    // CRUD de base
    Antecedent saveAntecedent(Antecedent antecedent);
    Antecedent getAntecedent(Long id);
    void deleteAntecedent(Long id);
    
    // Recherche et pagination
    Page<Antecedent> findAllAntecedents(Pageable pageable);
    Page<Antecedent> findAntecedentsByKeyword(String keyword, Pageable pageable);
    Page<Antecedent> findAntecedentsByDossier(DossierMedical dossier, Pageable pageable);
    Page<Antecedent> findAntecedentsByType(TypeAntecedent type, Pageable pageable);
    Page<Antecedent> findAntecedentsBySeverite(SeveriteAntecedent severite, Pageable pageable);
    
    // Recherche spécialisée
    Page<Antecedent> findAntecedentsByDossierAndKeyword(DossierMedical dossier, String keyword, Pageable pageable);
    List<Antecedent> findActiveAntecedentsByDossier(DossierMedical dossier);
    List<Antecedent> findFamilialAntecedentsByDossier(DossierMedical dossier);
    List<Antecedent> findAntecedentsByDossierAndType(DossierMedical dossier, TypeAntecedent type);
    List<Antecedent> findSevereAntecedents();
    
    // Gestion des statuts
    Antecedent activerAntecedent(Long id);
    Antecedent desactiverAntecedent(Long id);
    
    // Méthodes utilitaires
    boolean hasActiveAntecedentOfType(DossierMedical dossier, TypeAntecedent type);
    long countAntecedentsByDossier(DossierMedical dossier);
}
