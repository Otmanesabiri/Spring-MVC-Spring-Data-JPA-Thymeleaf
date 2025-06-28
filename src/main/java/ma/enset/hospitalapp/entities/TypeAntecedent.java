package ma.enset.hospitalapp.entities;

public enum TypeAntecedent {
    MEDICAL("Médical"),
    CHIRURGICAL("Chirurgical"),
    FAMILIAL("Familial"),
    GYNECOLOGIQUE("Gynécologique"),
    OBSTETRICAL("Obstétrical"),
    PSYCHIATRIQUE("Psychiatrique"),
    TOXICOLOGIQUE("Toxicologique"),
    ALLERGIQUE("Allergique"),
    AUTRE("Autre");

    private final String libelle;

    TypeAntecedent(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
