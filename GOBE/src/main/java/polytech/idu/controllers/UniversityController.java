package polytech.idu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import polytech.idu.models.University;
import polytech.idu.repositories.UniversityRepository;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "http://localhost:5173")
public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;

    @GetMapping
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
}
