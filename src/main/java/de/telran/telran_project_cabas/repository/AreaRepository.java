package de.telran.telran_project_cabas.repository;

import de.telran.telran_project_cabas.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long> {

    //Area findByAreaNameIgnoreCase(String areaName);

    List<Area> findAllByAreaNameIgnoreCaseContaining(String name);
}
