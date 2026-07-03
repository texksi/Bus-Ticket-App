package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KompanijaRequestDTO {

    @NotBlank(message = "Naziv kompanije je obavezan")
    private String naziv;
    @NotBlank(message = "Kontakt kompanije je obavezan")
    private String kontakt;

}
