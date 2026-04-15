package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.RezervacijaMapper;
import com.busticket.app.model.dto.RequestDTOs.RezervacijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.RezervacijaResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final KorisnikRepository korisnikRepository;

    public RezervacijaResponseDTO getRezervacijaById(Long id){
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena"));
        return rezervacijaMapper.toResponse(rezervacija);
    }

    public List<RezervacijaResponseDTO> getAllRezervacije(){
        List<Rezervacija> rezervacije = rezervacijaRepository.findAll();
        return rezervacije.stream().map(rezervacijaMapper::toResponse).toList();
    }

    public RezervacijaResponseDTO createRezervacija(RezervacijaRequestDTO newRezervacija){
        Rezervacija rezervacija = rezervacijaMapper.toEntity(newRezervacija);
        Korisnik korisnik = korisnikRepository.findById(newRezervacija.getKorisnikId())
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        rezervacija.setKorisnik(korisnik);
        return rezervacijaMapper.toResponse(rezervacijaRepository.save(rezervacija));
    }

    public RezervacijaResponseDTO updateRezervacija(Long id, String status, String nacinPlacanja, double ukupanIznos){
        Rezervacija savedRezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena"));
        savedRezervacija.setStatus(status);
        savedRezervacija.setNacinPlacanja(nacinPlacanja);
        savedRezervacija.setUkupanIznos(ukupanIznos);
        Rezervacija rezervacija = rezervacijaRepository.save(savedRezervacija);
        return rezervacijaMapper.toResponse(rezervacija);
    }

    public void deleteRezervacija(Long id){
        rezervacijaRepository.deleteById(id);
    }

    public List<RezervacijaResponseDTO> getRezervacijeByKorisnik(Long korisnikId){
        List<Rezervacija> rezervacije = rezervacijaRepository.findAllByKorisnikId(korisnikId);
        return rezervacije.stream().map(rezervacijaMapper::toResponse).toList();
    }
}
