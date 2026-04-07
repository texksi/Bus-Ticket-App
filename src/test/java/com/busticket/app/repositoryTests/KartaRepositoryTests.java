package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KartaRepository;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.PutovanjeRepository;
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
public class KartaRepositoryTests {

    @Autowired
    private KartaRepository kartaRepository;
    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private RezervacijaRepository rezervacijaRepository;
    @Autowired
    private PutovanjeRepository putovanjeRepository;
    private Rezervacija savedRezervacija;
    private Korisnik savedKorisnik;
    private Putovanje savedPutovanje;

    private Karta builderKarta(){
        return Karta.builder()
                .brojSedista("W1")
                .osnovnaCena(100)
                .datumIzdavanja(LocalDateTime.now())
                .tip("regular")
                .rezervacija(savedRezervacija)
                .putovanje(savedPutovanje)
                .build();
    }

    @BeforeEach
    public void setup(){
        kartaRepository.deleteAll();
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
        savedPutovanje = Putovanje.builder()
                .polaziste("polaziste")
                .odrediste("odrediste")
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanija(null)
                .vozilo(null)
                .build();
        putovanjeRepository.save(savedPutovanje);
    }

    @Test
    public void getKartaById(){
        Karta saved = kartaRepository.save(builderKarta());
        Karta found = kartaRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKarte(){
        Karta saved = kartaRepository.save(builderKarta());
        List<Karta> all = kartaRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void saveKarta(){
        Karta saved = kartaRepository.save(builderKarta());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updateKarta(){
        Karta saved = kartaRepository.save(builderKarta());
        saved.setBrojSedista("1A");
        Karta updated = kartaRepository.save(saved);
        Assertions.assertThat(updated.getBrojSedista()).isEqualTo("1A");
    }

    @Test
    public void deleteKarta(){
        Karta saved = kartaRepository.save(builderKarta());
        kartaRepository.deleteById(saved.getId());
        boolean exists = kartaRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void getAllKarteForPutovanje(){
        Karta saved = kartaRepository.save(builderKarta());
        List<Karta> all = kartaRepository.findAllByPutovanjeId(saved.getPutovanje().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getAllKarteForRezervacija(){
        Karta saved = kartaRepository.save(builderKarta());
        List<Karta> all = kartaRepository.findAllByRezervacijaId(saved.getRezervacija().getId());
        Assertions.assertThat(all).hasSize(1);
    }
}
