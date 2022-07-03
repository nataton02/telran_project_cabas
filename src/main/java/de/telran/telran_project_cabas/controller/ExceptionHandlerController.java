package de.telran.telran_project_cabas.controller;

import de.telran.telran_project_cabas.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionHandlerController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handle(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponseDTO.builder()
                        .status(ex.getStatus())
                        .message(ex.getReason())
                        .build()
                );
    }
}
