package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.VoziloRepository;
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
public class PutovanjeRepositoryTests {

    @Autowired
    private PutovanjeRepository putovanjeRepository;
    @Autowired
    private KompanijaRepository kompanijaRepository;
    @Autowired
    private VoziloRepository voziloRepository;
    private Vozilo savedVozilo;
    private Kompanija savedKompanija;

    private Putovanje builderPutovanje(){
        return Putovanje.builder()
                .polaziste("polaziste")
                .odrediste("odrediste")
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanija(savedKompanija)
                .vozilo(savedVozilo)
                .build();
    }

    @BeforeEach
    public void setup(){
        putovanjeRepository.deleteAll();
        savedKompanija = Kompanija.builder()
                .naziv("Kompanija")
                .kontakt("email@gmail.com")
                .build();
        kompanijaRepository.save(savedKompanija);
        savedVozilo = Vozilo.builder()
                .registracija("BG234")
                .kapacitet(32)
                .brojRedova(8)
                .brojKolona(4)
                .kompanija(savedKompanija)
                .build();
        voziloRepository.save(savedVozilo);
    }

    @Test
    public void getPutovanjeByIdTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        Putovanje found = putovanjeRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllPutovanjaTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        List<Putovanje> all = putovanjeRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void savePutovanjeTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updatePutovanjeTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        saved.setOdrediste("novo odrediste");
        Putovanje updated = putovanjeRepository.save(saved);
        Assertions.assertThat(updated.getOdrediste()).isEqualTo("novo odrediste");
    }

    @Test
    public void deletePutovanjeTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        putovanjeRepository.deleteById(saved.getId());
        boolean exists = putovanjeRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void getPutovanjaByKompanijaTest(){
        Putovanje saved = putovanjeRepository.save(builderPutovanje());
        List<Putovanje> all = putovanjeRepository.findAllByKompanijaId(saved.getKompanija().getId());
        Assertions.assertThat(all).hasSize(1);
    }




}
