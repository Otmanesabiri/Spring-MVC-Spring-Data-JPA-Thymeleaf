package ma.enset.hospitalapp.entities;

public enum StatutConsultation {
    PLANIFIEE("Planifiée"),
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    ANNULEE("Annulée"),
    REPORTEE("Reportée");

    private final String libelle;

    StatutConsultation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
