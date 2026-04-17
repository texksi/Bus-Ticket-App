package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KompanijaMapper;
import com.busticket.app.model.dto.RequestDTOs.KompanijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KompanijaResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.service.KompanijaService;
import org.assertj.core.api.Assertions;
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
public class KompanijaServiceTest {

    @Mock
    private KompanijaRepository kompanijaRepository;
    @Mock
    private KompanijaMapper kompanijaMapper;
    @InjectMocks
    private KompanijaService kompanijaService;

    private Kompanija builderKompanija() {
        return Kompanija.builder()
                .naziv("kompanija")
                .kontakt("email@gmail.com")
                .build();
    }

    @Test
    public void getKompanijaById_Success() {
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(kompanija));
        when(kompanijaMapper.toResponse(kompanija)).thenReturn(new KompanijaResponseDTO());
        KompanijaResponseDTO found = kompanijaService.getKompanijaById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getKompanijaById_ThrowsExceptionWhenNotFound() {
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> kompanijaService.getKompanijaById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }

    @Test
    public void getAllKompanije_Success() {
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findAll()).thenReturn(List.of(kompanija));
        when(kompanijaMapper.toResponse(kompanija)).thenReturn(new KompanijaResponseDTO());
        List<KompanijaResponseDTO> all = kompanijaService.getAllKompanije();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createKompanija_Success() {
        KompanijaRequestDTO kompanijaRequestDTO = KompanijaRequestDTO.builder()
                .naziv("kompanija")
                .kontakt("kontakt@email.com")
                .build();
        Kompanija kompanija = builderKompanija();
        when(kompanijaMapper.toEntity(kompanijaRequestDTO)).thenReturn(kompanija);
        when(kompanijaRepository.save(kompanija)).thenReturn(kompanija);
        when(kompanijaMapper.toResponse(kompanija)).thenReturn(new KompanijaResponseDTO());
        KompanijaResponseDTO saved = kompanijaService.createKompanija(kompanijaRequestDTO);
        verify(kompanijaRepository).save(any());
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void updateKompanija_Success() {
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(kompanija));
        when(kompanijaRepository.save(kompanija)).thenReturn(kompanija);
        when(kompanijaMapper.toResponse(kompanija)).thenReturn(new KompanijaResponseDTO());
        KompanijaResponseDTO updated = kompanijaService.updateKompanija(1L, "novi naziv", "kontakt@email.com");
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void updateKompanija_ThrowsExceptionWhenNotFound() {
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> kompanijaService.updateKompanija(1L, "novi naziv",
                        "kontakt@email.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }

    @Test
    public void deleteKompanija_Success() {
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(kompanija));
        kompanijaService.deleteKompanija(1L);
        verify(kompanijaRepository).deleteById(1L);
    }

    @Test
    public void deleteKompanija_ThrowsExceptionWhenNotFound() {
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> kompanijaService.deleteKompanija(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Kompanija nije pronadjena");
    }
}
