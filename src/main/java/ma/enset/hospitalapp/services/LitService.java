package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Lit;
import java.util.List;

public interface LitService {
    Lit saveLit(Lit lit);
    Lit getLit(Long id);
    List<Lit> getAllLits();
    void deleteLit(Long id);
    List<Lit> findByStatut(String statut);
    List<Lit> findByChambre(Long chambreId);
    // Ajoutez d'autres méthodes selon les besoins (réservation, occupation...)
}
