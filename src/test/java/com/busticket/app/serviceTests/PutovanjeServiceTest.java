package com.busticket.app.serviceTests;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.service.PutovanjeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PutovanjeServiceTest {

    @Mock
    private PutovanjeRepository putovanjeRepository;
    @InjectMocks
    private PutovanjeService putovanjeService;
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
        savedKompanija = Kompanija.builder()
                .naziv("Kompanija")
                .kontakt("email@gmail.com")
                .build();
        savedVozilo = Vozilo.builder()
                .registracija("BG234")
                .kapacitet(32)
                .brojRedova(8)
                .brojKolona(4)
                .kompanija(savedKompanija)
                .build();
    }

    @Test
    public void getPutovanjeById_Success(){
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(putovanje));
        Putovanje found = putovanjeService.getPutovanjeById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllPutovanja_Success(){
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findAll()).thenReturn(List.of(putovanje));
        List<Putovanje> all = putovanjeService.getAllPutovanja();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createPutovanje_Success(){
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.save(putovanje)).thenReturn(putovanje);
        Putovanje saved = putovanjeService.createPutovanje(putovanje);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void updatePutovanje_Success(){
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(putovanje));
        when(putovanjeRepository.save(putovanje)).thenReturn(putovanje);
        Putovanje updated = putovanjeService.updatePutovanje(1L,"polaziste2","odredostr2",
                LocalDateTime.now().plusDays(2),LocalDateTime.now().plusDays(5),100);
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void deletePutovanje_Success(){
        Putovanje putovanje = builderPutovanje();
        putovanjeService.deletePutovanje(1L);
        verify(putovanjeRepository).deleteById(1L);
    }

    @Test
    public void getPutovanjaByKompanija_Success(){
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findAllByKompanijaId(putovanje.getKompanija().getId())).thenReturn(List.of(putovanje));
        List<Putovanje> all = putovanjeService.getPutovanjaByKompanija(putovanje.getKompanija().getId());
        Assertions.assertThat(all).hasSize(1);
    }

}
