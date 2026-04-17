package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entitet koji predstavlja podatke o placanju rezervacije, ukljucujuci Stripe payment ID, iznos, datum plavanja,
 * status i vezu ka rezervaciji.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = "rezervacija")
@Table(name = "placanje")
public class Placanje {

    /**
     * Jedinstveni identifikator placanja, automatski se generise u bazi podataka
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Stripe payment identifikator transakcije, mora biti jedinstven
     */
    @Column(name = "stripe_payment_id", nullable = false, unique = true)
    private String stripePaymentId;
    /**
     * Iznos uplate
     */
    @Column(name = "iznos", nullable = false)
    private double iznos;
    /**
     * Datum i vreme kada je placanje kreirano
     */
    @Column(name = "datum", nullable = false)
    @Builder.Default
    private LocalDateTime datum = LocalDateTime.now();
    /**
     * Status placanja
     */
    @Column(name = "status", nullable = false)
    private String status;
    /**
     * Rezervacija na koju se odnosi placanje
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rezervacija_id")
    private Rezervacija rezervacija;
}
