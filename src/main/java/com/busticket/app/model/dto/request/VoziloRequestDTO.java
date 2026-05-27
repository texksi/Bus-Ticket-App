package com.busticket.app.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoziloRequestDTO {

    private String registracija;
    private int kapacitet;
    private int brojRedova;
    private int brojKolona;
    private Long kompanijaId;

}
