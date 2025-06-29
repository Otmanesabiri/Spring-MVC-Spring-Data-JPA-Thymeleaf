package ma.enset.hospitalapp.entities;

public enum StatutSuivi {
    EN_COURS("En Cours", "#17a2b8"),
    AMELIORATION("Amélioration", "#28a745"),
    STABLE("État Stable", "#6c757d"),
    DETERIORATION("Détérioration", "#dc3545"),
    GUERI("Guéri", "#28a745"),
    TRANSFERE("Transféré", "#ffc107"),
    TERMINE("Suivi Terminé", "#6c757d");

    private final String libelle;
    private final String couleur;

    StatutSuivi(String libelle, String couleur) {
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

    public boolean isPositif() {
        return this == AMELIORATION || this == STABLE || this == GUERI;
    }

    public boolean necessiteAttention() {
        return this == DETERIORATION;
    }
}
