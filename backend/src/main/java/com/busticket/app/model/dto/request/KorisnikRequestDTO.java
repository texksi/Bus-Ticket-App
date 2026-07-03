package com.busticket.app.model.dto.request;

import com.busticket.app.model.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KorisnikRequestDTO {

    @NotBlank(message = "Ime korisnika je obavezno")
    private String ime;
    @NotBlank(message = "Prezime korisnika je obavezno")
    private String prezime;
    @Email(message = "Email nije ispravnog formata")
    @NotBlank(message = "Email korisnika je obavezan")
    private String email;
    @NotBlank(message = "Username korisnika je obavezan")
    private String username;
    @NotBlank(message = "Password korisnika je obavezan")
    private String password;
    @NotNull(message = "Role korisnika je obavezan")
    private Role role;
}
