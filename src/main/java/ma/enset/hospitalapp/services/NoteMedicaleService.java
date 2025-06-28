package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.NoteMedicale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteMedicaleService {
    Page<NoteMedicale> getAllNotes(String keyword, Pageable pageable);
    Page<NoteMedicale> getNotesByDossier(Long dossierId, Pageable pageable);
    NoteMedicale getNoteById(Long id);
    NoteMedicale saveNote(NoteMedicale note);
    void deleteNote(Long id);
    List<NoteMedicale> getRecentNotesByDossier(Long dossierId, int limit);
}
