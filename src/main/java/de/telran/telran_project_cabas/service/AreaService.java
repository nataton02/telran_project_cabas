package de.telran.telran_project_cabas.service;

import de.telran.telran_project_cabas.dto.AreaRequestDTO;
import de.telran.telran_project_cabas.dto.AreaResponseDTO;

import java.util.List;

public interface AreaService {
    void createArea(AreaRequestDTO request);

    List<AreaResponseDTO> getAreas(String name);
}
