package com.busticket.app.model.dto.ResponseDTOs;

import com.busticket.app.model.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KorisnikResponseDTO {

    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private String username;
    private Role role;
}
