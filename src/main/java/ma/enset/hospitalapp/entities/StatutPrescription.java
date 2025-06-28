package ma.enset.hospitalapp.entities;

public enum StatutPrescription {
    ACTIVE("Active"),
    TERMINEE("Terminée"),
    ANNULEE("Annulée"),
    SUSPENDUE("Suspendue"),
    EXPIREE("Expirée");

    private final String libelle;

    StatutPrescription(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
