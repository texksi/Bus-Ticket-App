package com.busticket.app.repository;

import com.busticket.app.model.entity.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima rezervacija u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD
 * operacije, kao i custom metode za pretragu rezervacija po korisniku.
 */
@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija,Long> {

    /**
     * Pronalazi sve rezervacije vezane za korisnika sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator korisnika za koga se traze rezervacije
     * @return List<Rezervacija> - lista rezervacija vezanih za datog korisnika
     */
    List<Rezervacija> findAllByKorisnikId(Long id);
}
