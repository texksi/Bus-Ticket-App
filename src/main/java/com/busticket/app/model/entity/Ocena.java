package com.busticket.app.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Entitet koji predstavlja ocenu koju korisnik daje za određeno putovanje, ukljucujuci komentar, ocenu i veze
 * ka korisniku i putovanju.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"korisnik","putovanje"})
@Builder
@Table(name = "ocena", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"korisnik_id", "putovanje_id"})
})
public class Ocena {

    /**
     * Jedinstveni identifikator ocene, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Komentar korisnika
     */
    @Column(name = "komentar", nullable = false)
    @NotBlank
    private String komentar;
    /**
     * Numericka ocena
     */
    @Column(name = "ocena", nullable = false)
    @Positive
    private int ocena;
    /**
     * Korisnik koji je dao ocenu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;
    /**
     * Putovanje za koje je data ocena
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "putovanje_id")
    private Putovanje putovanje;
}
