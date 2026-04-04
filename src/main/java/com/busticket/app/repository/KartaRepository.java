package com.busticket.app.repository;

import com.busticket.app.model.entity.Karta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KartaRepository extends JpaRepository<Karta,Long> {
}
