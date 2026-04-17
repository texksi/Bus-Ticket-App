package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PlacanjeMapper;
import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.PlacanjeRepository;
import com.busticket.app.repository.RezervacijaRepository;
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
    @Mock
    private PlacanjeMapper placanjeMapper;
    @Mock
    private RezervacijaRepository rezervacijaRepository;
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
        when(placanjeMapper.toResponse(placanje)).thenReturn(new PlacanjeResponseDTO());
        PlacanjeResponseDTO found = placanjeService.getPlacanjeById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getPlacanjeById_ThrowsExceptionWhenNotFound(){
        when(placanjeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> placanjeService.getPlacanjeById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Placanje nije pronadjeno");
    }

    @Test
    public void getPlacanjaForRezervacija_Success(){
        Placanje placanje = builderPlacanje();
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.of(new Rezervacija()));
        when(placanjeRepository.findAllByRezervacijaId(1L)).thenReturn(List.of(placanje));
        when(placanjeMapper.toResponse(placanje)).thenReturn(new PlacanjeResponseDTO());
        List<PlacanjeResponseDTO> all = placanjeService.getPlacanjaForRezervacija(1L);
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getPlacanjaForRezervacija_ThrowsExceptionWhenRezervacijaNotFound(){
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> placanjeService.getPlacanjaForRezervacija(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Rezervacija nije pronadjena");
    }
}
