package com.busticket.app.repository;

import com.busticket.app.model.entity.Placanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacanjeRepository extends JpaRepository<Placanje,Long> {

    List<Placanje> findAllByRezervacijaId(Long id);
}
