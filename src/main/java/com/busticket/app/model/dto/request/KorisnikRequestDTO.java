package com.busticket.app.model.dto.RequestDTOs;

import com.busticket.app.model.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KorisnikRequestDTO {

    private String ime;
    private String prezime;
    private String email;
    private String username;
    private String password;
    private Role role;
}
