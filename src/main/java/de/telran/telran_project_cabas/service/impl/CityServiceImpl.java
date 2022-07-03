package de.telran.telran_project_cabas.service.impl;

import de.telran.telran_project_cabas.dto.CityRequestDTO;
import de.telran.telran_project_cabas.dto.CityResponseDTO;
import de.telran.telran_project_cabas.entity.City;
import de.telran.telran_project_cabas.repository.CityRepository;
import de.telran.telran_project_cabas.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void createCity(CityRequestDTO request) {
        cityRepository.save(convertDtoToCity(request));
    }

    @Override
    public List<CityResponseDTO> getCities(String name) {
        if(name == null) {
            return cityRepository.findAll()
                    .stream()
                    .map(this::convertCityToDto)
                    .collect(Collectors.toList());
        }

        return cityRepository.findAllByCityNameIgnoreCaseContaining(name)
                .stream()
                .map(this::convertCityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityResponseDTO getCityById(Long cityId) {
        City city = cityRepository.findByCityId(cityId);
        if(city == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("City with id %s does not exist", cityId)
            );
        }
        return convertCityToDto(cityRepository.findByCityId(cityId));

    }

    private CityResponseDTO convertCityToDto(City city) {
        return CityResponseDTO.builder()
                .cityId(city.getCityId())
                .cityName(city.getCityName())
                .areaId(city.getAreaId())
                .build();
    }

    private City convertDtoToCity(CityRequestDTO request) {
        return City.builder()
                .cityName(request.getCityName())
                .areaId(request.getAreaId())
                .build();
    }
}
