package ma.enset.hospitalapp.entities;

public enum NiveauUrgence {
    NORMAL("Normal", "#28a745"),
    FAIBLE("Faible Urgence", "#17a2b8"),
    MOYEN("Urgence Modérée", "#ffc107"),
    ELEVE("Urgence Élevée", "#fd7e14"),
    CRITIQUE("Urgence Critique", "#dc3545");

    private final String libelle;
    private final String couleur;

    NiveauUrgence(String libelle, String couleur) {
        this.libelle = libelle;
        this.couleur = couleur;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public boolean isCritique() {
        return this == CRITIQUE || this == ELEVE;
    }

    public int getNiveau() {
        return ordinal() + 1;
    }
}
