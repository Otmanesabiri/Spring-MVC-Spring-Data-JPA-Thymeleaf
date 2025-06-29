package ma.enset.hospitalapp.entities;

public enum TypeConsultation {
    CONSULTATION_EXTERNE("Consultation Externe"),
    URGENCE("Urgence"),
    HOSPITALISATION("Hospitalisation"),
    SUIVI_POST_CONSULTATION("Suivi Post-Consultation"),
    TELECONSULTATION("Téléconsultation"),
    CONSULTATION_SPECIALISTE("Consultation Spécialiste");

    private final String libelle;

    TypeConsultation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
