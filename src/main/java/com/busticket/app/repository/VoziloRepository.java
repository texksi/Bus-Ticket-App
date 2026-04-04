package com.busticket.app.repository;

import com.busticket.app.model.entity.Vozilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoziloRepository extends JpaRepository<Vozilo,Long> {

    List<Vozilo> findAllByKompanijaId(Long kompanijaId);
    boolean existsByRegistracija(String registracija);
    Vozilo findByPutovanjeId(Long id);
}
