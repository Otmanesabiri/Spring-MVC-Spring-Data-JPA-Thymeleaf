package ma.enset.hospitalapp.entities;

public enum StatutEmploye {
    ACTIF("Actif"),
    INACTIF("Inactif"),
    CONGE("En congé"),
    ARRET_MALADIE("Arrêt maladie"),
    FORMATION("En formation"),
    SUSPENDU("Suspendu"),
    DEMISSIONNAIRE("Démissionnaire");

    private final String libelle;

    StatutEmploye(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
