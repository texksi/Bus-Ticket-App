package com.busticket.app.model.dto.request;

import com.busticket.app.model.entity.enums.NacinPlacanja;
import com.busticket.app.model.entity.enums.StatusRezervacije;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RezervacijaRequestDTO {

    @NotNull(message = "Nacin placanja je obavezan")
    private NacinPlacanja nacinPlacanja;
    @NotNull(message = "Status je obavezan")
    private StatusRezervacije status;
    @NotNull(message = "ID korisnika je obavezan")
    private Long korisnikId;
}
