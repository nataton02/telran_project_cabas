package de.telran.telran_project_cabas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonResponseDTO {

    private Long personId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private PersonResponseDTO guardian;
    private List<PersonResponseDTO> children;
}
