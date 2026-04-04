package com.busticket.app.repository;

import com.busticket.app.model.entity.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik,Long> {

    Optional<Korisnik> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
