package ma.enset.hospitalapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class SuiviDashboardStats {
    
    // Statistiques générales
    private long totalSuivis;
    private long suivisDuJour;
    private long suivisEnCours;
    private long suivisTermines;
    
    // Suivis par statut
    private long suivisAmelioration;
    private long suivisStables;
    private long suivisDeterioration;
    private long suivisGueris;
    
    // Suivis par type
    private long suivisRoutine;
    private long suivisUrgents;
    private long suivisPostOperatoires;
    private long reevaluations;
    
    // Alertes et priorités
    private long suivisNecessitantAttention;
    private long suivisAvecProchainRdv;
    private long suivisEnRetard;
    
    // Répartitions
    private Map<StatutSuivi, Long> repartitionParStatut;
    private Map<TypeSuivi, Long> repartitionParType;
    
    // Moyennes et tendances
    private double tempsReponseUrgencesMoyen; // en minutes
    private double tauxAmelioration; // en pourcentage
    private double tauxDeterioration; // en pourcentage
    
    // Personnel
    private Map<String, Long> suivisParPersonnel;
    private String personnelLePlusActif;
    
    // Méthodes utilitaires
    public double getPourcentageAmelioration() {
        if (totalSuivis == 0) return 0;
        return (double) suivisAmelioration / totalSuivis * 100;
    }
    
    public double getPourcentageDeterioration() {
        if (totalSuivis == 0) return 0;
        return (double) suivisDeterioration / totalSuivis * 100;
    }
    
    public boolean hasAlertes() {
        return suivisNecessitantAttention > 0 || suivisEnRetard > 0;
    }
    
    public String getNiveauQualiteSoins() {
        double pourcentageAmelioration = getPourcentageAmelioration();
        double pourcentageDeterioration = getPourcentageDeterioration();
        
        if (pourcentageAmelioration > 70 && pourcentageDeterioration < 10) {
            return "EXCELLENT";
        } else if (pourcentageAmelioration > 50 && pourcentageDeterioration < 20) {
            return "BON";
        } else if (pourcentageAmelioration > 30 && pourcentageDeterioration < 30) {
            return "MOYEN";
        } else {
            return "A_AMELIORER";
        }
    }
    
    public String getCouleurQualiteSoins() {
        String niveau = getNiveauQualiteSoins();
        switch (niveau) {
            case "EXCELLENT": return "#28a745";
            case "BON": return "#17a2b8";
            case "MOYEN": return "#ffc107";
            default: return "#dc3545";
        }
    }
    
    public boolean isTendancePositive() {
        return tauxAmelioration > tauxDeterioration;
    }
    
    public long getSuivisActifs() {
        return suivisEnCours + suivisAmelioration + suivisStables;
    }
}
