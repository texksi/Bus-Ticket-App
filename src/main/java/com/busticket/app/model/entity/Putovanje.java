package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"karte","ocene","kompanija","vozilo"})
@Table(name = "putovanje")
public class Putovanje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "polaziste", nullable = false)
    private String polaziste;
    @Column(name = "odrediste", nullable = false)
    private String odrediste;
    @Column(name = "vreme_polaska", nullable = false)
    @Builder.Default
    private LocalDateTime vremePolaska = LocalDateTime.now();
    @Column(name = "vreme_dolaska", nullable = false)
    @Builder.Default
    private LocalDateTime vremeDolaska = LocalDateTime.now();
    @Column(name = "osnovna_cena", nullable = false)
    private double osnovnaCena;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "putovanje")
    @Builder.Default
    private List<Karta> karte = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "putovanje")
    @Builder.Default
    private List<Ocena> ocene = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kompanija_id")
    private Kompanija kompanija;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vozilo_id")
    private Vozilo vozilo;
}
