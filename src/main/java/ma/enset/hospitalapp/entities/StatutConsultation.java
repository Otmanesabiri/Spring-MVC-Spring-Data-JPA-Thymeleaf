package ma.enset.hospitalapp.entities;

public enum StatutConsultation {
    PROGRAMMEE("Programmée", "#17a2b8"),
    CONFIRMEE("Confirmée", "#28a745"),
    EN_ATTENTE("En Attente", "#ffc107"),
    EN_COURS("En Cours", "#fd7e14"),
    TERMINEE("Terminée", "#28a745"),
    ANNULEE("Annulée", "#dc3545"),
    REPORTEE("Reportée", "#6c757d"),
    NO_SHOW("Patient Absent", "#dc3545"),
    HOSPITALISE("Hospitalisé", "#6f42c1"),
    TRANSFERE("Transféré", "#20c997");

    private final String libelle;
    private final String couleur;

    StatutConsultation(String libelle, String couleur) {
        this.libelle = libelle;
        this.couleur = couleur;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public boolean isActif() {
        return this == PROGRAMMEE || this == CONFIRMEE || this == EN_ATTENTE || this == EN_COURS;
    }

    public boolean isTermine() {
        return this == TERMINEE || this == ANNULEE || this == NO_SHOW;
    }

    public boolean necessiteAction() {
        return this == EN_ATTENTE || this == EN_COURS;
    }
}
