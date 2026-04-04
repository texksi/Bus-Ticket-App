package com.busticket.app.repository;

import com.busticket.app.model.entity.Kompanija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KompanijaRepository extends JpaRepository<Kompanija,Long> {
}
