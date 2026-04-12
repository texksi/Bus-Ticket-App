package com.busticket.app.serviceTests;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.RezervacijaRepository;
import com.busticket.app.service.RezervacijaService;
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
public class RezervacijaServiceTest {

    @Mock
    private RezervacijaRepository rezervacijaRepository;
    @InjectMocks
    private RezervacijaService rezervacijaService;
    private Korisnik savedKorisnik;

    private Rezervacija builderRezervacija(){
        return Rezervacija.builder()
                .datumKreiranja(LocalDateTime.now())
                .ukupanIznos(100)
                .nacinPlacanja("Kartica")
                .status("pending")
                .korisnik(savedKorisnik).build();
    }

    @BeforeEach
    public void setup(){
        savedKorisnik = Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    public void getRezervacijaById_Success(){
        Rezervacija rezervacija = builderRezervacija();
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.of(rezervacija));
        Rezervacija found = rezervacijaService.getRezervacijaById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllRezervacije_Success(){
        Rezervacija rezervacija = builderRezervacija();
        when(rezervacijaRepository.findAll()).thenReturn(List.of(rezervacija));
        List<Rezervacija> all = rezervacijaService.getAllRezervacije();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createRezervacija_Success(){
        Rezervacija rezervacija = builderRezervacija();
        when(rezervacijaRepository.save(rezervacija)).thenReturn(rezervacija);
        Rezervacija saved = rezervacijaService.createRezervacija(rezervacija);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void updateRezervacija_Success(){
        Rezervacija rezervacija = builderRezervacija();
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.of(rezervacija));
        when(rezervacijaRepository.save(rezervacija)).thenReturn(rezervacija);
        Rezervacija updated = rezervacijaService.updateRezervacija(1L,"novi status","gotovina",100);
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void deleteRezervacija_Success(){
        Rezervacija rezervacija = builderRezervacija();
        rezervacijaService.deleteRezervacija(1L);
        verify(rezervacijaRepository).deleteById(1L);
    }

    @Test
    public void getRezervacijeByKorisnik_Success(){
        Rezervacija rezervacija = builderRezervacija();
        when(rezervacijaRepository.findAllByKorisnikId(rezervacija.getKorisnik().getId())).thenReturn(List.of(rezervacija));
        List<Rezervacija> all = rezervacijaService.getRezervacijeByKorisnik(rezervacija.getKorisnik().getId());
        Assertions.assertThat(all).hasSize(1);
    }
}


