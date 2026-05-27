package com.busticket.app.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcenaResponseDTO {

    private Long id;
    private String komentar;
    private int ocena;
    private Long korisnikId;
    private Long putovanjeId;

}
