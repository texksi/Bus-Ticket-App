package com.busticket.app.controller;

import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.service.KartaService;
import com.busticket.app.service.RezervacijaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RezervacijaController {

    private final RezervacijaService rezervacijaService;
    private final KartaService kartaService;

    @GetMapping("/api/rezervacije")
    public List<Rezervacija> getAllRezervacije() {
        return rezervacijaService.getAllRezervacije();
    }

    @GetMapping("/api/rezervacije/{id}")
    public Rezervacija getRezervacijaById(@PathVariable Long id) {
        return rezervacijaService.getRezervacijaById(id);
    }

    @GetMapping("/api/rezervacije/{id}/karte")
    public List<Karta> getAllKarteForRezervacija(@PathVariable Long id) {
        return kartaService.getAllKarteForRezervacija(id);
    }

    @PostMapping("/api/rezervacije")
    public Rezervacija createRezervacija(@RequestBody Rezervacija rezervacija) {
        return rezervacijaService.createRezervacija(rezervacija);
    }

    @PutMapping("/api/rezervacije/{id}")
    public Rezervacija updateRezervacija(@RequestBody Rezervacija rezervacija, @PathVariable Long id) {
        return rezervacijaService.updateRezervacija(id, rezervacija.getStatus(), rezervacija.getNacinPlacanja(),
                rezervacija.getUkupanIznos());
    }

    @DeleteMapping("/api/rezervacije/{id}")
    public void deleteRezervacija(@PathVariable Long id) {
        rezervacijaService.deleteRezervacija(id);
    }
}
