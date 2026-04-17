package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.VoziloMapper;
import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoziloServiceTest {

    @Mock
    private VoziloRepository voziloRepository;
    @Mock
    private VoziloMapper voziloMapper;
    @Mock
    private KompanijaRepository kompanijaRepository;
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
        when(voziloMapper.toResponse(vozilo)).thenReturn(new VoziloResponseDTO());
        VoziloResponseDTO found = voziloService.getVoziloById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getVoziloById_ThrowsExceptionWhenNotFound(){
        when(voziloRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> voziloService.getVoziloById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vozilo nije pronadjeno");
    }

    @Test
    public void getAllVozilaForKompanija_Success() {
        Vozilo vozilo = builderVozilo();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(savedKompanija));
        when(voziloRepository.findAllByKompanijaId(1L)).thenReturn(List.of(vozilo));
        when(voziloMapper.toResponse(vozilo)).thenReturn(new VoziloResponseDTO());
        List<VoziloResponseDTO> all = voziloService.getAllVozilaForKompanija(1L);
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getAllVozilaForKompanija_ThrowsExceptionWhenKompanijaNotFound(){
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> voziloService.getAllVozilaForKompanija(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }

    @Test
    public void createVozilo_Success() {
        VoziloRequestDTO voziloRequestDTO = VoziloRequestDTO.builder()
                .registracija("bg334")
                .kapacitet(32)
                .brojRedova(4)
                .brojKolona(8)
                .kompanijaId(1L)
                .build();
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.existsByRegistracija(voziloRequestDTO.getRegistracija())).thenReturn(false);
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(savedKompanija));
        when(voziloMapper.toEntity(voziloRequestDTO)).thenReturn(vozilo);
        when(voziloRepository.save(vozilo)).thenReturn(vozilo);
        when(voziloMapper.toResponse(vozilo)).thenReturn(new VoziloResponseDTO());
        VoziloResponseDTO saved = voziloService.createVozilo(voziloRequestDTO);
        verify(voziloRepository).save(any());
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void createVozilo_ThrowsExceptionWhenRegistracijaExists() {
        VoziloRequestDTO voziloRequestDTO = VoziloRequestDTO.builder()
                .registracija("bg334")
                .kapacitet(32)
                .brojRedova(4)
                .brojKolona(8)
                .kompanijaId(1L)
                .build();
        when(voziloRepository.existsByRegistracija(voziloRequestDTO.getRegistracija())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> voziloService.createVozilo(voziloRequestDTO))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Vozilo sa ovom registracijom vec postoji");
    }

    @Test
    public void createVozilo_ThrowsExceptionWhenKompanijaNotFound(){
        VoziloRequestDTO voziloRequestDTO = VoziloRequestDTO.builder()
                .registracija("bg334")
                .kapacitet(32)
                .brojRedova(4)
                .brojKolona(8)
                .kompanijaId(1L)
                .build();
        when(voziloRepository.existsByRegistracija(voziloRequestDTO.getRegistracija())).thenReturn(false);
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> voziloService.createVozilo(voziloRequestDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");

    }

    @Test
    public void updateVozilo_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(vozilo));
        when(voziloRepository.save(vozilo)).thenReturn(vozilo);
        when(voziloMapper.toResponse(vozilo)).thenReturn(new VoziloResponseDTO());
        VoziloResponseDTO updated = voziloService.updateVozilo(1L, 40, "bgr3f", 10, 4);
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void updateVozilo_ThrowsExceptionWhenVoziloNotFound(){
        when(voziloRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> voziloService.updateVozilo(1L, 40, "bgr3f", 10, 4))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vozilo nije pronadjeno");
    }

    @Test
    public void updateVozilo_ThrowsExceptionWhenRegistracijaExists(){
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(vozilo));
        when(voziloRepository.existsByRegistracija(vozilo.getRegistracija())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> voziloService.updateVozilo(1L, 40, "bg334", 10, 4))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Vozilo sa ovom registracijom vec postoji");
    }

    @Test
    public void deleteVozilo_Success() {
        Vozilo vozilo = builderVozilo();
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(vozilo));
        voziloService.deleteVozilo(1L);
        verify(voziloRepository).deleteById(1L);
    }

    @Test
    public void deleteVozilo_ThrowsExceptionWhenNotFound(){
        when(voziloRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> voziloService.deleteVozilo(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Vozilo nije pronadjeno");
    }
}
