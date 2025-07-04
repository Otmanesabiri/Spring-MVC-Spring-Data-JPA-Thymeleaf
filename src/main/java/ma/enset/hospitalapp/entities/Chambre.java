package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    private TypeChambre type;

    @Enumerated(EnumType.STRING)
    private StatutChambre statut;

    private int etage;

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL)
    private List<Lit> lits;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public TypeChambre getType() { return type; }
    public void setType(TypeChambre type) { this.type = type; }
    public StatutChambre getStatut() { return statut; }
    public void setStatut(StatutChambre statut) { this.statut = statut; }
    public int getEtage() { return etage; }
    public void setEtage(int etage) { this.etage = etage; }
    public List<Lit> getLits() { return lits; }
    public void setLits(List<Lit> lits) { this.lits = lits; }
}
