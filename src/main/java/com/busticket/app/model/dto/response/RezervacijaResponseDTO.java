package com.busticket.app.model.dto.response;

import com.busticket.app.model.entity.enums.NacinPlacanja;
import com.busticket.app.model.entity.enums.StatusRezervacije;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RezervacijaResponseDTO {

    private Long id;
    private LocalDateTime datumKreiranja;
    private double ukupanIznos;
    private NacinPlacanja nacinPlacanja;
    private StatusRezervacije status;
    private Long korisnikId;

}
