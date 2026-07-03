package com.busticket.app.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Entitet koji predstavlja vozilo koje se koristi za putovanja, ukljucujuci registraciju, kapacitet i raspored sedista,
 * kao i vezu ka kompaniji kojoj pripada.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"kompanija"})
@Table(name = "vozilo")
public class Vozilo {

    /**
     * Jedinstveni identifikator vozila, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Registracioni broj vozila, mora biti jedinstven
     */
    @Column(name = "registracija", nullable = false, unique = true)
    @NotBlank
    private String registracija;
    /**
     * Ukupan kapacitet vozila (broj putnika)
     */
    @Column(name = "kapacitet", nullable = false)
    @Positive
    private int kapacitet;
    /**
     * Broj redova sedista u vozilu
     */
    @Column(name = "broj_redova", nullable = false)
    @Positive
    private int brojRedova;
    /**
     * Broj kolona sedista u vozilu
     */
    @Column(name = "broj_kolona", nullable = false)
    @Positive
    private int brojKolona;
    /**
     * Kompanija kojoj vozilo pripada
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kompanija_id")
    private Kompanija kompanija;
}
