package com.busticket.app.repository;

import com.busticket.app.model.entity.Vozilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima vozila u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD operacije,
 * kao i custom metode za pretragu vozila po kompaniji i proveru jedinstvenosti registracije.
 */
@Repository
public interface VoziloRepository extends JpaRepository<Vozilo,Long> {


    /**
     * Pronalazi sva vozila vezana za kompaniju sa prosledjenim ID-om
     *
     * @param kompanijaId - jedinstveni identifikator kompanije za koju se traze vozila
     * @return List<Vozilo> - lista vozila vezanih za datu kompaniju
     */
    List<Vozilo> findAllByKompanijaId(Long kompanijaId);


    /**
     * Proverava da li u sistemu postoji vozilo sa prosledjenom registracijom
     *
     * @param registracija - registracija vozila cija se jedinstvenost proverava
     * @return boolean - true ako vozilo sa datom registracijom postoji, false ako ne postoji
     */
    boolean existsByRegistracija(String registracija);
}
