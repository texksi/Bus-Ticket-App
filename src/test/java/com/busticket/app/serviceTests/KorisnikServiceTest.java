package com.busticket.app.serviceTests;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.mapper.KorisnikMapper;
import com.busticket.app.model.dto.RequestDTOs.KorisnikRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KorisnikResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.enums.Role;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.service.KorisnikService;
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
public class KorisnikServiceTest {

    @Mock
    private KorisnikRepository korisnikRepository;
    @Mock
    private KorisnikMapper korisnikMapper;
    @InjectMocks
    private KorisnikService korisnikService;

    public Korisnik builderKorisnik() {
        return Korisnik.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    public void getKorisnikById_Success() {
        Korisnik korisnik = builderKorisnik();
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(korisnik));
        when(korisnikMapper.toResponse(korisnik)).thenReturn(new KorisnikResponseDTO());
        KorisnikResponseDTO found = korisnikService.getKorisnikById(1L);
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getKorisnikByUsername_Success() {
        Korisnik korisnik = builderKorisnik();
        when(korisnikRepository.findByUsername(korisnik.getUsername())).thenReturn(Optional.of(korisnik));
        when(korisnikMapper.toResponse(korisnik)).thenReturn(new KorisnikResponseDTO());
        KorisnikResponseDTO found = korisnikService.getKorisnikByUsername("username");
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void getAllKorisnici_Success() {
        Korisnik korisnik = builderKorisnik();
        when(korisnikRepository.findAll()).thenReturn(List.of(korisnik));
        when(korisnikMapper.toResponse(korisnik)).thenReturn(new KorisnikResponseDTO());
        List<KorisnikResponseDTO> all = korisnikService.getAllKorisnici();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    public void createKorisnik_Success() {
        KorisnikRequestDTO korisnikRequestDTO = KorisnikRequestDTO.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        Korisnik korisnik = builderKorisnik();
        when(korisnikRepository.save(korisnik)).thenReturn(korisnik);
        when(korisnikMapper.toResponse(korisnik)).thenReturn(new KorisnikResponseDTO());
        when(korisnikMapper.toEntity(korisnikRequestDTO)).thenReturn(korisnik);
        KorisnikResponseDTO saved = korisnikService.createKorisnik(korisnikRequestDTO);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void createKorisnik_ThrowsExceptionWhenEmailExists() {
        KorisnikRequestDTO korisnikRequestDTO = KorisnikRequestDTO.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        when(korisnikRepository.existsByEmail(korisnikRequestDTO.getEmail())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> korisnikService.createKorisnik(korisnikRequestDTO))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Korisnik sa tim email-om ili username-om već postoji");
    }

    @Test
    public void createKorisnik_ThrowsExceptionWhenUsernameExists(){
        KorisnikRequestDTO korisnikRequestDTO = KorisnikRequestDTO.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        when(korisnikRepository.existsByUsername(korisnikRequestDTO.getUsername())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> korisnikService.createKorisnik(korisnikRequestDTO))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Korisnik sa tim email-om ili username-om već postoji");
    }

    @Test
    public void updateKorisnik_Success(){
        Korisnik korisnik = builderKorisnik();
        korisnik.setUsername("novi username");
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(korisnik));
        when(korisnikRepository.save(korisnik)).thenReturn(korisnik);
        when(korisnikMapper.toResponse(korisnik)).thenReturn(new KorisnikResponseDTO());
        KorisnikResponseDTO updated = korisnikService.updateKorisnik(
                1L, "novi username", "proba@email.com", "KorisnikIme", "KorisnikPrezime");
        Assertions.assertThat(updated).isNotNull();
    }

    @Test
    public void updateKorisnik_ThrowsExceptionWhenEmailExists(){
        KorisnikRequestDTO korisnikRequestDTO = KorisnikRequestDTO.builder()
                .ime("KorisnikIme")
                .prezime("KorisnikPrezime")
                .email("proba@email.com")
                .username("username")
                .password("test")
                .role(Role.ADMIN)
                .build();
        Korisnik korisnik = builderKorisnik();
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(korisnik));
        when(korisnikRepository.existsByEmail(korisnikRequestDTO.getEmail())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> korisnikService.updateKorisnik(
                1L, "novi username", "proba@email.com", "KorisnikIme", "KorisnikPrezime"))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Korisnik sa tim email-om ili username-om već postoji");
    }

    @Test
    public void deleteKorisnik_Success(){
        Korisnik korisnik = builderKorisnik();
        korisnikService.deleteKorisnik(1L);
        verify(korisnikRepository).deleteById(1L);

    }

}
