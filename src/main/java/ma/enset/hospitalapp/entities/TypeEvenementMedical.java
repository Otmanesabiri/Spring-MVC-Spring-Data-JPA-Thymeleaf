package ma.enset.hospitalapp.entities;

public enum TypeEvenementMedical {
    CONSULTATION("Consultation"),
    HOSPITALISATION("Hospitalisation"),
    CHIRURGIE("Chirurgie"),
    URGENCE("Urgence"),
    DIAGNOSTIC("Diagnostic"),
    TRAITEMENT("Traitement"),
    VACCINATION("Vaccination"),
    CONTROLE("Contrôle"),
    AUTRE("Autre");

    private final String libelle;

    TypeEvenementMedical(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
