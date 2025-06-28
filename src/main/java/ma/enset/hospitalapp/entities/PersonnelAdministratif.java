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
public class PersonnelAdministratif {
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
    private String poste; // Réceptionniste, Gestionnaire, Secrétaire médicale, etc.

    @NotEmpty
    private String departement;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dateEmbauche;

    @Enumerated(EnumType.STRING)
    private HoraireTravail horaire;

    private boolean actif = true;

    @Enumerated(EnumType.STRING)
    private StatutEmploye statut;

    @Min(0)
    private double salaire;

    @Enumerated(EnumType.STRING)
    private NiveauAcces niveauAcces;
}
