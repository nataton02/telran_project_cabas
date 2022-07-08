package de.telran.telran_project_cabas.service;

import de.telran.telran_project_cabas.dto.PersonRequestDTO;
import de.telran.telran_project_cabas.dto.PersonUpdateDTO;


public interface PersonService {
    void createPerson(PersonRequestDTO request);

    void updatePerson(PersonUpdateDTO updateDTO, Long personId);

}
