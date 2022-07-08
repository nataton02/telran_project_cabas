package de.telran.telran_project_cabas.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonUpdateDTO {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
