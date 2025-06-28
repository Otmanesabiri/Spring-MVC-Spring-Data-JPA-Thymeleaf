package ma.enset.hospitalapp.repositories;

import ma.enset.hospitalapp.entities.NoteMedicale;
import ma.enset.hospitalapp.entities.DossierMedical;
import ma.enset.hospitalapp.entities.Medecin;
import ma.enset.hospitalapp.entities.TypeNote;
import ma.enset.hospitalapp.entities.NiveauConfidentialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface NoteMedicaleRepository extends JpaRepository<NoteMedicale, Long> {
    
    Page<NoteMedicale> findByDossierMedical(DossierMedical dossierMedical, Pageable pageable);
    
    Page<NoteMedicale> findByDossierMedicalId(Long dossierId, Pageable pageable);
    
    Page<NoteMedicale> findByContenuContainingIgnoreCase(String keyword, Pageable pageable);
    
    List<NoteMedicale> findTop5ByDossierMedicalIdOrderByDateCreationDesc(Long dossierId);
    
    Page<NoteMedicale> findByMedecin(Medecin medecin, Pageable pageable);
    
    Page<NoteMedicale> findByTypeNote(TypeNote typeNote, Pageable pageable);
    
    Page<NoteMedicale> findByNiveauConfidentialite(NiveauConfidentialite niveau, Pageable pageable);
    
    @Query("SELECT n FROM NoteMedicale n WHERE n.dossierMedical = :dossier AND " +
           "(LOWER(n.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.contenu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.motsCles) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<NoteMedicale> findByDossierAndKeyword(@Param("dossier") DossierMedical dossier, 
                                              @Param("keyword") String keyword, 
                                              Pageable pageable);
    
    @Query("SELECT n FROM NoteMedicale n WHERE n.dossierMedical = :dossier AND " +
           "n.dateCreation BETWEEN :dateDebut AND :dateFin ORDER BY n.dateCreation DESC")
    List<NoteMedicale> findByDossierAndDateCreationBetween(@Param("dossier") DossierMedical dossier,
                                                          @Param("dateDebut") Date dateDebut,
                                                          @Param("dateFin") Date dateFin);
    
    @Query("SELECT n FROM NoteMedicale n WHERE n.dossierMedical = :dossier AND n.archivee = false ORDER BY n.dateCreation DESC")
    List<NoteMedicale> findActiveNotesByDossier(@Param("dossier") DossierMedical dossier);
    
    @Query("SELECT n FROM NoteMedicale n WHERE n.noteParent = :noteParent ORDER BY n.numeroVersion DESC")
    List<NoteMedicale> findVersionsByNoteParent(@Param("noteParent") NoteMedicale noteParent);
    
    @Query("SELECT n FROM NoteMedicale n WHERE n.dossierMedical = :dossier ORDER BY n.dateCreation DESC")
    List<NoteMedicale> findByDossierMedicalOrderByDateCreationDesc(@Param("dossier") DossierMedical dossier);
}
