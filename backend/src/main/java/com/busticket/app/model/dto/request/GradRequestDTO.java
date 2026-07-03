package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradRequestDTO {

    @NotBlank(message = "Naziv grada je obavezan")
    private String naziv;
    @NotBlank(message = "Skracenica za grad je obavezna")
    private String skracenica;
}
