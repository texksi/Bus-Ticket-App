package com.busticket.app.repository;

import com.busticket.app.model.entity.Putovanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozitorijum za pristup podacima putovanja u bazi podataka. Nasledjuje JpaRepository koji pruza osnovne CRUD
 * operacije, kao i custom metode za pretragu putovanja po kompaniji.
 */
@Repository
public interface PutovanjeRepository extends JpaRepository<Putovanje,Long> {

    /**
     * Pronalazi sva putovanja vezana za kompaniju sa prosledjenim ID-om
     *
     * @param kompanijaId - jedinstveni identifikator kompanije za koju se traze putovanja
     * @return List<Putovanje> - lista putovanja vezanih za datu kompaniju
     */
    List<Putovanje> findAllByKompanijaId(Long kompanijaId);
}
