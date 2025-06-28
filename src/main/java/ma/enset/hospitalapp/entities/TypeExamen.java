package ma.enset.hospitalapp.entities;

public enum TypeExamen {
    SANGUIN("Examen sanguin"),
    URINAIRE("Examen urinaire"),
    RADIOLOGIE("Radiologie"),
    SCANNER("Scanner"),
    IRM("IRM"),
    ECHOGRAPHIE("Échographie"),
    ECG("Électrocardiogramme"),
    EEG("Électroencéphalogramme"),
    ENDOSCOPIE("Endoscopie"),
    BIOPSIE("Biopsie"),
    MICROBIOLOGIE("Microbiologie"),
    HISTOLOGIE("Histologie"),
    AUTRE("Autre");

    private final String libelle;

    TypeExamen(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
