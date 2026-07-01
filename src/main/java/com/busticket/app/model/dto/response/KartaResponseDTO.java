package com.busticket.app.model.dto.response;

import com.busticket.app.model.entity.enums.TipKarte;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KartaResponseDTO {

    private Long id;
    private String brojSedista;
    private LocalDateTime datumIzdavanja;
    private TipKarte tip;
    private double finalnaCena;
    private Long rezervacijaId;
    private Long putovanjeId;
}
