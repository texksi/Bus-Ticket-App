package com.busticket.app.controller;

import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.service.VoziloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoziloController {

    private final VoziloService voziloService;

    @GetMapping("/api/vozila/{id}")
    public Vozilo getVoziloById(@PathVariable Long id) {
        return voziloService.getVoziloById(id);
    }

    @GetMapping("/api/vozila/kompanija/{id}")
    public List<Vozilo> getVozilaForKompanija(@PathVariable Long id) {
        return voziloService.getAllVozilaForKompanija(id);
    }

    @PostMapping("/api/vozila")
    public Vozilo createVozilo(@RequestBody Vozilo vozilo) {
        return voziloService.createVozilo(vozilo);
    }

    @PutMapping("/api/vozila/{id}")
    public Vozilo updateVozilo(@RequestBody Vozilo vozilo, @PathVariable Long id) {
        return voziloService.updateVozilo(id, vozilo.getKapacitet(), vozilo.getRegistracija(), vozilo.getBrojRedova(),
                vozilo.getBrojKolona());
    }

    @DeleteMapping("/api/vozila/{id}")
    public void deleteVozilo(@PathVariable Long id) {
        voziloService.deleteVozilo(id);
    }
}
