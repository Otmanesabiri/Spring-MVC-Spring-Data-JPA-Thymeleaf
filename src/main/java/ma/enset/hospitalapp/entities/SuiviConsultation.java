package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class SuiviConsultation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeSuivi typeSuivi;

    @NotNull
    private LocalDateTime dateSuivi;

    @NotEmpty
    @Size(max = 100)
    private String personnel; // Nom du personnel qui fait le suivi

    @Column(length = 2000)
    private String observations;

    @Column(length = 1000)
    private String actions;

    @Enumerated(EnumType.STRING)
    private StatutSuivi statut;

    // Alias pour compatibilité
    public StatutSuivi getStatutSuivi() {
        return this.statut;
    }
    
    public void setStatutSuivi(StatutSuivi statut) {
        this.statut = statut;
    }

    // Données vitales de contrôle
    private Double tension;
    private Double temperature;
    private Integer pouls;
    private Double poids;

    @Column(length = 500)
    private String prescriptionsModifiees;

    private LocalDateTime prochainRendezVous;

    @Column(length = 1000)
    private String consignes;

    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereModification;

    // Alias pour compatibilité
    public LocalDateTime getDateModification() {
        return this.dateDerniereModification;
    }
    
    public void setDateModification(LocalDateTime date) {
        this.dateDerniereModification = date;
    }

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        this.dateDerniereModification = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutSuivi.EN_COURS;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dateDerniereModification = LocalDateTime.now();
    }

    // Méthodes utilitaires
    public String getPatientNomComplet() {
        if (consultation != null && consultation.getPatient() != null) {
            return consultation.getPatientNomComplet();
        }
        return "";
    }

    public boolean isAmeliorationConstante() {
        return StatutSuivi.AMELIORATION.equals(this.statut);
    }

    public boolean isDeterioration() {
        return StatutSuivi.DETERIORATION.equals(this.statut);
    }

    public boolean estUrgent() {
        return TypeSuivi.CONTROLE_URGENT.equals(this.typeSuivi) ||
               StatutSuivi.DETERIORATION.equals(this.statut);
    }
}
