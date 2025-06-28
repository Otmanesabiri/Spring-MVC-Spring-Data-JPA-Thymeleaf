package ma.enset.hospitalapp.entities;

public enum TypeAllergie {
    MEDICAMENTEUSE("Médicamenteuse"),
    ALIMENTAIRE("Alimentaire"),
    ENVIRONNEMENTALE("Environnementale"),
    CUTANEE("Cutanée"),
    RESPIRATOIRE("Respiratoire"),
    AUTRE("Autre");

    private final String libelle;

    TypeAllergie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
