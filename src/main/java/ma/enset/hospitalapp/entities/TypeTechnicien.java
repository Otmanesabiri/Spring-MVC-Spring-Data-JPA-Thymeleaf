package ma.enset.hospitalapp.entities;

public enum TypeTechnicien {
    LABORATOIRE("Technicien de laboratoire"),
    RADIOLOGIE("Technicien en radiologie"),
    PHARMACIE("Technicien en pharmacie"),
    MAINTENANCE_BIOMEDICALE("Technicien biomédical"),
    INFORMATIQUE("Technicien informatique"),
    ANESTHESIE("Technicien d'anesthésie"),
    BLOC_OPERATOIRE("Technicien de bloc opératoire"),
    STERILISATION("Technicien en stérilisation");

    private final String libelle;

    TypeTechnicien(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
