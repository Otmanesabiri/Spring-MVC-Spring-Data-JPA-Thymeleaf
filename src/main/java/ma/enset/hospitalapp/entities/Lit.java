package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"numero", "chambre_id"}))
public class Lit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    private StatutLit statut;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public StatutLit getStatut() { return statut; }
    public void setStatut(StatutLit statut) { this.statut = statut; }
    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }
}
