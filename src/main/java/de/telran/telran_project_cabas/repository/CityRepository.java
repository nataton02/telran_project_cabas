package de.telran.telran_project_cabas.repository;

import de.telran.telran_project_cabas.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findAllByAreaId(Long areaId);

    List<City> findAllByCityNameIgnoreCaseContaining(String name);

    City findByCityId(Long cityId);

    City findByCityIdAndAreaId(Long cityId, Long areaId);
}
