package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RendezVousServiceImpl implements RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Override
    public RendezVous saveRendezVous(RendezVous rendezVous) {
        return rendezVousRepository.save(rendezVous);
    }

    @Override
    public Page<RendezVous> findAllRendezVous(Pageable pageable) {
        return rendezVousRepository.findAll(pageable);
    }

    @Override
    public Page<RendezVous> findRendezVousByKeyword(String keyword, Pageable pageable) {
        return rendezVousRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Page<RendezVous> findRendezVousByStatut(StatutRendezVous statut, Pageable pageable) {
        return rendezVousRepository.findByStatut(statut, pageable);
    }

    @Override
    public Page<RendezVous> findRendezVousByMedecin(Medecin medecin, Pageable pageable) {
        return rendezVousRepository.findByMedecin(medecin, pageable);
    }

    @Override
    public Page<RendezVous> findRendezVousByPatient(Patient patient, Pageable pageable) {
        return rendezVousRepository.findByPatient(patient, pageable);
    }

    @Override
    public RendezVous getRendezVous(Long id) {
        return rendezVousRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRendezVous(Long id) {
        rendezVousRepository.deleteById(id);
    }

    @Override
    public List<RendezVous> getRendezVousByDate(Date date) {
        return rendezVousRepository.findByDateRendezVous(date);
    }

    @Override
    public List<RendezVous> getRendezVousByMedecinAndPeriode(Medecin medecin, Date dateDebut, Date dateFin) {
        return rendezVousRepository.findByMedecinAndDateHeureRendezVousBetweenAndStatutNotIn(medecin, dateDebut, dateFin);
    }

    @Override
    public boolean isCreneauDisponible(Medecin medecin, Date dateHeure, int dureeMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateHeure);
        cal.add(Calendar.MINUTE, dureeMinutes);
        Date dateFin = cal.getTime();
        
        List<RendezVous> rendezVousExistants = rendezVousRepository.findByMedecinAndDateHeureRendezVousBetweenAndStatutNotIn(
                medecin, dateHeure, dateFin);
        
        return rendezVousExistants.isEmpty();
    }

    @Override
    public List<Date> getCreneauxDisponibles(Medecin medecin, Date date, int dureeMinutes) {
        List<Date> creneauxDisponibles = new ArrayList<>();
        
        // Générer les créneaux de 8h à 18h avec un pas de 30 minutes
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Calendar finJournee = Calendar.getInstance();
        finJournee.setTime(date);
        finJournee.set(Calendar.HOUR_OF_DAY, 18);
        finJournee.set(Calendar.MINUTE, 0);
        
        while (cal.getTime().before(finJournee.getTime())) {
            Date creneau = cal.getTime();
            if (isCreneauDisponible(medecin, creneau, dureeMinutes)) {
                creneauxDisponibles.add(creneau);
            }
            cal.add(Calendar.MINUTE, 30);
        }
        
        return creneauxDisponibles;
    }

    @Override
    public RendezVous confirmerRendezVous(Long id) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.CONFIRME);
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public RendezVous commencerRendezVous(Long id) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.EN_COURS);
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public RendezVous terminerRendezVous(Long id) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.TERMINE);
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public RendezVous annulerRendezVous(Long id, String raison) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.ANNULE);
            rendezVous.setRaisonAnnulation(raison);
            rendezVous.setDateAnnulation(new Date());
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public RendezVous reporterRendezVous(Long id, Date nouvelleDate, String raison) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.REPORTE);
            rendezVous.setDateHeureRendezVous(nouvelleDate);
            rendezVous.setRaisonAnnulation(raison);
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public RendezVous marquerNonPresente(Long id) {
        RendezVous rendezVous = getRendezVous(id);
        if (rendezVous != null) {
            rendezVous.setStatut(StatutRendezVous.NON_PRESENTE);
            return saveRendezVous(rendezVous);
        }
        return null;
    }

    @Override
    public List<RendezVous> getRendezVousPourRappel() {
        return rendezVousRepository.findRendezVousPourRappel(new Date());
    }

    @Override
    public void envoyerRappel(Long rendezVousId) {
        RendezVous rendezVous = getRendezVous(rendezVousId);
        if (rendezVous != null) {
            // Simulation de l'envoi de rappel (SMS, email, etc.)
            rendezVous.setRappelEnvoye(true);
            rendezVous.setDateRappel(new Date());
            saveRendezVous(rendezVous);
        }
    }

    @Override
    public void planifierRappels() {
        List<RendezVous> rendezVousPourRappel = getRendezVousPourRappel();
        for (RendezVous rendezVous : rendezVousPourRappel) {
            // Vérifier si le rendez-vous est dans les 24 heures
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, 24);
            
            if (rendezVous.getDateHeureRendezVous().before(cal.getTime())) {
                envoyerRappel(rendezVous.getId());
            }
        }
    }
}
