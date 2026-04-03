package com.busticket.app.model.entity;

import com.busticket.app.model.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"ocene"})
@Table(name = "korisnik")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ime",nullable = false)
    private String ime;
    @Column(name = "prezime",nullable = false)
    private String prezime;
    @Column(name = "email",nullable = false, unique = true)
    private String email;
    @Column(name = "username",nullable = false, unique = true)
    private String username;
    @Column(name = "password",nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "korisnik")
    @Builder.Default
    private List<Ocena> ocene = new ArrayList<>();
}
