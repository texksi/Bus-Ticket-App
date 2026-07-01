package com.busticket.app.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutovanjeResponseDTO {

    private Long id;
    private Long polazisteId;
    private Long odredisteId;
    private LocalDateTime vremePolaska;
    private LocalDateTime vremeDolaska;
    private double osnovnaCena;
    private Long kompanijaId;
    private Long voziloId;

}
