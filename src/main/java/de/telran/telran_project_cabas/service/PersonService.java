package de.telran.telran_project_cabas.service;

import de.telran.telran_project_cabas.dto.*;


public interface PersonService {
    void createPerson(PersonRequestDTO request);

    void updatePerson(PersonUpdateDTO updateDTO, Long personId);

    void createGuardian(PersonGuardianDTO guardianDTO, Long personId);

    void changeGuardian(ChangeGuardianRequestDTO request);

    PersonResponseDTO getPersonById(Long personId);

    PersonResponseDTO getPersonByEmail(String email);

    void movePerson(PersonMoveRequestDTO request);
}
