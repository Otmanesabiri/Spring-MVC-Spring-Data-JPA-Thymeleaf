package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class DossierMedical {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id", unique = true)
    @NotNull(message = "Le patient est obligatoire")
    private Patient patient;

    @Column(unique = true, length = 20)
    @NotEmpty(message = "Le numéro de dossier est obligatoire")
    private String numeroDossier;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de création est obligatoire")
    private Date dateCreation;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDerniereModification;

    @Enumerated(EnumType.STRING)
    private StatutDossier statut = StatutDossier.ACTIF;

    @Column(length = 2000)
    private String notesGenerales;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueMedical> historiquesMedicaux;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultatExamen> resultatsExamens;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Allergie> allergies;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Antecedent> antecedents;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoteMedicale> notesMedicales;

    // Méthodes utilitaires
    public String getPatientNomComplet() {
        return patient != null ? patient.getName() : "N/A";
    }

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = new Date();
        }
        dateDerniereModification = new Date();
        if (numeroDossier == null || numeroDossier.isEmpty()) {
            // Générer un numéro de dossier automatique
            numeroDossier = "DOS" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateDerniereModification = new Date();
    }
}
