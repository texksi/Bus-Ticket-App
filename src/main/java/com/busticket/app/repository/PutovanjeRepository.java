package com.busticket.app.repository;

import com.busticket.app.model.entity.Putovanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PutovanjeRepository extends JpaRepository<Putovanje,Long> {
}
