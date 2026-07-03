package com.busticket.app.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoziloResponseDTO {

    private Long id;
    private String registracija;
    private int kapacitet;
    private int brojRedova;
    private int brojKolona;
    private Long kompanijaId;

}
