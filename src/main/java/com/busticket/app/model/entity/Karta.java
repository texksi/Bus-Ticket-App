package com.busticket.app.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entitet koji sadrzi podatke o karti kao sto su broj sedista, osnovna cena, datum izdavanja i tip karte, kao i
 * referencu na rezervaciju i putovanje za koje je karta izdata.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"rezervacija","putovanje"})
@Table(name = "karta")
public class Karta {

    /**
     * Jedinstveni identifikator karte, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Broj sedista na karti
     */
    @Column(name = "broj_sedista", nullable = false)
    @NotBlank
    private String brojSedista;
    /**
     * Osnovna cena karte
     */
    @Column(name = "osnovna_cena", nullable = false)
    @Positive
    private double osnovnaCena;
    /**
     * Datum i vreme izdavanja karte
     */
    @Column(name = "datum_izdavanja", nullable = false)
    @Builder.Default
    private LocalDateTime datumIzdavanja = LocalDateTime.now();
    /**
     * Tip karte (student,regular...)
     */
    @Column(name = "tip", nullable = false)
    @NotBlank
    private String tip;
    /**
     * Rezervacija za koju je karta izdata
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rezervacija_id")
    private Rezervacija rezervacija;
    /**
     * Putovanje za koje je karta izdata
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "putovanje_id")
    private Putovanje putovanje;

}
