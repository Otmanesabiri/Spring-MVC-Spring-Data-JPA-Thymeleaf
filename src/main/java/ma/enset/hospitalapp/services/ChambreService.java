package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Chambre;
import java.util.List;

public interface ChambreService {
    Chambre saveChambre(Chambre chambre);
    Chambre getChambre(Long id);
    List<Chambre> getAllChambres();
    void deleteChambre(Long id);
    List<Chambre> findByStatut(String statut);
    List<Chambre> findByType(String type);
    // Ajoutez d'autres méthodes selon les besoins (occupation, maintenance...)
}
