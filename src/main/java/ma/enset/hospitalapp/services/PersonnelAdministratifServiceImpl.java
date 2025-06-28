package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.PersonnelAdministratif;
import ma.enset.hospitalapp.repositories.PersonnelAdministratifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonnelAdministratifServiceImpl implements PersonnelAdministratifService {

    @Autowired
    private PersonnelAdministratifRepository personnelAdministratifRepository;

    @Override
    public Page<PersonnelAdministratif> findAllPersonnelAdministratif(Pageable pageable) {
        return personnelAdministratifRepository.findAll(pageable);
    }

    @Override
    public Page<PersonnelAdministratif> findPersonnelAdministratifByKeyword(String keyword, Pageable pageable) {
        return personnelAdministratifRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public PersonnelAdministratif savePersonnelAdministratif(PersonnelAdministratif personnel) {
        return personnelAdministratifRepository.save(personnel);
    }

    @Override
    public PersonnelAdministratif findPersonnelAdministratifById(Long id) {
        return personnelAdministratifRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePersonnelAdministratif(Long id) {
        personnelAdministratifRepository.deleteById(id);
    }

    @Override
    public void activerPersonnelAdministratif(Long id) {
        PersonnelAdministratif personnel = findPersonnelAdministratifById(id);
        if (personnel != null) {
            personnel.setActif(true);
            savePersonnelAdministratif(personnel);
        }
    }

    @Override
    public void desactiverPersonnelAdministratif(Long id) {
        PersonnelAdministratif personnel = findPersonnelAdministratifById(id);
        if (personnel != null) {
            personnel.setActif(false);
            savePersonnelAdministratif(personnel);
        }
    }
}
