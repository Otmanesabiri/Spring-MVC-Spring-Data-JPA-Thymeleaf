package ma.enset.hospitalapp.entities;

public enum HoraireTravail {
    TEMPS_PLEIN("Temps plein"),
    TEMPS_PARTIEL("Temps partiel"),
    GARDE("Garde"),
    NUIT("Nuit uniquement");

    private final String libelle;

    HoraireTravail(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
