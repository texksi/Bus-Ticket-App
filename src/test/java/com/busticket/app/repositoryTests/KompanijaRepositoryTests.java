package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.repository.KompanijaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class KompanijaRepositoryTests {

    @Autowired
    private KompanijaRepository kompanijaRepository;

    private Kompanija builderKompanija(){
        return Kompanija.builder()
                .naziv("kompanija")
                .kontakt("email@gmail.com")
                .build();
    }

    @BeforeEach
    public void setup(){
        kompanijaRepository.deleteAll();
    }

    @Test
    public void getKompanijaByIdTest(){
        Kompanija saved = kompanijaRepository.save(builderKompanija());
        Kompanija found = kompanijaRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKompanijeTest(){
        Kompanija saved = kompanijaRepository.save(builderKompanija());
        List<Kompanija> all = kompanijaRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void saveKompanijaTest(){
        Kompanija saved = kompanijaRepository.save(builderKompanija());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updateKompanijaTest(){
        Kompanija saved = kompanijaRepository.save(builderKompanija());
        saved.setNaziv("novi naziv");
        Kompanija updated = kompanijaRepository.save(saved);
        Assertions.assertThat(updated.getNaziv()).isEqualTo("novi naziv");
    }

    @Test
    public void deleteKompanijaTest(){
        Kompanija saved = kompanijaRepository.save(builderKompanija());
        kompanijaRepository.deleteById(saved.getId());
        boolean exists = kompanijaRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }
}
