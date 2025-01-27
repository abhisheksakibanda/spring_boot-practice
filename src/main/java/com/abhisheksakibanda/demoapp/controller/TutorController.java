package com.abhisheksakibanda.demoapp.controller;

import com.abhisheksakibanda.demoapp.model.Tutor;
import com.abhisheksakibanda.demoapp.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tutor")
public class TutorController {

    private final TutorService service;

    @Autowired
    public TutorController(TutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Tutor>> getTutors() {
        return service.getTutors();
    }

    @PostMapping
    public ResponseEntity<String> registerNewTutor(@Valid @RequestBody Tutor Tutor) {
        return service.addNewTutor(Tutor);
    }

    @PutMapping(path = "{tutorId}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable("tutorId") Long tutorId, @RequestBody Tutor tutor) {
        return service.updateTutor(tutorId, tutor);
    }

    @DeleteMapping(path = "{tutorId}")
    public ResponseEntity<String> deleteTutor(@PathVariable("tutorId") Long tutorId) {
        return service.deleteTutor(tutorId);
    }
}
