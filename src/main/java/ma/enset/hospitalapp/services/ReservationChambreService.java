package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.ReservationChambre;
import java.util.List;

public interface ReservationChambreService {
    ReservationChambre saveReservation(ReservationChambre reservation);
    ReservationChambre getReservation(Long id);
    List<ReservationChambre> getAllReservations();
    void deleteReservation(Long id);
    List<ReservationChambre> findByPatient(Long patientId);
    List<ReservationChambre> findByChambre(Long chambreId);
    List<ReservationChambre> findByLit(Long litId);
    // Ajoutez d'autres méthodes selon les besoins (par date, statut...)
}
