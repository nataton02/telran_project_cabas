package de.telran.telran_project_cabas.service;

import de.telran.telran_project_cabas.dto.CityRequestDTO;
import de.telran.telran_project_cabas.dto.CityResponseDTO;

import java.util.List;

public interface CityService {
    void createCity(CityRequestDTO request);

    List<CityResponseDTO> getCities(String name);

    CityResponseDTO getCityById(Long cityId);
}
