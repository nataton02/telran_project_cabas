package de.telran.telran_project_cabas.service.impl;

import de.telran.telran_project_cabas.dto.PersonRequestDTO;
import de.telran.telran_project_cabas.dto.PersonUpdateDTO;
import de.telran.telran_project_cabas.entity.City;
import de.telran.telran_project_cabas.entity.Person;
import de.telran.telran_project_cabas.repository.CityRepository;
import de.telran.telran_project_cabas.repository.PersonRepository;
import de.telran.telran_project_cabas.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;


@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    @Override
    public void createPerson(PersonRequestDTO request) {
        if(!request.getEmail().equals("") && personRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Email '%s' already exists", request.getEmail()));
        }

        if(!request.getPhoneNumber().equals("") && personRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("PhoneNumber '%s' already exists", request.getPhoneNumber()));
        }

        Period personAge = Period.between(request.getDateOfBirth(), LocalDate.now());
        Long guardianId = request.getGuardianId();

        if(personAge.getYears() < 18 && guardianId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person '%s' is too young not to have a guardian", request.getFirstName()));
        }

        if(guardianId != null) {
            checkGuardian(request, guardianId);
        }

        checkCity(request.getCity_id(), request.getAreaId());

        personRepository.save(convertDtoToPerson(request));

    }

    @Transactional
    @Override
    public void updatePerson(PersonUpdateDTO updateDTO, Long personId) {
        Person person = checkPersonExistence(personId);
        person.setFirstName(updateDTO.getFirstName());
        person.setLastName(updateDTO.getLastName());
        person.setPhoneNumber(updateDTO.getPhoneNumber());
        person.setEmail(updateDTO.getEmail());
        personRepository.save(person);
    }


    private void checkGuardian(PersonRequestDTO request, Long guardianId) {
        Person guardian = personRepository.findById(guardianId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Person with id %s does not exist", guardianId)));

        Period guardiansAge = Period.between(guardian.getDateOfBirth(), LocalDate.now());
        if (guardiansAge.getYears() < 18) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person with id %s is to young to be a guardian", guardianId));
        }

        if (guardian.getGuardianId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person with id %s has a guardian, so he cannot be a guardian", guardianId));
        }

        if (!guardian.getAreaId().equals(request.getAreaId()) || !guardian.getCityId().equals(request.getCity_id())) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person '%s' must live in the same place as the guardian", request.getFirstName()));
        }
    }

    private void checkCity(Long cityId, Long areaId) {
        City city = cityRepository.findByCityIdAndAreaId(cityId, areaId);
        if(city == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("There is no city with id %s in the area with id %s", cityId, areaId));
        }
    }

    private Person checkPersonExistence(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Person with id %s does not exist", id)));
    }

    private Person convertDtoToPerson(PersonRequestDTO request) {
        return Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .guardianId(request.getGuardianId())
                .areaId(request.getAreaId())
                .cityId(request.getCity_id())
                .build();
    }
}
