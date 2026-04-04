package com.busticket.app.service;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.RezervacijaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;

    public Rezervacija getRezervacijaById(Long id){
        return rezervacijaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Rezervacija> getAllRezervacije(){
        return rezervacijaRepository.findAll();
    }

    public Rezervacija createRezervacija(Rezervacija rezervacija){
        return rezervacijaRepository.save(rezervacija);
    }

    public Rezervacija updateRezervacija(Long id, String status, String nacinPlacanja, double ukupanIznos){
        Rezervacija savedRezervacija = getRezervacijaById(id);
        savedRezervacija.setStatus(status);
        savedRezervacija.setNacinPlacanja(nacinPlacanja);
        savedRezervacija.setUkupanIznos(ukupanIznos);
        return rezervacijaRepository.save(savedRezervacija);
    }

    public void deleteRezervacija(Long id){
        rezervacijaRepository.deleteById(id);
    }

    public List<Rezervacija> getRezervacijeByKorisnik(Long korisnikId){
        return rezervacijaRepository.findAllByKorisnikId(korisnikId);
    }
}
