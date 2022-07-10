package de.telran.telran_project_cabas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonMoveRequestDTO {

    private Long personId;
    private Long fromCityId;
    private Long toCityId;
}
