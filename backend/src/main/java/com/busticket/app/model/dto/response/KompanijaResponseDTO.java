package com.busticket.app.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KompanijaResponseDTO {

    private Long id;
    private String naziv;
    private String kontakt;

}
