package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@ToString(exclude = {"suivis", "patient", "dossierMedical"})
public class Consultation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "dossier_medical_id")
    private DossierMedical dossierMedical;

    @NotEmpty
    @Size(max = 100)
    private String medecin;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeConsultation type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatutConsultation statut;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateHeureConsultation;

    @Column(length = 1000)
    private String motifConsultation;

    @Column(length = 2000)
    private String symptomes;

    @Column(length = 2000)
    private String examenClinique;

    @Column(length = 1000)
    private String diagnostic;

    @Column(length = 2000)
    private String traitement;

    @Column(length = 1000)
    private String recommandations;

    @Column(length = 500)
    private String observationsInfirmiere;

    // Données vitales
    private Double tension;
    private Double temperature;
    private Integer pouls;
    private Double poids;
    private Double taille;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NiveauUrgence niveauUrgence;

    @Min(0)
    private Double tarif;
    private boolean payee = false;

    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereModification;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SuiviConsultation> suivis;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        this.dateDerniereModification = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutConsultation.PROGRAMMEE;
        }
        if (this.niveauUrgence == null) {
            this.niveauUrgence = NiveauUrgence.NORMAL;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dateDerniereModification = LocalDateTime.now();
    }

    // Méthodes utilitaires
    public String getPatientNomComplet() {
        if (patient == null) return "";
        return (patient.getNom() != null && patient.getPrenom() != null) 
            ? patient.getNom() + " " + patient.getPrenom() 
            : patient.getName();
    }

    public boolean isUrgence() {
        return TypeConsultation.URGENCE.equals(this.type);
    }

    public boolean isHospitalisation() {
        return TypeConsultation.HOSPITALISATION.equals(this.type);
    }

    public boolean isConsultationExterne() {
        return TypeConsultation.CONSULTATION_EXTERNE.equals(this.type);
    }

    public String getDureeDansService() {
        if (dateHeureConsultation != null && dateDerniereModification != null) {
            // Calculer la durée entre l'arrivée et maintenant ou la fin
            LocalDateTime fin = (StatutConsultation.TERMINEE.equals(statut)) 
                ? dateDerniereModification 
                : LocalDateTime.now();
            
            long heures = java.time.Duration.between(dateHeureConsultation, fin).toHours();
            return heures + " heure(s)";
        }
        return "N/A";
    }
}
