package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.ReservationChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationChambreServiceImpl implements ReservationChambreService {
    @Autowired
    private ReservationChambreRepository reservationChambreRepository;

    @Override
    public ReservationChambre saveReservation(ReservationChambre reservation) {
        return reservationChambreRepository.save(reservation);
    }

    @Override
    public ReservationChambre getReservation(Long id) {
        return reservationChambreRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReservationChambre> getAllReservations() {
        return reservationChambreRepository.findAll();
    }

    @Override
    public void deleteReservation(Long id) {
        reservationChambreRepository.deleteById(id);
    }

    @Override
    public List<ReservationChambre> findByPatient(Long patientId) {
        Patient patient = new Patient();
        patient.setId(patientId);
        return reservationChambreRepository.findByPatient(patient);
    }

    @Override
    public List<ReservationChambre> findByChambre(Long chambreId) {
        Chambre chambre = new Chambre();
        chambre.setId(chambreId);
        return reservationChambreRepository.findByChambre(chambre);
    }

    @Override
    public List<ReservationChambre> findByLit(Long litId) {
        Lit lit = new Lit();
        lit.setId(litId);
        return reservationChambreRepository.findByLit(lit);
    }
}
