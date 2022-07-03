package de.telran.telran_project_cabas.controller;

import de.telran.telran_project_cabas.dto.AreaRequestDTO;
import de.telran.telran_project_cabas.dto.AreaResponseDTO;
import de.telran.telran_project_cabas.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping("/api/areas")
    @ResponseStatus(HttpStatus.CREATED)
    public void createArea(@RequestBody AreaRequestDTO request) {
        areaService.createArea(request);
    }

    @GetMapping("/api/areas")
    public List<AreaResponseDTO> readAreas(
            @RequestParam(name = "name", required = false) String name) {
        return areaService.getAreas(name);
    }
}
