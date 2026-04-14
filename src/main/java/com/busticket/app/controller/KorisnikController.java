package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.KorisnikRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KorisnikResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.RezervacijaResponseDTO;
import com.busticket.app.service.KorisnikService;
import com.busticket.app.service.OcenaService;
import com.busticket.app.service.RezervacijaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KorisnikController {

    private final KorisnikService korisnikService;
    private final RezervacijaService rezervacijaService;
    private final OcenaService ocenaService;

    @GetMapping("/api/korisnici")
    public ResponseEntity<List<KorisnikResponseDTO>> getAllKorisnici(){
        return ResponseEntity.ok(korisnikService.getAllKorisnici());
    }

    @GetMapping("/api/korisnici/{id}")
    public ResponseEntity<KorisnikResponseDTO> getKorisnik(@PathVariable Long id){
        return  ResponseEntity.ok(korisnikService.getKorisnikById(id));
    }

    @GetMapping("/api/korisnici/{id}/rezervacije")
    public ResponseEntity<List<RezervacijaResponseDTO>> getAllRezervacijeByKorisnik(@PathVariable  Long id){
        return ResponseEntity.ok(rezervacijaService.getRezervacijeByKorisnik(id));
    }

    @GetMapping("/api/korisnici/{id}/ocene")
    public ResponseEntity<List<OcenaResponseDTO>> getAllOceneByKorisnik(@PathVariable Long id){
        return ResponseEntity.ok(ocenaService.getOceneByKorisnik(id));
    }

    @PostMapping("/api/korisnici")
    public ResponseEntity<KorisnikResponseDTO> createKorisnik(@RequestBody KorisnikRequestDTO korisnik){
        return ResponseEntity.status(201).body(korisnikService.createKorisnik(korisnik));
    }

    @PutMapping("/api/korisnici/{id}")
    public ResponseEntity<KorisnikResponseDTO> updateKorisnik(@PathVariable Long id, @RequestBody KorisnikRequestDTO korisnik){
        return ResponseEntity.ok(korisnikService.updateKorisnik(id, korisnik.getUsername(),
                korisnik.getEmail(), korisnik.getIme(), korisnik.getPrezime()));
    }

    @DeleteMapping("/api/korisnici/{id}")
    public ResponseEntity<Void> deleteKorisnik(@PathVariable Long id){
        korisnikService.deleteKorisnik(id);
        return ResponseEntity.noContent().build();
    }

}
