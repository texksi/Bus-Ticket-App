package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji predstavlja putovanje i sadrzi informacije o polazistu, odredistu, vremenu polaska i dolaska, ceni
 * , kao i veze ka kartama, ocenama, kompaniji i vozilu.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"karte","ocene","kompanija","vozilo"})
@Table(name = "putovanje")
public class Putovanje {

    /**
     * Jedinstveni identifikator putovanja, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Mesto polaska putovanja
     */
    @Column(name = "polaziste", nullable = false)
    private String polaziste;
    /**
     * Mesto odredista putovanja
     */
    @Column(name = "odrediste", nullable = false)
    private String odrediste;
    /**
     * Vreme polaska putovanja
     */
    @Column(name = "vreme_polaska", nullable = false)
    @Builder.Default
    private LocalDateTime vremePolaska = LocalDateTime.now();
    /**
     * Vreme dolaska putovanja
     */
    @Column(name = "vreme_dolaska", nullable = false)
    @Builder.Default
    private LocalDateTime vremeDolaska = LocalDateTime.now();
    /**
     * Osnovna cena putovanja
     */
    @Column(name = "osnovna_cena", nullable = false)
    private double osnovnaCena;
    /**
     * Lista karata vezanih za ovo putovanje
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "putovanje")
    @Builder.Default
    private List<Karta> karte = new ArrayList<>();
    /**
     * Lista ocena za ovo putovanje
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "putovanje")
    @Builder.Default
    private List<Ocena> ocene = new ArrayList<>();
    /**
     * Kompanija koja organizuje putovanje
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kompanija_id")
    private Kompanija kompanija;
    /**
     * Vozilo koje se koristi za putovanje
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vozilo_id")
    private Vozilo vozilo;
}
