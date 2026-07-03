package com.busticket.app.repository;

import com.busticket.app.model.entity.Ocena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima ocena u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD operacije,
 * kao i custom metode za pretragu ocena po korisniku i putovanju,i da li je korisnik vec ocenio odredjeno putovanje.
 */
@Repository
public interface OcenaRepository extends JpaRepository<Ocena,Long> {

    /**
     * Pronalazi sve ocene koje je dao korisnik sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator korisnika za koga se traze ocene
     * @return List<Ocena> - lista ocena koje je dao dati korisnik
     */
    List<Ocena> findAllByKorisnikId(Long id);

    /**
     * Pronalazi sve ocene vezane za putovanje sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator putovanja za koje se traze ocene
     * @return List<Ocena> - lista ocena vezanih za dato putovanje
     */
    List<Ocena> findAllByPutovanjeId(Long id);

    /**
     * Proverava da li je korisnik vec ocenio odredjeno putovanje
     *
     * @param korisnikId  - jedinstveni identifikator korisnika
     * @param putovanjeId - jedinstveni identifikator putovanja
     * @return boolean - true ako korisnik vec jeste ocenio dato putovanje, false ako nije
     */
    boolean existsByKorisnikIdAndPutovanjeId(Long korisnikId, Long putovanjeId);
}
