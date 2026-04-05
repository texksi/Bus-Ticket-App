package com.busticket.app.controller;

import com.busticket.app.model.entity.Karta;
import com.busticket.app.service.KartaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KartaController {

    private final KartaService kartaService;

    @GetMapping("/api/karte/{id}")
    public Karta getKartaById(@PathVariable Long id) {
        return kartaService.getKartaById(id);
    }

    @GetMapping("/api/karte/putovanje/{id}")
    public List<Karta> getKarteForPutovanje(@PathVariable Long id) {
        return kartaService.getAllKarteForPutovanje(id);
    }

    @PostMapping("/api/karte")
    public Karta createKarta(@RequestBody Karta karta) {
        return kartaService.createKarta(karta);
    }

    @PutMapping("/api/karte/{id}")
    public Karta updateKarta(@RequestBody Karta karta, @PathVariable Long id) {
        return kartaService.updateKarta(id, karta.getBrojSedista(), karta.getTip());
    }

    @DeleteMapping("/api/karte/{id}")
    public void deleteKarta(@PathVariable Long id) {
        kartaService.deleteKarta(id);
    }
}
