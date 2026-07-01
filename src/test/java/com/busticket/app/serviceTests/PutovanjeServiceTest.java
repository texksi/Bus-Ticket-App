package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PutovanjeMapper;
import com.busticket.app.model.dto.request.PutovanjeRequestDTO;
import com.busticket.app.model.dto.response.PutovanjeResponseDTO;
import com.busticket.app.model.entity.Grad;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.GradRepository;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.VoziloRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PutovanjeServiceTest {

    @Mock
    private PutovanjeRepository putovanjeRepository;
    @Mock
    private PutovanjeMapper putovanjeMapper;
    @Mock
    private VoziloRepository voziloRepository;
    @Mock
    private KompanijaRepository kompanijaRepository;
    @Mock
    private GradRepository gradRepository;
    @InjectMocks
    private PutovanjeService putovanjeService;
    private Vozilo savedVozilo;
    private Kompanija savedKompanija;

    private Putovanje builderPutovanje() {
        Grad polaziste = Grad.builder().naziv("Beograd").skracenica("BG").build();
        Grad odrediste = Grad.builder().naziv("Nis").skracenica("NI").build();
        return Putovanje.builder()
                .polaziste(polaziste)
                .odrediste(odrediste)
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanija(savedKompanija)
                .vozilo(savedVozilo)
                .build();
    }

    @BeforeEach
    public void setup() {
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
    public void getPutovanjeById_Success() {
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(putovanje));
        when(putovanjeMapper.toResponse(putovanje)).thenReturn(new PutovanjeResponseDTO());
        PutovanjeResponseDTO found = putovanjeService.getPutovanjeById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getPutovanjeById_ThrowsExceptionWhenNotFound() {
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.getPutovanjeById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Putovanje nije pronadjeno");
    }

    @Test
    public void getAllPutovanja_Success() {
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findAll()).thenReturn(List.of(putovanje));
        when(putovanjeMapper.toResponse(putovanje)).thenReturn(new PutovanjeResponseDTO());
        List<PutovanjeResponseDTO> all = putovanjeService.getAllPutovanja();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createPutovanje_Success() {
        PutovanjeRequestDTO putovanjeRequestDTO = PutovanjeRequestDTO.builder()
                .polazisteId(1L)
                .odredisteId(2L)
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanijaId(1L)
                .voziloId(1L)
                .build();
        Putovanje putovanje = builderPutovanje();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(savedVozilo));
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(savedKompanija));
        Grad polaziste = Grad.builder().naziv("Beograd").skracenica("BG").build();
        Grad odrediste = Grad.builder().naziv("Nis").skracenica("NI").build();
        when(gradRepository.findById(1L)).thenReturn(Optional.of(polaziste));
        when(gradRepository.findById(2L)).thenReturn(Optional.of(odrediste));
        when(putovanjeMapper.toEntity(putovanjeRequestDTO)).thenReturn(putovanje);
        when(putovanjeRepository.save(putovanje)).thenReturn(putovanje);
        when(putovanjeMapper.toResponse(putovanje)).thenReturn(new PutovanjeResponseDTO());
        PutovanjeResponseDTO saved = putovanjeService.createPutovanje(putovanjeRequestDTO);
        verify(putovanjeRepository).save(any());
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void createPutovanje_ThrowsExceptionWhenVoziloNotFound() {
        PutovanjeRequestDTO putovanjeRequestDTO = PutovanjeRequestDTO.builder()
                .polazisteId(1L)
                .odredisteId(2L)
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanijaId(1L)
                .voziloId(1L)
                .build();
        when(voziloRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.createPutovanje(putovanjeRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vozilo nije pronadjeno");
    }

    @Test
    public void createPutovanje_ThrowsExceptionWhenKompanijaNotFound() {
        PutovanjeRequestDTO putovanjeRequestDTO = PutovanjeRequestDTO.builder()
                .polazisteId(1L)
                .odredisteId(2L)
                .vremePolaska(LocalDateTime.now())
                .vremeDolaska(LocalDateTime.now().plusDays(4))
                .osnovnaCena(100)
                .kompanijaId(1L)
                .voziloId(1L)
                .build();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(savedVozilo));
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.createPutovanje(putovanjeRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }

    @Test
    public void updatePutovanje_Success() {
        Grad polaziste = Grad.builder().naziv("Beograd").skracenica("BG").build();
        Grad odrediste = Grad.builder().naziv("Nis").skracenica("NI").build();
        PutovanjeRequestDTO dto = PutovanjeRequestDTO.builder()
                .polazisteId(1L)
                .odredisteId(2L)
                .vremePolaska(LocalDateTime.now().plusDays(2))
                .vremeDolaska(LocalDateTime.now().plusDays(5))
                .osnovnaCena(100)
                .build();
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(putovanje));
        when(gradRepository.findById(1L)).thenReturn(Optional.of(polaziste));
        when(gradRepository.findById(2L)).thenReturn(Optional.of(odrediste));
        when(putovanjeRepository.save(putovanje)).thenReturn(putovanje);
        when(putovanjeMapper.toResponse(putovanje)).thenReturn(new PutovanjeResponseDTO());
        PutovanjeResponseDTO updated = putovanjeService.updatePutovanje(1L, dto);
        Assertions.assertThat(updated).isNotNull();
    }


    @Test
    public void updatePutovanje_ThrowsExceptionWhenNotFound() {
        PutovanjeRequestDTO dto = PutovanjeRequestDTO.builder()
                .polazisteId(1L)
                .odredisteId(2L)
                .vremePolaska(LocalDateTime.now().plusDays(2))
                .vremeDolaska(LocalDateTime.now().plusDays(5))
                .osnovnaCena(100)
                .build();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.updatePutovanje(1L, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Putovanje nije pronadjeno");
    }

    @Test
    public void deletePutovanje_Success() {
        Putovanje putovanje = builderPutovanje();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(putovanje));
        putovanjeService.deletePutovanje(1L);
        verify(putovanjeRepository).deleteById(1L);
    }

    @Test
    public void deletePutovanje_ThrowsExceptionWhenNotFound() {
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.deletePutovanje(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Putovanje nije pronadjeno");
    }

    @Test
    public void getPutovanjaByKompanija_Success() {
        Putovanje putovanje = builderPutovanje();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(savedKompanija));
        when(putovanjeRepository.findAllByKompanijaId(1L)).thenReturn(List.of(putovanje));
        when(putovanjeMapper.toResponse(putovanje)).thenReturn(new PutovanjeResponseDTO());
        List<PutovanjeResponseDTO> all = putovanjeService.getPutovanjaByKompanija(1L);
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getPutovanjaByKompanija_ThrowsExceptionWhenKompanijaNotFound() {
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> putovanjeService.getPutovanjaByKompanija(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }

}
