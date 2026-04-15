package com.busticket.app.model.dto.ResponseDTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacanjeResponseDTO {

    private Long id;
    private String stripePaymentId;
    private double iznos;
    private LocalDateTime datum;
    private String status;
    private Long rezervacijaId;

}
