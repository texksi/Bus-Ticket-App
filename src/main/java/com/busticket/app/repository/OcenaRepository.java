package com.busticket.app.repository;

import com.busticket.app.model.entity.Ocena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcenaRepository extends JpaRepository<Ocena,Long> {
}
