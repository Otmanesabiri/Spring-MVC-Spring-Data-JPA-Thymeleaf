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
public class RendezVous {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "La date et l'heure du rendez-vous sont obligatoires")
    private Date dateHeureRendezVous;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull(message = "Le patient est obligatoire")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    @NotNull(message = "Le médecin est obligatoire")
    private Medecin medecin;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut est obligatoire")
    private StatutRendezVous statut = StatutRendezVous.PLANIFIE;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de rendez-vous est obligatoire")
    private TypeRendezVous type = TypeRendezVous.CONSULTATION;

    @Column(length = 1000)
    private String motif;

    @Column(length = 2000)
    private String notes;

    @Min(value = 15, message = "La durée doit être d'au moins 15 minutes")
    @Max(value = 240, message = "La durée ne peut pas dépasser 240 minutes")
    private int dureeMinutes = 30;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    // Pour les rappels
    private boolean rappelEnvoye = false;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRappel;

    // Pour les annulations/reports
    @Column(length = 1000)
    private String raisonAnnulation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnulation;

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
