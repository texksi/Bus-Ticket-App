package com.busticket.app.serviceTests;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KompanijaServiceTest {

    @Mock
    private KompanijaRepository kompanijaRepository;
    @InjectMocks
    private KompanijaService kompanijaService;

    private Kompanija builderKompanija(){
        return Kompanija.builder()
                .naziv("kompanija")
                .kontakt("email@gmail.com")
                .build();
    }

    @Test
    public void getKompanijaById_Success(){
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(kompanija));
        Kompanija found = kompanijaService.getKompanijaById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKompanije_Success(){
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findAll()).thenReturn(List.of(kompanija));
        List<Kompanija> all = kompanijaService.getAllKompanije();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createKompanija_Success(){
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.save(kompanija)).thenReturn(kompanija);
        Kompanija saved = kompanijaService.createKompanija(kompanija);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void updateKompanija_Success(){
        Kompanija kompanija = builderKompanija();
        when(kompanijaRepository.findById(1L)).thenReturn(Optional.of(kompanija));
        when(kompanijaRepository.save(kompanija)).thenReturn(kompanija);
        Kompanija updated = kompanijaService.updateKompanija(1L,"novi naziv","kontakt@email.com");
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void deleteKompanija_Success(){
        Kompanija kompanija = builderKompanija();
        kompanijaService.deleteKompanija(1L);
        verify(kompanijaRepository).deleteById(1L);
    }
}
