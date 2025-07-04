package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Lit;
import ma.enset.hospitalapp.entities.StatutLit;
import ma.enset.hospitalapp.entities.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LitRepository extends JpaRepository<Lit, Long> {
    List<Lit> findByStatut(StatutLit statut);
    List<Lit> findByChambre(Chambre chambre);
    Lit findByNumeroAndChambre(String numero, Chambre chambre);
}
