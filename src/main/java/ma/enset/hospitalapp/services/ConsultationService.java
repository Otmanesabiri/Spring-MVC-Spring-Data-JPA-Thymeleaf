package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationService {
    
    // CRUD de base
    Consultation saveConsultation(Consultation consultation);
    Consultation getConsultation(Long id);
    Consultation getConsultationById(Long id); // Alias pour getConsultation
    void deleteConsultation(Long id);
    
    // Recherche et pagination
    Page<Consultation> findAllConsultations(Pageable pageable);
    Page<Consultation> findConsultationsByKeyword(String keyword, Pageable pageable);
    
    // Recherche par critères
    Page<Consultation> findByPatient(Patient patient, Pageable pageable);
    Page<Consultation> findByType(TypeConsultation type, Pageable pageable);
    Page<Consultation> findByStatut(StatutConsultation statut, Pageable pageable);
    Page<Consultation> findByNiveauUrgence(NiveauUrgence niveauUrgence, Pageable pageable);
    Page<Consultation> findByMedecin(String medecin, Pageable pageable);
    
    // Recherche combinée avec filtres
    Page<Consultation> findConsultationsFiltered(
        Patient patient, TypeConsultation type, StatutConsultation statut,
        NiveauUrgence niveauUrgence, String medecin, 
        LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable
    );
    
    // Gestion des statuts
    Consultation changerStatutConsultation(Long id, StatutConsultation nouveauStatut);
    Consultation marquerCommeTerminee(Long id);
    Consultation annulerConsultation(Long id, String motif);
    Consultation reporterConsultation(Long id, LocalDateTime nouvelleDate);
    
    // Gestion spécialisée
    Consultation creerConsultationUrgence(Patient patient, String medecin, NiveauUrgence niveauUrgence, String motif);

    // Consultations spéciales
    List<Consultation> getConsultationsDuJour();
    Page<Consultation> getConsultationsDuJour(Pageable pageable);
    List<Consultation> getUrgencesEnAttente();
    List<Consultation> getConsultationsEnRetard();
    List<Consultation> getHospitalisationsActives();
    List<Consultation> getConsultationsTerminees(); // Ajout de la méthode manquante

    // Historique patient
    List<Consultation> getHistoriqueConsultations(Patient patient);
    Page<Consultation> getHistoriqueConsultations(Patient patient, Pageable pageable);
    
    // Lien avec dossier médical
    Page<Consultation> getConsultationsByDossier(DossierMedical dossierMedical, Pageable pageable);
    Consultation lierAuDossierMedical(Long consultationId, Long dossierId);
    
    // Statistiques
    long countByStatut(StatutConsultation statut);
    long countByType(TypeConsultation type);
    long countByNiveauUrgence(NiveauUrgence niveauUrgence);
    
    // Dashboard
    ConsultationDashboardStats getDashboardStats();
    List<Consultation> getConsultationsRecentes(int limit);
    List<Consultation> getUrgencesPrioritaires();
}
