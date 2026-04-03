package com.busticket.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"putovanja","vozila"})
@Table(name = "kompanija")
public class Kompanija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "naziv", nullable = false)
    private String naziv;
    @Column(name = "kontakt", nullable = false)
    private String kontakt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kompanija")
    @Builder.Default
    private List<Putovanje> putovanja = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kompanija")
    @Builder.Default
    private List<Vozilo> vozila = new ArrayList<>();
}
