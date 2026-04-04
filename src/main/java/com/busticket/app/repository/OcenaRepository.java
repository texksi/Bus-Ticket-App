package com.busticket.app.repository;

import com.busticket.app.model.entity.Ocena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcenaRepository extends JpaRepository<Ocena,Long> {

    List<Ocena> findAllByKorisnikId(Long id);
    List<Ocena> findAllByPutovanjeId(Long id);
    boolean existsByKorisnikIdAndPutovanjeId(Long korisnikId, Long putovanjeId);
}
