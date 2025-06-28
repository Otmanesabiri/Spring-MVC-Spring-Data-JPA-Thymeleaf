package ma.enset.hospitalapp.entities;

public enum StatutRendezVous {
    PLANIFIE("Planifié"),
    CONFIRME("Confirmé"),
    EN_COURS("En cours"),
    TERMINE("Terminé"),
    ANNULE("Annulé"),
    REPORTE("Reporté"),
    NON_PRESENTE("Non présenté");

    private final String libelle;

    StatutRendezVous(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
