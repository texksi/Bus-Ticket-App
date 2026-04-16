package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.mapper.OcenaMapper;
import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.OcenaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.service.OcenaService;
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
public class OcenaServiceTest {

    @Mock
    private OcenaRepository ocenaRepository;
    @Mock
    private OcenaMapper ocenaMapper;
    @Mock
    private KorisnikRepository korisnikRepository;
    @Mock
    private PutovanjeRepository putovanjeRepository;
    @InjectMocks
    private OcenaService ocenaService;

    private Korisnik savedKorisnik;
    private Putovanje savedPutovanje;

    private Ocena builderOcena(){
        return Ocena.builder()
                .komentar("komentar")
                .ocena(4)
                .korisnik(savedKorisnik)
                .putovanje(savedPutovanje)
                .build();
    }

    @BeforeEach
    public void setup(){
        savedKorisnik =  Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
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
    public void getOceneByKorisnik_Success(){
        Ocena ocena = builderOcena();
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(savedKorisnik));
        when(ocenaRepository.findAllByKorisnikId(1L)).thenReturn(List.of(ocena));
        when(ocenaMapper.toResponse(ocena)).thenReturn(new OcenaResponseDTO());
        List<OcenaResponseDTO> all = ocenaService.getOceneByKorisnik(1L);
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void getOceneForPutovanje_Success(){
        Ocena ocena = builderOcena();
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(savedPutovanje));
        when(ocenaRepository.findAllByPutovanjeId(1L)).thenReturn(List.of(ocena));
        when(ocenaMapper.toResponse(ocena)).thenReturn(new OcenaResponseDTO());
        List<OcenaResponseDTO> all = ocenaService.getOceneForPutovanje(1L);
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createOcena_Success(){
        OcenaRequestDTO ocenaRequestDTO = OcenaRequestDTO.builder()
                .komentar("komentar")
                .ocena(4)
                .korisnikId(1L)
                .putovanjeId(1L)
                .build();
        Ocena ocena = builderOcena();
        when(ocenaMapper.toEntity(ocenaRequestDTO)).thenReturn(ocena);
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(savedKorisnik));
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(savedPutovanje));
        when(ocenaRepository.save(ocena)).thenReturn(ocena);
        when(ocenaMapper.toResponse(ocena)).thenReturn(new OcenaResponseDTO());
        OcenaResponseDTO saved = ocenaService.createOcena(ocenaRequestDTO);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void createOcena_ThrowsExceptionWhenAlreadyRated(){
        OcenaRequestDTO ocenaRequestDTO = OcenaRequestDTO.builder()
                .komentar("komentar")
                .ocena(4)
                .korisnikId(1L)
                .putovanjeId(1L)
                .build();
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(savedKorisnik));
        when(putovanjeRepository.findById(1L)).thenReturn(Optional.of(savedPutovanje));
        when(ocenaRepository.existsByKorisnikIdAndPutovanjeId(ocenaRequestDTO.getKorisnikId(),ocenaRequestDTO.getPutovanjeId()))
                .thenReturn(true);
        Assertions.assertThatThrownBy(() -> ocenaService.createOcena(ocenaRequestDTO))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Korisnik je već ocenio ovo putovanje");
    }

    @Test
    public void deleteOcena_Success(){
        Ocena ocena = builderOcena();
        when(ocenaRepository.findById(1L)).thenReturn(Optional.of(ocena));
        ocenaService.deleteOcena(1L);
        verify(ocenaRepository).deleteById(1L);
    }
}
