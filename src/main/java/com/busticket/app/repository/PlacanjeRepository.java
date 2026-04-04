package com.busticket.app.repository;

import com.busticket.app.model.entity.Placanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacanjeRepository extends JpaRepository<Placanje,Long> {
}
