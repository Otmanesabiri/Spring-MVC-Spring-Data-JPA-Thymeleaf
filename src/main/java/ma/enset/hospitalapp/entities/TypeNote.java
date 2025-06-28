package ma.enset.hospitalapp.entities;

public enum TypeNote {
    CONSULTATION("Note de consultation"),
    OBSERVATION("Observation médicale"),
    DIAGNOSTIC("Note diagnostique"),
    TRAITEMENT("Note de traitement"),
    EVOLUTION("Note d'évolution"),
    SUIVI("Note de suivi"),
    TRANSFERT("Note de transfert"),
    SORTIE("Note de sortie"),
    URGENCE("Note d'urgence"),
    AUTRE("Autre");

    private final String libelle;

    TypeNote(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
