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
public class Medecin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String nom;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String prenom;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @NotEmpty
    private String specialite;

    @NotEmpty
    private String numeroOrdre; // Numéro d'ordre des médecins

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dateEmbauche;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date heureDebutTravail;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date heureFinTravail;

    private boolean actif = true;

    @Enumerated(EnumType.STRING)
    private StatutMedecin statut;

    @Min(0)
    private double salaire;

    // Relation avec les patients (consultations)
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;
}
