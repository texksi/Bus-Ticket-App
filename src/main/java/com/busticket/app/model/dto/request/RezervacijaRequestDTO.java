package com.busticket.app.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RezervacijaRequestDTO {

    private String nacinPlacanja;
    private String status;
    private double ukupanIznos;
    private Long korisnikId;
}
