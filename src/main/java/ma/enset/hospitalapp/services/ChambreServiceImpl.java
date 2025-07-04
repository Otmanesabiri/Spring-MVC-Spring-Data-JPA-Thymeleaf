package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChambreServiceImpl implements ChambreService {
    @Autowired
    private ChambreRepository chambreRepository;

    @Override
    public Chambre saveChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    @Override
    public Chambre getChambre(Long id) {
        return chambreRepository.findById(id).orElse(null);
    }

    @Override
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    @Override
    public void deleteChambre(Long id) {
        chambreRepository.deleteById(id);
    }

    @Override
    public List<Chambre> findByStatut(String statut) {
        return chambreRepository.findByStatut(StatutChambre.valueOf(statut));
    }

    @Override
    public List<Chambre> findByType(String type) {
        return chambreRepository.findByType(TypeChambre.valueOf(type));
    }
}
