package ma.enset.hospitalapp.entities;

public enum NiveauConfidentialite {
    PUBLIC("Public"),
    NORMAL("Normal"),
    CONFIDENTIEL("Confidentiel"),
    SECRET("Secret"),
    TOP_SECRET("Top Secret");

    private final String libelle;

    NiveauConfidentialite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
