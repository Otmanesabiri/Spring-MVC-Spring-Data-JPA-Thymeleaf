package ma.enset.hospitalapp.entities;

public enum StatutResultat {
    NORMAL("Normal"),
    ANORMAL("Anormal"),
    PATHOLOGIQUE("Pathologique"),
    A_SURVEILLER("À surveiller"),
    URGENT("Urgent"),
    EN_ATTENTE("En attente");

    private final String libelle;

    StatutResultat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
