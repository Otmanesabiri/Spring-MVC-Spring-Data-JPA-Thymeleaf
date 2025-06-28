package ma.enset.hospitalapp.entities;

public enum EquipeType {
    MATIN("Équipe Matin"),
    APRES_MIDI("Équipe Après-midi"),
    NUIT("Équipe Nuit"),
    ROTATION("Rotation");

    private final String libelle;

    EquipeType(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
