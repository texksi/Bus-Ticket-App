package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.KartaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.service.KartaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KartaController {

    private final KartaService kartaService;

    @GetMapping("/api/karte/{id}")
    public ResponseEntity<KartaResponseDTO> getKartaById(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getKartaById(id));
    }

    @GetMapping("/api/karte/putovanje/{id}")
    public ResponseEntity<List<KartaResponseDTO>> getKarteForPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getAllKarteForPutovanje(id));
    }

    @PostMapping("/api/karte")
    public ResponseEntity<KartaResponseDTO> createKarta(@RequestBody KartaRequestDTO karta) {
        return ResponseEntity.status(201).body(kartaService.createKarta(karta));
    }

    @PutMapping("/api/karte/{id}")
    public ResponseEntity<KartaResponseDTO> updateKarta(@RequestBody KartaRequestDTO karta, @PathVariable Long id) {
        return ResponseEntity.ok(kartaService.updateKarta(id, karta.getBrojSedista(), karta.getTip()));
    }

    @DeleteMapping("/api/karte/{id}")
    public ResponseEntity<Void> deleteKarta(@PathVariable Long id) {
        kartaService.deleteKarta(id);
        return ResponseEntity.noContent().build();
    }
}
