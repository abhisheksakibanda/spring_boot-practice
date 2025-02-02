package com.abhisheksakibanda.demoapp.service;

import com.abhisheksakibanda.demoapp.exception.ConflictException;
import com.abhisheksakibanda.demoapp.exception.ResourceNotFoundException;
import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.repository.TutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public ResponseEntity<List<Tutor>> getTutors() {
        List<Tutor> tutors = tutorRepository.findAll();

        if (tutors.isEmpty()) {
            throw new ResourceNotFoundException("No tutors found");
        }

        return ResponseEntity.ok(tutors);
    }

    public ResponseEntity<String> addNewTutor(Tutor tutor) {
        if (tutorRepository.existsByFirstName(tutor.getFirstName())) {
            throw new ConflictException("Tutor already exists");
        }

        tutorRepository.save(tutor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tutor added successfully");
    }

    @Transactional
    public ResponseEntity<Tutor> updateTutor(Long tutorId, Tutor tutor) {
        Tutor foundTutor = tutorRepository.findById(tutorId).orElseThrow(() -> new ResourceNotFoundException("Tutor with ID " + tutorId + " does not exist"));

        // Update the tutor's first name if provided
        // Not required to use repository method to update (save()) the tutor values when in a transaction, as the entity is already managed
        if (tutor.getFirstName() != null && !tutor.getFirstName().isBlank() && !tutor.getFirstName().equals(foundTutor.getFirstName())) {
            foundTutor.setFirstName(tutor.getFirstName());
        }

        if (tutor.getLastName() != null && !tutor.getLastName().isBlank() && !tutor.getLastName().equals(foundTutor.getLastName())) {
            foundTutor.setLastName(tutor.getLastName());
        }
        return ResponseEntity.ok(foundTutor);
    }

    public ResponseEntity<String> deleteTutor(Long tutorId) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new ResourceNotFoundException("Tutor with id " + tutorId + " does not exist");
        }
        tutorRepository.deleteById(tutorId);
        return ResponseEntity.ok("Tutor with id " + tutorId + " deleted successfully");
    }
}
