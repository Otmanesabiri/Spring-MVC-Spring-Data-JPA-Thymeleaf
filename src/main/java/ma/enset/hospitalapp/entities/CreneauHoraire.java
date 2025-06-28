package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class CreneauHoraire {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    @NotNull(message = "Le médecin est obligatoire")
    private Medecin medecin;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le jour de la semaine est obligatoire")
    private DayOfWeek jourSemaine;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    @Min(value = 15, message = "La durée doit être d'au moins 15 minutes")
    @Max(value = 120, message = "La durée ne peut pas dépasser 120 minutes")
    private int dureeRendezVousMinutes = 30;

    private boolean actif = true;

    @Column(length = 500)
    private String notes;
}
