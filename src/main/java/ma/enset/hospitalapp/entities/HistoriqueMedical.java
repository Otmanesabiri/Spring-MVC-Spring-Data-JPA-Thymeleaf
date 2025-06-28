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
public class HistoriqueMedical {
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

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "La date de l'événement est obligatoire")
    private Date dateEvenement;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type d'événement est obligatoire")
    private TypeEvenementMedical typeEvenement;

    @Column(length = 100)
    @NotEmpty(message = "Le titre est obligatoire")
    private String titre;

    @Column(length = 5000)
    @NotEmpty(message = "La description est obligatoire")
    private String description;

    @Enumerated(EnumType.STRING)
    private NiveauGravite niveauGravite = NiveauGravite.NORMAL;

    @Column(length = 2000)
    private String traitement;

    @Column(length = 1000)
    private String recommandations;

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
}
