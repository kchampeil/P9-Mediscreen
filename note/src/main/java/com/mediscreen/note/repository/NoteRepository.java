package com.mediscreen.note.repository;

import java.util.List;

import com.mediscreen.note.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    Page<Note> findAllByPatientId(Integer patientId, Pageable pageable);

    List<Note> findAllByPatientId(Integer patientId);
}
