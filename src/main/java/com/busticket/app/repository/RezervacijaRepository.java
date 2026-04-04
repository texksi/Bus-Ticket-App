package com.busticket.app.repository;

import com.busticket.app.model.entity.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija,Long> {

    List<Rezervacija> findAllByKorisnikId(Long id);
}
