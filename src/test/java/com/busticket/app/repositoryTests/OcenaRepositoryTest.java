package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.OcenaRepository;
import com.busticket.app.repository.PutovanjeRepository;
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
public class OcenaRepositoryTest {

    @Autowired
    private OcenaRepository ocenaRepository;
    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private PutovanjeRepository putovanjeRepository;
    private Korisnik savedKorisnik;
    private Putovanje savedPutovanje;

    private Ocena builderOcena(){
        return Ocena.builder()
                .komentar("komentar")
                .ocena(4)
                .korisnik(savedKorisnik)
                .putovanje(savedPutovanje)
                .build();
    }

    @BeforeEach
    public void setup(){
        ocenaRepository.deleteAll();
        savedKorisnik =  Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        korisnikRepository.save(savedKorisnik);
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
    public void getOceneByKorisnikTest(){
        Ocena saved = ocenaRepository.save(builderOcena());
        List<Ocena> all = ocenaRepository.findAllByKorisnikId(saved.getKorisnik().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getOceneForPutovanjeTest(){
        Ocena saved = ocenaRepository.save(builderOcena());
        List<Ocena> all = ocenaRepository.findAllByPutovanjeId(saved.getPutovanje().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void saveOcenaTest(){
        Ocena saved = ocenaRepository.save(builderOcena());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void deleteOcenaTest(){
        Ocena saved = ocenaRepository.save(builderOcena());
        ocenaRepository.deleteById(saved.getId());
        boolean exists = ocenaRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }
}
