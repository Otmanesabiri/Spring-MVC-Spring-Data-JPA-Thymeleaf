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
public class Prescription {
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

    @Column(length = 100)
    @NotEmpty(message = "Le nom du médicament est obligatoire")
    private String nomMedicament;

    @Column(length = 100)
    private String dosage;

    @Column(length = 200)
    private String posologie;

    @Column(length = 500)
    private String instructions;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de prescription est obligatoire")
    private Date datePrescription;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;

    @Min(value = 1, message = "La durée doit être d'au moins 1 jour")
    private int dureeJours;

    @Enumerated(EnumType.STRING)
    private StatutPrescription statut = StatutPrescription.ACTIVE;

    @Column(length = 1000)
    private String notes;

    @Column(length = 200)
    private String frequence; // Ex: "2 fois par jour", "Matin et soir"

    private boolean renouvellable = false;

    @Min(value = 0, message = "Le nombre de renouvellements ne peut pas être négatif")
    private int nombreRenouvellements = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
        dateModification = new Date();
        if (datePrescription == null) {
            datePrescription = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = new Date();
    }

    // Méthodes utilitaires
    public boolean isExpired() {
        return dateFin != null && new Date().after(dateFin);
    }

    public String getStatutLibelle() {
        return statut != null ? statut.getLibelle() : "N/A";
    }
}
