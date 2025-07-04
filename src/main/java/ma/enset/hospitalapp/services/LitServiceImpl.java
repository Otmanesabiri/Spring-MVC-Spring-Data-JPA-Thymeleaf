package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.LitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LitServiceImpl implements LitService {
    @Autowired
    private LitRepository litRepository;

    @Override
    public Lit saveLit(Lit lit) {
        // Vérifier l'unicité du numéro de lit dans la chambre
        Lit existingLit = litRepository.findByNumeroAndChambre(lit.getNumero(), lit.getChambre());
        if (existingLit != null && !existingLit.getId().equals(lit.getId())) {
            throw new IllegalArgumentException("Un lit avec le numéro '" + lit.getNumero() + "' existe déjà dans cette chambre");
        }
        return litRepository.save(lit);
    }

    @Override
    public Lit getLit(Long id) {
        return litRepository.findById(id).orElse(null);
    }

    @Override
    public List<Lit> getAllLits() {
        return litRepository.findAll();
    }

    @Override
    public void deleteLit(Long id) {
        litRepository.deleteById(id);
    }

    @Override
    public List<Lit> findByStatut(String statut) {
        return litRepository.findByStatut(StatutLit.valueOf(statut));
    }

    @Override
    public List<Lit> findByChambre(Long chambreId) {
        Chambre chambre = new Chambre();
        chambre.setId(chambreId);
        return litRepository.findByChambre(chambre);
    }
}
