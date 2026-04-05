package com.busticket.app.controller;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.service.KompanijaService;
import com.busticket.app.service.PutovanjeService;
import com.busticket.app.service.VoziloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KompanijaController {

    private final KompanijaService kompanijaService;
    private final PutovanjeService putovanjeService;
    private final VoziloService voziloService;

    @GetMapping("/api/kompanije")
    public List<Kompanija> getAllKompanije() {
        return kompanijaService.getAllKompanije();
    }

    @GetMapping("/api/kompanije/{id}")
    public Kompanija getKompanijaById(@PathVariable Long id) {
        return kompanijaService.getKompanijaById(id);
    }

    @GetMapping("/api/kompanije/{id}/putovanja")
    public List<Putovanje> getPutovanjaByKompanija(@PathVariable Long id) {
        return putovanjeService.getPutovanjaByKompanija(id);
    }

    @GetMapping("/api/kompanije/{id}/vozila")
    public List<Vozilo> getVozilaByKompanija(@PathVariable Long id) {
        return voziloService.getAllVozilaForKompanija(id);
    }

    @PostMapping("/api/kompanije")
    public Kompanija createKompanija(@RequestBody Kompanija kompanija) {
        return kompanijaService.createKompanija(kompanija);
    }

    @PutMapping("/api/kompanije/{id}")
    public Kompanija updateKompanija(@RequestBody Kompanija kompanija, @PathVariable Long id) {
        return kompanijaService.updateKompanija(id, kompanija.getNaziv(), kompanija.getKontakt());
    }

    @DeleteMapping("/api/kompanije/{id}")
    public void deleteKompanija(@PathVariable Long id) {
        kompanijaService.deleteKompanija(id);
    }


}
