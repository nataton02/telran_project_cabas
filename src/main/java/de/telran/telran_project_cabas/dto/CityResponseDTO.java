package de.telran.telran_project_cabas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CityResponseDTO {

    private Long cityId;
    private String cityName;
    private Long areaId;
}
