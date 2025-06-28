package ma.enset.hospitalapp.entities;

public enum TypeRendezVous {
    CONSULTATION("Consultation"),
    CONTROLE("Contrôle"),
    URGENCE("Urgence"),
    CHIRURGIE("Chirurgie"),
    EXAMEN("Examen"),
    VACCINATION("Vaccination"),
    SUIVI("Suivi");

    private final String libelle;

    TypeRendezVous(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
