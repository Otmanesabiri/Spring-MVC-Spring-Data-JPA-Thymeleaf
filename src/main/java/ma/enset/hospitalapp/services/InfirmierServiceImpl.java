package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Infirmier;
import ma.enset.hospitalapp.repositories.InfirmierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InfirmierServiceImpl implements InfirmierService {

    @Autowired
    private InfirmierRepository infirmierRepository;

    @Override
    public Page<Infirmier> findAllInfirmiers(Pageable pageable) {
        return infirmierRepository.findAll(pageable);
    }

    @Override
    public Page<Infirmier> findInfirmiersByKeyword(String keyword, Pageable pageable) {
        return infirmierRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Infirmier saveInfirmier(Infirmier infirmier) {
        return infirmierRepository.save(infirmier);
    }

    @Override
    public Infirmier findInfirmierById(Long id) {
        return infirmierRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteInfirmier(Long id) {
        infirmierRepository.deleteById(id);
    }

    @Override
    public void activerInfirmier(Long id) {
        Infirmier infirmier = findInfirmierById(id);
        if (infirmier != null) {
            infirmier.setActif(true);
            saveInfirmier(infirmier);
        }
    }

    @Override
    public void desactiverInfirmier(Long id) {
        Infirmier infirmier = findInfirmierById(id);
        if (infirmier != null) {
            infirmier.setActif(false);
            saveInfirmier(infirmier);
        }
    }
}
