package ma.enset.hospitalapp.entities;

public enum StatutDossier {
    ACTIF("Actif"),
    ARCHIVE("Archivé"),
    SUSPENDU("Suspendu"),
    EN_TRANSFERT("En transfert");

    private final String libelle;

    StatutDossier(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
