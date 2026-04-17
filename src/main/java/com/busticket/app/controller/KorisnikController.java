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

/**
 * Kontroler za upravljanje korisnicima
 * Sadrzi REST API endpointe za kreiranje, pregled, azuriranje i brisanje korisnika, kao i za pregled
 * rezervacija i ocena vezanih za korisnika.
 */
@RestController
@RequiredArgsConstructor
public class KorisnikController {

    private final KorisnikService korisnikService;
    private final RezervacijaService rezervacijaService;
    private final OcenaService ocenaService;

    /**
     * Metoda koja vraca listu svih korisnika koji se nalaze u sistemu
     *
     * @return ResponseEntity<List<KorisnikResponseDTO>> - lista KorisnikResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/korisnici")
    public ResponseEntity<List<KorisnikResponseDTO>> getAllKorisnici() {
        return ResponseEntity.ok(korisnikService.getAllKorisnici());
    }

    /**
     * Metoda koja vraca korisnika na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator korisnika na osnovu koga se vrsi pretraga
     * @return ResponseEntity<KorisnikResponseDTO> - vraca KorisnikResponseDTO objekat i HTTP status 200
     */
    @GetMapping("/api/korisnici/{id}")
    public ResponseEntity<KorisnikResponseDTO> getKorisnik(@PathVariable Long id) {
        return ResponseEntity.ok(korisnikService.getKorisnikById(id));
    }

    /**
     * Metoda koja vraca listu svih rezervacija koje pripadaju korisniku sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator korisnika za koga se traze rezervacije
     * @return ResponseEntity<List<RezervacijaResponseDTO>> - vraca listu RezervacijaResponseDTO objekata
     * i HTTP status 200
     */
    @GetMapping("/api/korisnici/{id}/rezervacije")
    public ResponseEntity<List<RezervacijaResponseDTO>> getAllRezervacijeByKorisnik(@PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.getRezervacijeByKorisnik(id));
    }

    /**
     * Metoda koja vraca listu svih ocena koje je dao korisnik sa prosledjenim ID-om,
     *
     * @param id - jedinstveni identifikator korisnika za koga se traze ocene
     * @return ResponseEntity<List<OcenaResponseDTO>> - vraca listu OcenaResponseDTO objekata i HTTP status 200
     */
    @GetMapping("/api/korisnici/{id}/ocene")
    public ResponseEntity<List<OcenaResponseDTO>> getAllOceneByKorisnik(@PathVariable Long id) {
        return ResponseEntity.ok(ocenaService.getOceneByKorisnik(id));
    }

    /**
     * Metoda koja kreira novog korisnika na osnovu podataka prosledjenih u KorisnikRequestDTO objektu
     *
     * @param korisnik - KorisnikRequestDTO objekat koji sadrzi podatke za kreiranje novog korisnika
     * @return ResponseEntity sa KorisnikResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/korisnici")
    public ResponseEntity<KorisnikResponseDTO> createKorisnik(@RequestBody KorisnikRequestDTO korisnik) {
        return ResponseEntity.status(201).body(korisnikService.createKorisnik(korisnik));
    }

    /**
     * Metoda koja azurira postojeceg korisnika na osnovu prosledjenog ID-a i podataka u KorisnikRequestDTO objektu
     *
     * @param id       - jedinstveni identifikator korisnika kojeg treba azurirati
     * @param korisnik - KorisnikRequestDTO objekat koji sadrzi nove podatke korisnika
     * @return ResponseEntity sa KorisnikResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/korisnici/{id}")
    public ResponseEntity<KorisnikResponseDTO> updateKorisnik(@PathVariable Long id, @RequestBody KorisnikRequestDTO korisnik) {
        return ResponseEntity.ok(korisnikService.updateKorisnik(id, korisnik.getUsername(),
                korisnik.getEmail(), korisnik.getIme(), korisnik.getPrezime()));
    }

    /**
     * Metoda koja brise korisnika iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator korisnika kojeg treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/korisnici/{id}")
    public ResponseEntity<Void> deleteKorisnik(@PathVariable Long id) {
        korisnikService.deleteKorisnik(id);
        return ResponseEntity.noContent().build();
    }

}
