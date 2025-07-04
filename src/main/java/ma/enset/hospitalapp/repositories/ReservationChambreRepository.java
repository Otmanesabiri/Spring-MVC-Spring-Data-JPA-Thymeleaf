package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.ReservationChambre;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.entities.Chambre;
import ma.enset.hospitalapp.entities.Lit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationChambreRepository extends JpaRepository<ReservationChambre, Long> {
    List<ReservationChambre> findByPatient(Patient patient);
    List<ReservationChambre> findByChambre(Chambre chambre);
    List<ReservationChambre> findByLit(Lit lit);
}
