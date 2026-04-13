package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KorisnikMapper;
import com.busticket.app.model.dto.RequestDTOs.KorisnikRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KorisnikResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.repository.KorisnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;

    public KorisnikResponseDTO getKorisnikById(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        return korisnikMapper.toResponse(korisnik);
    }

    public KorisnikResponseDTO getKorisnikByUsername(String username) {
        Korisnik korisnik = korisnikRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        return korisnikMapper.toResponse(korisnik);
    }

    public List<KorisnikResponseDTO> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        return korisnici.stream().map(korisnikMapper::toResponse).toList();
    }

    public KorisnikResponseDTO createKorisnik(KorisnikRequestDTO newKorisnik) {
        if (korisnikRepository.existsByEmail(newKorisnik.getEmail()) || korisnikRepository.existsByUsername(newKorisnik.getUsername())) {
            throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
        }
        Korisnik korisnik = korisnikRepository.save(korisnikMapper.toEntity(newKorisnik));
        return korisnikMapper.toResponse(korisnik);
    }

    public KorisnikResponseDTO updateKorisnik(Long id, String username, String email, String ime, String prezime) {
        Korisnik savedKorisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        if (!savedKorisnik.getUsername().equals(username) || !savedKorisnik.getEmail().equals(email)) {
            if (korisnikRepository.existsByEmail(email) || korisnikRepository.existsByUsername(username)) {
                throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
            }
        }
        savedKorisnik.setIme(ime);
        savedKorisnik.setPrezime(prezime);
        savedKorisnik.setEmail(email);
        savedKorisnik.setUsername(username);
        Korisnik korisnik = korisnikRepository.save(savedKorisnik);
        return korisnikMapper.toResponse(korisnik);
    }

    public void deleteKorisnik(Long id) {
        korisnikRepository.deleteById(id);
    }
}
