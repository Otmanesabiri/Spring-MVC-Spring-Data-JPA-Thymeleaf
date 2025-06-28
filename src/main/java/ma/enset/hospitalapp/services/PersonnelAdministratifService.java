package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.PersonnelAdministratif;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonnelAdministratifService {
    Page<PersonnelAdministratif> findAllPersonnelAdministratif(Pageable pageable);
    Page<PersonnelAdministratif> findPersonnelAdministratifByKeyword(String keyword, Pageable pageable);
    PersonnelAdministratif savePersonnelAdministratif(PersonnelAdministratif personnel);
    PersonnelAdministratif findPersonnelAdministratifById(Long id);
    void deletePersonnelAdministratif(Long id);
    void activerPersonnelAdministratif(Long id);
    void desactiverPersonnelAdministratif(Long id);
}
