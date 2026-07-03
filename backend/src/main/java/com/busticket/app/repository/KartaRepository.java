package com.busticket.app.repository;

import com.busticket.app.model.entity.Karta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima karata u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD operacije,
 * kao i custom metode za pretragu karata po rezervaciji i putovanju.
 */
@Repository
public interface KartaRepository extends JpaRepository<Karta,Long> {

    /**
     * Pronalazi sve karte vezane za rezervaciju sa prosledjenim ID-om
     *
     * @param rezervacijaId - jedinstveni identifikator rezervacije za koju se traze karte
     * @return List<Karta> - lista karata vezanih za datu rezervaciju
     */
    List<Karta> findAllByRezervacijaId(Long rezervacijaId);

    /**
     * Pronalazi sve karte vezane za putovanje sa prosledjenim ID-om
     *
     * @param putovanjeId - jedinstveni identifikator putovanja za koje se traze karte
     * @return List<Karta> - lista karata vezanih za dato putovanje
     */
    List<Karta> findAllByPutovanjeId(Long putovanjeId);
}
