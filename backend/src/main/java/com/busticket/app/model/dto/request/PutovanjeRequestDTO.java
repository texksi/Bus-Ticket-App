package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutovanjeRequestDTO {

    @NotNull(message = "ID polazista je obavezan")
    private Long polazisteId;
    @NotNull(message = "ID odredista je obavezno")
    private Long odredisteId;
    @NotNull(message = "Vreme polaska je obavezno")
    @FutureOrPresent(message = "Vreme polaska ne moze biti u proslosti")
    private LocalDateTime vremePolaska;
    @NotNull(message = "Vreme dolaska je obavezno")
    @FutureOrPresent(message = "Vreme dolaska ne moze biti u proslosti")
    private LocalDateTime vremeDolaska;
    @Positive(message = "Osnovna cena mora biti veca od nule")
    private double osnovnaCena;
    @NotNull(message = "ID kompanije je obavezan")
    private Long kompanijaId;
    @NotNull(message = "ID vozila je obavezno")
    private Long voziloId;

}
