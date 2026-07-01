package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacanjeRequestDTO {

    @Positive(message = "Iznos placanja mora biti pozitivan")
    private double iznos;
    @NotNull(message = "ID rezervacije je obavezan")
    private Long rezervacijaId;

}
