package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Infirmier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfirmierService {
    Page<Infirmier> findAllInfirmiers(Pageable pageable);
    Page<Infirmier> findInfirmiersByKeyword(String keyword, Pageable pageable);
    Infirmier saveInfirmier(Infirmier infirmier);
    Infirmier findInfirmierById(Long id);
    void deleteInfirmier(Long id);
    void activerInfirmier(Long id);
    void desactiverInfirmier(Long id);
}
