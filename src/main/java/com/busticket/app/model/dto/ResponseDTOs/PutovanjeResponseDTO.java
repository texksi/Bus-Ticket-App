package com.busticket.app.model.dto.ResponseDTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutovanjeResponseDTO {

    private Long id;
    private String polaziste;
    private String odrediste;
    private LocalDateTime vremePolaska;
    private LocalDateTime vremeDolaska;
    private double osnovnaCena;
    private Long kompanijaId;
    private Long voziloId;

}
