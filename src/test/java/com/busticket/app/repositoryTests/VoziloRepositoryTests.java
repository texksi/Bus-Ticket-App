package com.busticket.app.repositoryTests;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.VoziloRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class VoziloRepositoryTests {

    @Autowired
    private VoziloRepository voziloRepository;
    @Autowired
    private KompanijaRepository kompanijaRepository;
    private Kompanija savedKompanija;

    private Vozilo builderVozilo(){
        return Vozilo.builder()
                .registracija("bg334")
                .kapacitet(32)
                .brojRedova(4)
                .brojKolona(8)
                .kompanija(savedKompanija)
                .build();
    }

    @BeforeEach
    public void setup(){
        voziloRepository.deleteAll();
        savedKompanija = Kompanija.builder()
                .naziv("kompanija")
                .kontakt("email@gmail.com")
                .build();
        kompanijaRepository.save(savedKompanija);
    }

    @Test
    public void getVoziloByIdTest(){
        Vozilo saved = voziloRepository.save(builderVozilo());
        Vozilo found = voziloRepository.findById(saved.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllVozilaForKompanijaTest(){
        Vozilo saved = voziloRepository.save(builderVozilo());
        List<Vozilo> all = voziloRepository.findAllByKompanijaId(saved.getKompanija().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void saveVoziloTest(){
        Vozilo saved = voziloRepository.save(builderVozilo());
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updateVoziloTest(){
        Vozilo saved = voziloRepository.save(builderVozilo());
        saved.setRegistracija("bg111");
        Vozilo updated = voziloRepository.save(saved);
        Assertions.assertThat(updated.getRegistracija()).isEqualTo("bg111");
    }

    @Test
    public void deleteVoziloTest(){
        Vozilo saved = voziloRepository.save(builderVozilo());
        voziloRepository.deleteById(saved.getId());
        boolean exists = voziloRepository.existsById(saved.getId());
        Assertions.assertThat(exists).isFalse();
    }
}
