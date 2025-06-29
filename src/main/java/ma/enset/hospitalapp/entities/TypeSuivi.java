package ma.enset.hospitalapp.entities;

public enum TypeSuivi {
    CONTROLE_ROUTINE("Contrôle de Routine"),
    CONTROLE_URGENT("Contrôle Urgent"),
    SUIVI_POST_OPERATOIRE("Suivi Post-Opératoire"),
    SUIVI_TRAITEMENT("Suivi de Traitement"),
    REEVALUATION("Réévaluation"),
    SURVEILLANCE("Surveillance"),
    CONSULTATION_RETOUR("Consultation de Retour"),
    BILAN_EVOLUTION("Bilan d'Évolution");

    private final String libelle;

    TypeSuivi(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public boolean isUrgent() {
        return this == CONTROLE_URGENT;
    }
}
