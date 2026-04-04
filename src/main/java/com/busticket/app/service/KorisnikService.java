package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.repository.KorisnikRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;

    public Korisnik getKorisnikById(Long id){
        return korisnikRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Korisnik getKorisnikByUsername(String username){
        return korisnikRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public List<Korisnik> getAllKorisnici(){
        return korisnikRepository.findAll();
    }

    public Korisnik createKorisnik(Korisnik newKorisnik){
        if(korisnikRepository.existsByEmail(newKorisnik.getEmail()) || korisnikRepository.existsByUsername(newKorisnik.getUsername())){
            throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
        }
        return korisnikRepository.save(newKorisnik);
    }

    public Korisnik updateKorisnik(Long id, String username, String email, String ime, String prezime){
        Korisnik savedKorisnik = getKorisnikById(id);
        if (!savedKorisnik.getUsername().equals(username) || !savedKorisnik.getEmail().equals(email)) {
            if(korisnikRepository.existsByEmail(email) || korisnikRepository.existsByUsername(username)){
                throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
            }
        }
        savedKorisnik.setIme(ime);
        savedKorisnik.setPrezime(prezime);
        savedKorisnik.setEmail(email);
        savedKorisnik.setUsername(username);
        return korisnikRepository.save(savedKorisnik);
    }

    public void deleteKorisnik(Long id){
        korisnikRepository.deleteById(id);
    }
}
