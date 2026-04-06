package com.busticket.app.repositoryTests;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class KorisnikRepositoryTests {

    @Autowired
    private KorisnikRepository korisnikRepository;

    public Korisnik builderKorisnik(){
        return Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
    }

    @BeforeEach
    public void setup(){
        korisnikRepository.deleteAll();
    }

    @Test
    public void saveKorisnikTest(){
        Korisnik korisnik = builderKorisnik();
        Korisnik saved = korisnikRepository.save(korisnik);
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void getKorisnikByIdTest(){
        Korisnik saved = korisnikRepository.save(builderKorisnik());
        Korisnik found = korisnikRepository.findById(saved.getId()).orElseThrow(() -> new
                EntityNotFoundException("Korisnik ne postoji"));
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getKorisnikByUsernameTest(){
        Korisnik saved = korisnikRepository.save(builderKorisnik());
        Korisnik found = korisnikRepository.findByUsername(saved.getUsername()).orElseThrow(() -> new
                EntityNotFoundException("Korisnik ne postoji"));
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKorisniciTest(){
        Korisnik saved = korisnikRepository.save(builderKorisnik());
        List<Korisnik> all = korisnikRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void updateKorisnikTest(){
        Korisnik saved = korisnikRepository.save(builderKorisnik());
        saved.setUsername("NEW USERNAME");
        Korisnik updated = korisnikRepository.save(saved);
        Assertions.assertThat(updated.getUsername()).isEqualTo("NEW USERNAME");
    }

    @Test
    public void deleteKorisnik(){
        Korisnik saved = korisnikRepository.save(builderKorisnik());
        korisnikRepository.deleteById(saved.getId());
        boolean exists = korisnikRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }

}
