package com.busticket.app.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcenaRequestDTO {

    private String komentar;
    @Min(value = 1, message = "Ocena mora biti najmanje 1")
    @Max(value = 5, message = "Ocena moze biti najvise 5")
    private int ocena;
    @NotNull(message = "ID korisnika je obavezan")
    private Long korisnikId;
    @NotNull(message = "ID putovanja je obavezan")
    private Long putovanjeId;

}
