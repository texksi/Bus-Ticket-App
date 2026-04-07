package com.busticket.app.repositoryTests;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.RezervacijaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class RezervacijaRepositoryTests {

    @Autowired
    private RezervacijaRepository rezervacijaRepository;
    private Korisnik savedKorisnik;
    @Autowired
    private KorisnikRepository korisnikRepository;

    private Rezervacija builderRezervacija(){
        return Rezervacija.builder()
                .datumKreiranja(LocalDateTime.now())
                .ukupanIznos(100)
                .nacinPlacanja("Kartica")
                .status("pending")
                .korisnik(savedKorisnik).build();
    }

    @BeforeEach
    public void setup(){
        rezervacijaRepository.deleteAll();
        savedKorisnik = Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        korisnikRepository.save(savedKorisnik);
    }

    @Test
    public void saveRezervacijaTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void getRezervacijaByIdTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        Rezervacija found = rezervacijaRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllRezervacijeTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        List<Rezervacija> all = rezervacijaRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void updateRezervacijaTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        saved.setNacinPlacanja("GOTOVINA");
        Rezervacija updated = rezervacijaRepository.save(saved);
        Assertions.assertThat(updated.getNacinPlacanja()).isEqualTo("GOTOVINA");
    }

    @Test
    public void deleteRezervacijaTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        rezervacijaRepository.deleteById(saved.getId());
        boolean exists = rezervacijaRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();

    }

    @Test
    public void findAllRezervacijeByKorisnikTest(){
        Rezervacija saved = rezervacijaRepository.save(builderRezervacija());
        List<Rezervacija> all = rezervacijaRepository.findAllByKorisnikId(saved.getKorisnik().getId());
        Assertions.assertThat(all).hasSize(1);

    }
}
