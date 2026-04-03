package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"kompanija"})
@Table(name = "vozilo")
public class Vozilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "registracija", nullable = false, unique = true)
    private String registracija;
    @Column(name = "kapacitet", nullable = false)
    private int kapacitet;
    @Column(name = "broj_redova", nullable = false)
    private int brojRedova;
    @Column(name = "broj_kolona", nullable = false)
    private int brojKolona;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kompanija_id")
    private Kompanija kompanija;
}
