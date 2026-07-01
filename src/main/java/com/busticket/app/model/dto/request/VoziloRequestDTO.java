package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoziloRequestDTO {

    @NotBlank(message = "Registracija je obavezna")
    private String registracija;
    @Positive(message = "Kapacitet mora biti veci od nule")
    private int kapacitet;
    @Positive(message = "Broj redova mora biti veci od nule")
    private int brojRedova;
    @Positive(message = "Broj kolona mora biti veci od nule")
    private int brojKolona;
    @NotNull(message = "ID kompanije je obavezan")
    private Long kompanijaId;
}
