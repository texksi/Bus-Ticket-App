package com.busticket.app.model.entity;

import com.busticket.app.model.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji sadrzi osnovne podatke o korisniku kao sto su ime, prezime, email, username i lozinka,
 * ulogu korisnika u sistemu i listu ocena koje je korisnik dao.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"ocene"})
@Table(name = "korisnik")
public class Korisnik {

    /**
     * Jedinstveni identifikator korisnika, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Ime korisnika
     */
    @Column(name = "ime", nullable = false)
    @NotBlank
    private String ime;
    /**
     * Prezime korisnika
     */
    @Column(name = "prezime", nullable = false)
    @NotBlank
    private String prezime;
    /**
     * Email adresa korisnika, mora biti jedinstvena
     */
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank
    private String email;
    /**
     * Korisnicko ime korisnika, mora biti jedinstveno
     */
    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    private String username;
    /**
     * Lozinka korisnika
     */
    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;
    /**
     * Uloga korisnika, moze biti USER ili ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull
    private Role role;
    /**
     * Lista ocena koje je korisnik dao za putovanja
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "korisnik")
    @Builder.Default
    private List<Ocena> ocene = new ArrayList<>();
}
