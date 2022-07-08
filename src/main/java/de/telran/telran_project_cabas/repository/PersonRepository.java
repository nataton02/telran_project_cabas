package de.telran.telran_project_cabas.repository;

import de.telran.telran_project_cabas.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<Person> findAllByGuardianId(Long guardianId);
}
