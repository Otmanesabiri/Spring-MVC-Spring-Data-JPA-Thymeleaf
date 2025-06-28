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
public class Allergie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_medical_id")
    @NotNull(message = "Le dossier médical est obligatoire")
    private DossierMedical dossierMedical;

    @Column(length = 100)
    @NotEmpty(message = "Le nom de l'allergène est obligatoire")
    private String allergene;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type d'allergie est obligatoire")
    private TypeAllergie typeAllergie;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La sévérité est obligatoire")
    private SeveriteAllergie severite;

    @Column(length = 2000)
    private String symptomes;

    @Column(length = 1000)
    private String traitement;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDecouverte;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDerniereReaction;

    private boolean active = true;

    @Column(length = 1000)
    private String notes;

    @Column(length = 200)
    private String precautions;

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
    public String getSeveriteLibelle() {
        return severite != null ? severite.getLibelle() : "N/A";
    }

    public String getTypeAllergieLibelle() {
        return typeAllergie != null ? typeAllergie.getLibelle() : "N/A";
    }
}
