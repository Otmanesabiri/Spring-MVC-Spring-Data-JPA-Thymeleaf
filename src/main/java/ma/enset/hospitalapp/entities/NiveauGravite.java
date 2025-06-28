package ma.enset.hospitalapp.entities;

public enum NiveauGravite {
    NORMAL("Normal"),
    ATTENTION("Attention"),
    URGENT("Urgent"),
    CRITIQUE("Critique");

    private final String libelle;

    NiveauGravite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
