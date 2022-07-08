package de.telran.telran_project_cabas.service.impl;

import de.telran.telran_project_cabas.dto.AreaRequestDTO;
import de.telran.telran_project_cabas.dto.AreaResponseDTO;
import de.telran.telran_project_cabas.dto.CityResponseDTO;
import de.telran.telran_project_cabas.entity.Area;
import de.telran.telran_project_cabas.entity.City;
import de.telran.telran_project_cabas.repository.AreaRepository;
import de.telran.telran_project_cabas.repository.CityRepository;
import de.telran.telran_project_cabas.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void createArea(AreaRequestDTO request) {
        areaRepository.save(converterAreaDtoToArea(request));
    }

    @Override
    public List<AreaResponseDTO> getAreas(String name) {
        if(name == null) {
            return getResponseByAreas(areaRepository.findAll());
        }

        return getResponseByAreas(areaRepository.findAllByAreaNameIgnoreCaseContaining(name));
    }

    private List<AreaResponseDTO> getResponseByAreas(List<Area> areas) {
        return areas.stream()
                .map(area -> {
                    List<Long> citiesIds = cityRepository.findAllByAreaId(area.getAreaId())
                            .stream()
                            .map(City::getCityId)
                            .collect(Collectors.toList());
                    return convertAreaToAreaDto(area, citiesIds);
                })
                .collect(Collectors.toList());
    }

    private AreaResponseDTO convertAreaToAreaDto(Area area, List<Long> citiesIds) {
        return AreaResponseDTO.builder()
                .areaId(area.getAreaId())
                .areaName(area.getAreaName())
                .citiesIds(citiesIds)
                .build();
    }

    private Area converterAreaDtoToArea(AreaRequestDTO request) {
        return Area.builder()
                .areaName(request.getAreaName())
                .build();
    }
}
