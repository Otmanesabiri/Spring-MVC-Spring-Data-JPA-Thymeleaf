package ma.enset.hospitalapp.entities;

public enum SeveriteAllergie {
    LEGERE("Légère"),
    MODEREE("Modérée"),
    SEVERE("Sévère"),
    ANAPHYLACTIQUE("Anaphylactique");

    private final String libelle;

    SeveriteAllergie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
