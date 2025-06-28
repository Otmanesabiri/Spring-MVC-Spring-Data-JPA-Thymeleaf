package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class NoteMedicale {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_medical_id")
    @NotNull(message = "Le dossier médical est obligatoire")
    private DossierMedical dossierMedical;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    @NotNull(message = "Le médecin est obligatoire")
    private Medecin medecin;

    @Column(length = 200)
    @NotEmpty(message = "Le titre est obligatoire")
    private String titre;

    @Column(length = 10000)
    @NotEmpty(message = "Le contenu est obligatoire")
    private String contenu;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de note est obligatoire")
    private TypeNote typeNote;

    @Enumerated(EnumType.STRING)
    private NiveauConfidentialite niveauConfidentialite = NiveauConfidentialite.NORMAL;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "La date de création est obligatoire")
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    private boolean archivee = false;

    @Column(length = 1000)
    private String motsCles; // Pour faciliter la recherche

    // Pour les modifications/versions
    @ManyToOne
    @JoinColumn(name = "note_parent_id")
    private NoteMedicale noteParent;

    private int numeroVersion = 1;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = new Date();
        }
        dateModification = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = new Date();
    }

    // Méthodes utilitaires
    public String getTypeNoteLibelle() {
        return typeNote != null ? typeNote.getLibelle() : "N/A";
    }

    public String getNiveauConfidentialiteLibelle() {
        return niveauConfidentialite != null ? niveauConfidentialite.getLibelle() : "N/A";
    }

    public String getMedecinNomComplet() {
        return medecin != null ? "Dr. " + medecin.getPrenom() + " " + medecin.getNom() : "N/A";
    }
}
