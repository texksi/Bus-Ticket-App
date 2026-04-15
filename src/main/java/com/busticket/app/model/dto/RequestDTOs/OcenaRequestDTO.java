package com.busticket.app.model.dto.RequestDTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcenaRequestDTO {

    private String komentar;
    private int ocena;
    private Long korisnikId;
    private Long putovanjeId;

}
