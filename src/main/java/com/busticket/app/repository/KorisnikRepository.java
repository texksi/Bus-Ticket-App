package com.busticket.app.repository;

import com.busticket.app.model.entity.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozitorijum za pristup podacima korisnika u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD
 * operacije, kao i custom metode za pretragu i proveru jedinstvenosti korisnika.
 */
@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik,Long> {

    /**
     * Pronalazi korisnika na osnovu prosledjenog username-a
     *
     * @param username - username korisnika na osnovu koga se vrsi pretraga
     * @return Optional<Korisnik> - objekat koji moze sadrzati pronadjenog korisnika ili biti prazan
     */
    Optional<Korisnik> findByUsername(String username);

    /**
     * Proverava da li u sistemu postoji korisnik sa prosledjenim email-om
     *
     * @param email - email cija se jedinstvenost proverava
     * @return boolean - true ako korisnik sa datim email-om postoji, false ako ne postoji
     */
    boolean existsByEmail(String email);

    /**
     * Proverava da li u sistemu postoji korisnik sa prosledjenim username-om
     *
     * @param username - username cija se jedinstvenost proverava
     * @return boolean - true ako korisnik sa datim username-om postoji, false ako ne postoji
     */
    boolean existsByUsername(String username);
}
