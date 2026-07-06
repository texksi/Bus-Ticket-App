package com.busticket.app.repository;

import com.busticket.app.model.entity.Grad;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradRepository extends JpaRepository<Grad,Long> {
    
    Optional<Grad> findByNaziv(String naziv);
    Optional<Grad> findBySkracenica(String skracenica);

    boolean existsByNaziv(String naziv);

    boolean existsBySkracenica(String skracenica);
}
