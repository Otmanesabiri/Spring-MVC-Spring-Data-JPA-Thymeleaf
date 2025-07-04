package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.Chambre;
import ma.enset.hospitalapp.entities.StatutChambre;
import ma.enset.hospitalapp.entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByStatut(StatutChambre statut);
    List<Chambre> findByType(TypeChambre type);
    Chambre findByNumero(String numero);
}
