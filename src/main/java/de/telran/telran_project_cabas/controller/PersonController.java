package de.telran.telran_project_cabas.controller;

import de.telran.telran_project_cabas.dto.*;
import de.telran.telran_project_cabas.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/api/people")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(@RequestBody PersonRequestDTO request) {
        personService.createPerson(request);
    }

    @PutMapping("/api/people/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePerson(@RequestBody PersonUpdateDTO updateDTO,
                             @PathVariable("id") Long personId) {
        personService.updatePerson(updateDTO, personId);
    }

    @PostMapping("/api/people/{id}/guardians")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGuardian(@RequestBody PersonGuardianDTO guardianDTO,
                               @PathVariable("id") Long personId) {
        personService.createGuardian(guardianDTO, personId);
    }

    @PatchMapping("/api/people/guardians")
    public void changeGuardian(@RequestBody ChangeGuardianRequestDTO request) {
        personService.changeGuardian(request);
    }

    @GetMapping("/api/people/{id}")
    public PersonResponseDTO getPerson(@PathVariable("id") Long personId) {
        return personService.getPersonById(personId);
    }
}
