package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"rezervacija","putovanje"})
@Table(name = "karta")
public class Karta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "broj_sedista", nullable = false)
    private String brojSedista;
    @Column(name = "osnovna_cena", nullable = false)
    private double osnovnaCena;
    @Column(name = "datum_izdavanja", nullable = false)
    @Builder.Default
    private LocalDateTime datumIzdavanja = LocalDateTime.now();
    @Column(name = "tip", nullable = false)
    private String tip;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rezervacija_id")
    private Rezervacija rezervacija;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "putovanje_id")
    private Putovanje putovanje;

}
