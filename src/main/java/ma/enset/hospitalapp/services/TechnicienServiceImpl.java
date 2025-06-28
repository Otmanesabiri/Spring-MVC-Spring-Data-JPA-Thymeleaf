package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Technicien;
import ma.enset.hospitalapp.repositories.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TechnicienServiceImpl implements TechnicienService {

    @Autowired
    private TechnicienRepository technicienRepository;

    @Override
    public Page<Technicien> findAllTechniciens(Pageable pageable) {
        return technicienRepository.findAll(pageable);
    }

    @Override
    public Page<Technicien> findTechniciensByKeyword(String keyword, Pageable pageable) {
        return technicienRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Technicien saveTechnicien(Technicien technicien) {
        return technicienRepository.save(technicien);
    }

    @Override
    public Technicien findTechnicienById(Long id) {
        return technicienRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTechnicien(Long id) {
        technicienRepository.deleteById(id);
    }

    @Override
    public void activerTechnicien(Long id) {
        Technicien technicien = findTechnicienById(id);
        if (technicien != null) {
            technicien.setActif(true);
            saveTechnicien(technicien);
        }
    }

    @Override
    public void desactiverTechnicien(Long id) {
        Technicien technicien = findTechnicienById(id);
        if (technicien != null) {
            technicien.setActif(false);
            saveTechnicien(technicien);
        }
    }
}
