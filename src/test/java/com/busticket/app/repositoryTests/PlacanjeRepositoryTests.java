package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.PlacanjeRepository;
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
public class PlacanjeRepositoryTests {

    @Autowired
    private PlacanjeRepository placanjeRepository;
    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private RezervacijaRepository rezervacijaRepository;
    private Rezervacija savedRezervacija;
    private Korisnik savedKorisnik;

    private Placanje builderPlacanje(){
        return Placanje.builder()
                .stripePaymentId("stripeId")
                .iznos(100)
                .datum(LocalDateTime.now())
                .status("pending")
                .rezervacija(savedRezervacija)
                .build();
    }

    @BeforeEach
    public void setup(){
        placanjeRepository.deleteAll();
        savedKorisnik = Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        korisnikRepository.save(savedKorisnik);
        savedRezervacija = Rezervacija.builder()
                .datumKreiranja(LocalDateTime.now())
                .ukupanIznos(100)
                .nacinPlacanja("Kartica")
                .status("pending")
                .korisnik(savedKorisnik).build();
        rezervacijaRepository.save(savedRezervacija);
    }

    @Test
    public void getPlacanjeByIdTest(){
        Placanje saved = placanjeRepository.save(builderPlacanje());
        Placanje found = placanjeRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getPlacanjaForRezervacijaTest(){
        Placanje saved = placanjeRepository.save(builderPlacanje());
        List<Placanje> all = placanjeRepository.findAllByRezervacijaId(saved.getRezervacija().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void savePlacanjeTest(){
        Placanje saved = placanjeRepository.save(builderPlacanje());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void deletePlacanjeTest(){
        Placanje saved = placanjeRepository.save(builderPlacanje());
        placanjeRepository.deleteById(saved.getId());
        boolean exists = placanjeRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }
}
