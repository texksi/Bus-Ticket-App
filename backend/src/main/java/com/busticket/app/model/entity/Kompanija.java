package com.busticket.app.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji predstavlja kompaniju i sadrzi osnovne informacije o kompaniji naziv, kontakt, kao i listu putovanja
 * i vozila koja joj pripadaju.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"putovanja","vozila"})
@Table(name = "kompanija")
public class Kompanija {

    /**
     * Jedinstveni identifikator kompanije, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Naziv kompanije
     */
    @Column(name = "naziv", nullable = false)
    @NotBlank
    private String naziv;
    /**
     * Kontakt kompanije
     */
    @Column(name = "kontakt", nullable = false)
    @NotBlank
    private String kontakt;

    /**
     * Lista putovanja koja pripadaju kompaniji
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kompanija")
    @Builder.Default
    private List<Putovanje> putovanja = new ArrayList<>();
    /**
     * Lista vozila koja pripadaju kompaniji
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kompanija")
    @Builder.Default
    private List<Vozilo> vozila = new ArrayList<>();
}
