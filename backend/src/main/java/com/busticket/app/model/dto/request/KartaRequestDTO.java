package com.busticket.app.model.dto.request;

import com.busticket.app.model.entity.enums.TipKarte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KartaRequestDTO {

    @NotBlank(message = "Broj sedista je obavezan")
    private String brojSedista;
    @NotNull(message = "Tip karte je obavezan")
    private TipKarte tip;
    @NotNull(message = "ID rezervacije je obavezan")
    private Long rezervacijaId;
    @NotNull(message = "ID putovanja je obavezan")
    private Long putovanjeId;
}
