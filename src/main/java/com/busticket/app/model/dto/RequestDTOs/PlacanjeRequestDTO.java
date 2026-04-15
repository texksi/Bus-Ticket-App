package com.busticket.app.model.dto.RequestDTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacanjeRequestDTO {

    private double iznos;
    private Long rezervacijaId;

}
