package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface RendezVousService {
    
    RendezVous saveRendezVous(RendezVous rendezVous);
    
    Page<RendezVous> findAllRendezVous(Pageable pageable);
    
    Page<RendezVous> findRendezVousByKeyword(String keyword, Pageable pageable);
    
    Page<RendezVous> findRendezVousByStatut(StatutRendezVous statut, Pageable pageable);
    
    Page<RendezVous> findRendezVousByMedecin(Medecin medecin, Pageable pageable);
    
    Page<RendezVous> findRendezVousByPatient(Patient patient, Pageable pageable);
    
    RendezVous getRendezVous(Long id);
    
    void deleteRendezVous(Long id);
    
    // Gestion des créneaux et disponibilités
    List<RendezVous> getRendezVousByDate(Date date);
    
    List<RendezVous> getRendezVousByMedecinAndPeriode(Medecin medecin, Date dateDebut, Date dateFin);
    
    boolean isCreneauDisponible(Medecin medecin, Date dateHeure, int dureeMinutes);
    
    List<Date> getCreneauxDisponibles(Medecin medecin, Date date, int dureeMinutes);
    
    // Gestion des statuts
    RendezVous confirmerRendezVous(Long id);
    
    RendezVous commencerRendezVous(Long id);
    
    RendezVous terminerRendezVous(Long id);
    
    RendezVous annulerRendezVous(Long id, String raison);
    
    RendezVous reporterRendezVous(Long id, Date nouvelleDate, String raison);
    
    RendezVous marquerNonPresente(Long id);
    
    // Gestion des rappels
    List<RendezVous> getRendezVousPourRappel();
    
    void envoyerRappel(Long rendezVousId);
    
    void planifierRappels();
    
    // Renvoie la liste des prochains rendez-vous (date > maintenant, statut non annulé)
    List<RendezVous> getProchainsRendezVous();
}
