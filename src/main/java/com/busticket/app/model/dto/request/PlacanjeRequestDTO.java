package com.busticket.app.model.dto.request;

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
