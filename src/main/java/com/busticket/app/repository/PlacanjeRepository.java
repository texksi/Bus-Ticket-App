package com.busticket.app.repository;

import com.busticket.app.model.entity.Placanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima placanja u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD
 * operacije, kao i custom metode za pretragu placanja po rezervaciji.
 */
@Repository
public interface PlacanjeRepository extends JpaRepository<Placanje,Long> {

    /**
     * Pronalazi sva placanja vezana za rezervaciju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator rezervacije za koju se traze placanja
     * @return List<Placanje> - lista placanja vezanih za datu rezervaciju
     */
    List<Placanje> findAllByRezervacijaId(Long id);
}
