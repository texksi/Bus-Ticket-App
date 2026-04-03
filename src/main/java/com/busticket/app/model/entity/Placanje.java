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
@ToString(exclude = "rezervacija")
@Table(name = "placanje")
public class Placanje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "stripe_payment_id", nullable = false, unique = true)
    private String stripePaymentId;
    @Column(name = "iznos", nullable = false)
    private double iznos;
    @Column(name = "datum", nullable = false)
    @Builder.Default
    private LocalDateTime datum = LocalDateTime.now();
    @Column(name = "status", nullable = false)
    private String status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rezervacija_id")
    private Rezervacija rezervacija;
}
