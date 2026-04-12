package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.VoziloRepository;
import com.busticket.app.service.VoziloService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoziloServiceTest {

    @Mock
    private VoziloRepository voziloRepository;
    @InjectMocks
    private VoziloService voziloService;
    private Kompanija savedKompanija;

    private Vozilo builderVozilo() {
        return Vozilo.builder()
                .registracija("bg334")
                .kapacitet(32)
                .brojRedova(4)
                .brojKolona(8)
                .kompanija(savedKompanija)
                .build();
    }

    @BeforeEach
    public void setup() {
        savedKompanija = Kompanija.builder()
                .naziv("kompanija")
                .kontakt("email@gmail.com")
                .build();
    }

    @Test
    public void getVoziloById_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(vozilo));
        Vozilo found = voziloService.getVoziloById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllVozilaForKompanija_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findAllByKompanijaId(vozilo.getKompanija().getId())).thenReturn(List.of(vozilo));
        List<Vozilo> all = voziloService.getAllVozilaForKompanija(vozilo.getKompanija().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createVozilo_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.save(vozilo)).thenReturn(vozilo);
        Vozilo saved = voziloService.createVozilo(vozilo);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void createVozilo_ThrowsExceptionWhenRegistracijaExists() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.existsByRegistracija(vozilo.getRegistracija())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> voziloService.createVozilo(vozilo))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Vozilo sa ovom registracijom vec postoji");
    }

    @Test
    public void updateVozilo_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(vozilo));
        when(voziloRepository.save(vozilo)).thenReturn(vozilo);
        Vozilo updated = voziloService.updateVozilo(1L, 40, "bgr3f", 10, 4);
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void deleteVozilo_Success() {
        Vozilo vozilo = builderVozilo();
        voziloService.deleteVozilo(1L);
        verify(voziloRepository).deleteById(1L);
    }
}
