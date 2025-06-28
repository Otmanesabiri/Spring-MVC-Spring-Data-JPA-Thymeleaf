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
public class Infirmier {
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
    private String specialite; // Infirmier généraliste, spécialisé en réanimation, etc.

    @NotEmpty
    private String diplome;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dateEmbauche;

    @Enumerated(EnumType.STRING)
    private EquipeType equipe;

    @Enumerated(EnumType.STRING)
    private HoraireTravail horaire;

    private boolean actif = true;

    @Enumerated(EnumType.STRING)
    private StatutEmploye statut;

    @Min(0)
    private double salaire;

    private String service; // Service hospitalier affecté
}
