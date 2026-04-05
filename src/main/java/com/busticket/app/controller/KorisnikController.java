package com.busticket.app.controller;

import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.service.KorisnikService;
import com.busticket.app.service.OcenaService;
import com.busticket.app.service.RezervacijaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KorisnikController {

    private final KorisnikService korisnikService;
    private final RezervacijaService rezervacijaService;
    private final OcenaService ocenaService;

    @GetMapping("/api/korisnici")
    public List<Korisnik> getAllKorisnici(){
        return  korisnikService.getAllKorisnici();
    }

    @GetMapping("/api/korisnici/{id}")
    public Korisnik getKorisnik(@PathVariable Long id){
        return  korisnikService.getKorisnikById(id);
    }

    @GetMapping("/api/korisnici/{id}/rezervacije")
    public List<Rezervacija> getAllRezervacijeByKorisnik(@PathVariable  Long id){
        return rezervacijaService.getRezervacijeByKorisnik(id);
    }

    @GetMapping("/api/korisnici/{id}/ocene")
    public List<Ocena> getAllOceneByKorisnik(@PathVariable Long id){
        return ocenaService.getOceneByKorisnik(id);
    }

    @PostMapping("/api/korisnici")
    public Korisnik createKorisnik(@RequestBody  Korisnik korisnik){
        return  korisnikService.createKorisnik(korisnik);
    }

    @PutMapping("/api/korisnici/{id}")
    public Korisnik updateKorisnik(@PathVariable Long id, @RequestBody Korisnik korisnik){
        return korisnikService.updateKorisnik(id, korisnik.getUsername(),
                korisnik.getEmail(), korisnik.getIme(), korisnik.getPrezime());
    }

    @DeleteMapping("/api/korisnici/{id}")
    public void deleteKorisnik(@PathVariable Long id){
        korisnikService.deleteKorisnik(id);
    }

}
