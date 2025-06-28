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
public class ResultatExamen {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_medical_id")
    @NotNull(message = "Le dossier médical est obligatoire")
    private DossierMedical dossierMedical;

    @ManyToOne
    @JoinColumn(name = "medecin_prescripteur_id")
    private Medecin medecinPrescripteur;

    @Column(length = 100)
    @NotEmpty(message = "Le nom de l'examen est obligatoire")
    private String nomExamen;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type d'examen est obligatoire")
    private TypeExamen typeExamen;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de l'examen est obligatoire")
    private Date dateExamen;

    @Column(length = 5000)
    @NotEmpty(message = "Les résultats sont obligatoires")
    private String resultats;

    @Column(length = 2000)
    private String interpretation;

    @Column(length = 200)
    private String valeursReference;

    @Enumerated(EnumType.STRING)
    private StatutResultat statut = StatutResultat.NORMAL;

    @Column(length = 1000)
    private String commentaires;

    @Column(length = 200)
    private String laboratoire;

    @Column(length = 100)
    private String technicien;

    private boolean urgent = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
        dateModification = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = new Date();
    }

    // Méthodes utilitaires
    public String getStatutLibelle() {
        return statut != null ? statut.getLibelle() : "N/A";
    }

    public String getTypeExamenLibelle() {
        return typeExamen != null ? typeExamen.getLibelle() : "N/A";
    }
}
