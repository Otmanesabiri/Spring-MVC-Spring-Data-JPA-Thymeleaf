package ma.enset.hospitalapp.entities;

public enum StatutMedecin {
    TITULAIRE("Titulaire"),
    CONTRACTUEL("Contractuel"),
    VACATAIRE("Vacataire"),
    STAGIAIRE("Stagiaire"),
    RETRAITE("Retraité"),
    SUSPENDU("Suspendu");

    private final String libelle;

    StatutMedecin(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
