package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji predstavlja rezervaciju koju korisnik pravi, ukljucujuci datum kreiranja, ukupan iznos, nacin placanja,
 * status, kao i veze ka korisniku i kartama.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"korisnik","karte"})
@Table(name = "rezervacija")
public class Rezervacija {

    /**
     * Jedinstveni identifikator rezervacije, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Datum i vreme kreiranja rezervacije
     */
    @Column(name = "datum_kreiranja", nullable = false)
    @Builder.Default
    private LocalDateTime datumKreiranja = LocalDateTime.now();
    /**
     * Ukupan iznos rezervacije
     */
    @Column(name = "ukupan_iznos", nullable = false)
    private double ukupanIznos;
    /**
     * Nacin placanja
     */
    @Column(name = "nacin_placanja", nullable = false)
    private String nacinPlacanja;
    /**
     * Status rezervacije
     */
    @Column(name = "status", nullable = false)
    private String status;
    /**
     * Korisnik koji je napravio rezervaciju
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;
    /**
     * Lista karata koje pripadaju rezervaciji
     */
    @OneToMany(mappedBy = "rezervacija", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Karta> karte = new ArrayList<>();

}
