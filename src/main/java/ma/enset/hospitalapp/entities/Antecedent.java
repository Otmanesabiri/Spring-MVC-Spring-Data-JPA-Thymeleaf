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
public class Antecedent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_medical_id")
    @NotNull(message = "Le dossier médical est obligatoire")
    private DossierMedical dossierMedical;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type d'antécédent est obligatoire")
    private TypeAntecedent typeAntecedent;

    @Column(length = 100)
    @NotEmpty(message = "Le nom de l'antécédent est obligatoire")
    private String nom;

    @Column(length = 2000)
    private String description;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;

    private boolean actuel = true;

    @Enumerated(EnumType.STRING)
    private SeveriteAntecedent severite = SeveriteAntecedent.NORMAL;

    @Column(length = 1000)
    private String traitement;

    @Column(length = 1000)
    private String notes;

    // Pour les antécédents familiaux
    @Column(length = 100)
    private String lienParente; // Ex: "Père", "Mère", "Frère", etc.

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
    public String getTypeAntecedentLibelle() {
        return typeAntecedent != null ? typeAntecedent.getLibelle() : "N/A";
    }

    public String getSeveriteLibelle() {
        return severite != null ? severite.getLibelle() : "N/A";
    }

    public boolean isFamilial() {
        return typeAntecedent == TypeAntecedent.FAMILIAL;
    }
}
