package de.telran.telran_project_cabas.service.impl;

import de.telran.telran_project_cabas.dto.*;
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
import java.util.List;
import java.util.stream.Collectors;


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
                    "This person is too young not to have a guardian");
        }

        if(guardianId != null) {
            Person guardian = checkPersonExistence(guardianId);

            checkGuardiansAge(guardian.getDateOfBirth());

            checkGuardiansExistence(guardian);

            if (!guardian.getAreaId().equals(request.getAreaId()) || !guardian.getCityId().equals(request.getCity_id())) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        String.format("The person '%s' must live in the same place as the guardian", request.getFirstName()));
            }
        }

        checkCity(request.getCity_id(), request.getAreaId());

        personRepository.save(convertPersonDtoToPerson(request));

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

    @Transactional
    @Override
    public void createGuardian(PersonGuardianDTO guardianDTO, Long personId) {
        Person person = checkPersonExistence(personId);

        if(personRepository.existsByGuardianId(personId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person with id %s is a guardian, so he cannot have a guardian", personId));
        }
        if(person.getGuardianId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("The person with id %s already has a guardian", personId));
        }

        checkGuardiansAge(guardianDTO.getDateOfBirth());
        checkCity(guardianDTO.getCity_id(), guardianDTO.getAreaId());

        Person guardian = convertGuardianDtoToPerson(guardianDTO);

        personRepository.save(guardian);

        person.setGuardianId(guardian.getPersonId());
        person.setCityId(guardian.getCityId());
        person.setAreaId(guardianDTO.getAreaId());

        personRepository.save(person);

    }

    @Transactional
    @Override
    public void changeGuardian(ChangeGuardianRequestDTO request) {
        Long fromGuardianId = request.getFromGuardian();
        checkPersonExistence(fromGuardianId);

        Long toGuardianId = request.getToGuardian();
        Person toGuardian = checkPersonExistence(toGuardianId);
        checkGuardiansAge(toGuardian.getDateOfBirth());
        checkGuardiansExistence(toGuardian);

        List<Long> childrenIdsFromGuardian = getChildrenIdsByGuardian(fromGuardianId);

        List<Long> childrenIdsToMove = request.getChildrenIds();
        childrenIdsToMove.stream()
                .map(childId -> {
                    Person child = checkPersonExistence(childId);
                    if(!childrenIdsFromGuardian.contains(childId)) {
                        throw new ResponseStatusException(
                                HttpStatus.UNPROCESSABLE_ENTITY,
                                String.format("The person with id %s has no child with id %s",
                                        fromGuardianId, childId));
                    }
                    child.setGuardianId(toGuardianId);
                    child.setAreaId(toGuardian.getAreaId());
                    child.setCityId(toGuardian.getCityId());
                    return child;
                })
                .forEach(child -> personRepository.save(child));
    }

    @Transactional
    @Override
    public PersonResponseDTO getPersonById(Long personId) {
        Person person = checkPersonExistence(personId);
        List<Long> childrenIds = getChildrenIdsByGuardian(personId);

        return convertPersonToPersonDto(person, childrenIds);
    }


    private Person checkPersonExistence(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Person with id %s does not exist", id)));
    }

    private void checkGuardiansAge(LocalDate dateOfBirth) {
        Period guardiansAge = Period.between(dateOfBirth, LocalDate.now());
        if (guardiansAge.getYears() < 18) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("This person is only %s years old, so he cannot be a guardian", guardiansAge.getYears()));
        }
    }

    private void checkGuardiansExistence(Person guardian) {
        if(guardian.getGuardianId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "This person has a guardian, so he cannot be a guardian");
        }
    }

    private List<Long> getChildrenIdsByGuardian(Long guardianId) {
        List<Person> children = personRepository.findAllByGuardianId(guardianId);
        return children.stream()
                .map(Person::getPersonId)
                .collect(Collectors.toList());
    }

    private void checkCity(Long cityId, Long areaId) {
        City city = cityRepository.findByCityIdAndAreaId(cityId, areaId);
        if(city == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("There is no city with id %s in the area with id %s", cityId, areaId));
        }
    }

    private Person convertPersonDtoToPerson(PersonRequestDTO request) {
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

    private PersonResponseDTO convertPersonToPersonDto(Person person, List<Long> childrenIds) {
        return PersonResponseDTO.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .dateOfBirth(person.getDateOfBirth())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getEmail())
                .guardian(person.getGuardianId())
                .children(childrenIds)
                .areaId(person.getAreaId())
                .city_id(person.getCityId())
                .build();
    }

    private Person convertGuardianDtoToPerson(PersonGuardianDTO guardianDTO) {
        return Person.builder()
                .firstName(guardianDTO.getFirstName())
                .lastName(guardianDTO.getLastName())
                .dateOfBirth(guardianDTO.getDateOfBirth())
                .phoneNumber(guardianDTO.getPhoneNumber())
                .email(guardianDTO.getEmail())
                .areaId(guardianDTO.getAreaId())
                .cityId(guardianDTO.getCity_id())
                .build();
    }
}
