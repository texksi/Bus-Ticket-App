package com.busticket.app.serviceTests;

import com.busticket.app.model.entity.Placanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.PlacanjeRepository;
import com.busticket.app.service.PlacanjeService;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlacanjeServiceTest {

    @Mock
    private PlacanjeRepository placanjeRepository;
    @InjectMocks
    private PlacanjeService placanjeService;
    private Rezervacija savedRezervacija;

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
        savedRezervacija = Rezervacija.builder()
                .datumKreiranja(LocalDateTime.now())
                .ukupanIznos(100)
                .nacinPlacanja("Kartica")
                .status("pending")
                .korisnik(null).build();
    }

    @Test
    public void getPlacanjeById_Success(){
        Placanje placanje = builderPlacanje();
        when(placanjeRepository.findById(1L)).thenReturn(Optional.of(placanje));
        Placanje found = placanjeService.getPlacanjeById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getPlacanjaForRezervacija_Success(){
        Placanje placanje = builderPlacanje();
        when(placanjeRepository.findAllByRezervacijaId(placanje.getRezervacija().getId())).thenReturn(List.of(placanje));
        List<Placanje> all = placanjeService.getPlacanjaForRezervacija(placanje.getRezervacija().getId());
        Assertions.assertThat(all).hasSize(1);
    }
}
