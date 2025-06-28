package ma.enset.hospitalapp.entities;

public enum SeveriteAntecedent {
    NORMAL("Normal"),
    LEGER("Léger"),
    MODERE("Modéré"),
    SEVERE("Sévère"),
    CRITIQUE("Critique");

    private final String libelle;

    SeveriteAntecedent(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
