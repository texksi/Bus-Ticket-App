package com.busticket.app.serviceTests;

import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KartaRepository;
import com.busticket.app.service.KartaService;
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
public class KartaServiceTest {

    @Mock
    private KartaRepository kartaRepository;
    @InjectMocks
    private KartaService kartaService;
    private Rezervacija savedRezervacija;
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
         Korisnik savedKorisnik = Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
         savedRezervacija = Rezervacija.builder()
                .datumKreiranja(LocalDateTime.now())
                .ukupanIznos(100)
                .nacinPlacanja("Kartica")
                .status("pending")
                .korisnik(savedKorisnik).build();
         savedPutovanje = Putovanje.builder()
                .polaziste("polaziste")
                .odrediste("odrediste")
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanija(null)
                .vozilo(null)
                .build();
    }

    @Test
    public void getKartaById_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.findById(1L)).thenReturn(Optional.of(karta));
        Karta found = kartaService.getKartaById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKarte_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.findAll()).thenReturn(List.of(karta));
        List<Karta> all = kartaService.getAllKarte();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createKarta_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.save(karta)).thenReturn(karta);
        Karta saved = kartaService.createKarta(karta);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void updateKarta_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.findById(1L)).thenReturn(Optional.of(karta));
        when(kartaRepository.save(karta)).thenReturn(karta);
        Karta updated = kartaService.updateKarta(1L,"2ad","regular");
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void deleteKarta_Success(){
        Karta karta = builderKarta();
        kartaService.deleteKarta(1L);
        verify(kartaRepository).deleteById(1L);
    }

    @Test
    public void getAllKarteForPutovanje_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.findAllByPutovanjeId(karta.getPutovanje().getId())).thenReturn(List.of(karta));
        List<Karta> all = kartaService.getAllKarteForPutovanje(karta.getPutovanje().getId());
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getAllKarteForRezervacija_Success(){
        Karta karta = builderKarta();
        when(kartaRepository.findAllByRezervacijaId(karta.getRezervacija().getId())).thenReturn(List.of(karta));
        List<Karta> all = kartaService.getAllKarteForRezervacija(karta.getRezervacija().getId());
        Assertions.assertThat(all).hasSize(1);
    }
}
