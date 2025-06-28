package ma.enset.hospitalapp.entities;

public enum NiveauAcces {
    BASIQUE("Accès basique"),
    INTERMEDIAIRE("Accès intermédiaire"),
    AVANCE("Accès avancé"),
    ADMINISTRATEUR("Administrateur"),
    SUPER_ADMIN("Super administrateur");

    private final String libelle;

    NiveauAcces(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
