package de.telran.telran_project_cabas.controller;

import de.telran.telran_project_cabas.dto.CityRequestDTO;
import de.telran.telran_project_cabas.dto.CityResponseDTO;
import de.telran.telran_project_cabas.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/api/cities/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCity(@RequestBody CityRequestDTO request) {
        cityService.createCity(request);
    }

    @GetMapping("/api/cities/")
    public List<CityResponseDTO> readCities(
            @RequestParam(name = "name", required = false) String name) {
        return cityService.getCities(name);
    }

    @GetMapping("/api/cities/{id}/")
    public CityResponseDTO readCityById(@PathVariable("id") Long cityId) {
        return cityService.getCityById(cityId);
    }



}
