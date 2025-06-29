package ma.enset.hospitalapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class ConsultationDashboardStats {
    
    // Statistiques générales
    private long totalConsultations;
    private long consultationsDuJour;
    private long consultationsEnCours;
    private long consultationsTerminees;
    
    // Urgences
    private long urgencesEnAttente;
    private long urgencesCritiques;
    private long consultationsEnRetard;
    
    // Hospitalisations
    private long hospitalisationsActives;
    private long nouvellesHospitalisations;
    
    // Consultations par type
    private long consultationsExternes;
    private long suiviPostConsultation;
    private long teleconsultations;
    
    // Répartition par statut
    private Map<StatutConsultation, Long> repartitionParStatut;
    
    // Répartition par type
    private Map<TypeConsultation, Long> repartitionParType;
    
    // Répartition par niveau d'urgence
    private Map<NiveauUrgence, Long> repartitionParUrgence;
    
    // Taux et pourcentages
    private double tauxOccupation;
    private double tempsAttenteMoyen; // en minutes
    private double satisfactionMoyenne;
    
    // Revenus (si applicable)
    private double revenusDuJour;
    private double revenusNonEncaisses;
    
    // Méthodes utilitaires
    public double getPourcentageUrgences() {
        if (totalConsultations == 0) return 0;
        return (double) urgencesEnAttente / totalConsultations * 100;
    }
    
    public double getPourcentageConsultationsTerminees() {
        if (totalConsultations == 0) return 0;
        return (double) consultationsTerminees / totalConsultations * 100;
    }
    
    public boolean hasUrgencesCritiques() {
        return urgencesCritiques > 0;
    }
    
    public boolean hasConsultationsEnRetard() {
        return consultationsEnRetard > 0;
    }
    
    public String getNiveauActivite() {
        if (tauxOccupation > 80) return "ELEVE";
        if (tauxOccupation > 60) return "MOYEN";
        if (tauxOccupation > 30) return "FAIBLE";
        return "TRES_FAIBLE";
    }
    
    public String getCouleurNiveauActivite() {
        String niveau = getNiveauActivite();
        switch (niveau) {
            case "ELEVE": return "#dc3545";
            case "MOYEN": return "#ffc107";
            case "FAIBLE": return "#28a745";
            default: return "#6c757d";
        }
    }
}
