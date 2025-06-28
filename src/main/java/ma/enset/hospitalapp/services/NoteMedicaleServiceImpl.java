package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.NoteMedicale;
import ma.enset.hospitalapp.repositories.NoteMedicaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteMedicaleServiceImpl implements NoteMedicaleService {

    @Autowired
    private NoteMedicaleRepository noteMedicaleRepository;

    @Override
    public Page<NoteMedicale> getAllNotes(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return noteMedicaleRepository.findByContenuContainingIgnoreCase(keyword, pageable);
        }
        return noteMedicaleRepository.findAll(pageable);
    }

    @Override
    public Page<NoteMedicale> getNotesByDossier(Long dossierId, Pageable pageable) {
        return noteMedicaleRepository.findByDossierMedicalId(dossierId, pageable);
    }

    @Override
    public NoteMedicale getNoteById(Long id) {
        return noteMedicaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note médicale introuvable avec l'ID: " + id));
    }

    @Override
    public NoteMedicale saveNote(NoteMedicale note) {
        return noteMedicaleRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        noteMedicaleRepository.deleteById(id);
    }

    @Override
    public List<NoteMedicale> getRecentNotesByDossier(Long dossierId, int limit) {
        return noteMedicaleRepository.findTop5ByDossierMedicalIdOrderByDateCreationDesc(dossierId);
    }
}
