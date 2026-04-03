package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "komentar", nullable = false)
    private String komentar;
    @Column(name = "ocena", nullable = false)
    private int ocena;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "putovanje_id")
    private Putovanje putovanje;
}
