package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.Technicien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TechnicienService {
    Page<Technicien> findAllTechniciens(Pageable pageable);
    Page<Technicien> findTechniciensByKeyword(String keyword, Pageable pageable);
    Technicien saveTechnicien(Technicien technicien);
    Technicien findTechnicienById(Long id);
    void deleteTechnicien(Long id);
    void activerTechnicien(Long id);
    void desactiverTechnicien(Long id);
}
