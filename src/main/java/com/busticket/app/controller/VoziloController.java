package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.service.VoziloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoziloController {

    private final VoziloService voziloService;

    @GetMapping("/api/vozila/{id}")
    public ResponseEntity<VoziloResponseDTO> getVoziloById(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getVoziloById(id));
    }

    @GetMapping("/api/vozila/kompanija/{id}")
    public ResponseEntity<List<VoziloResponseDTO>> getVozilaForKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getAllVozilaForKompanija(id));
    }

    @PostMapping("/api/vozila")
    public ResponseEntity<VoziloResponseDTO> createVozilo(@RequestBody VoziloRequestDTO vozilo) {
        return ResponseEntity.status(201).body(voziloService.createVozilo(vozilo));
    }

    @PutMapping("/api/vozila/{id}")
    public ResponseEntity<VoziloResponseDTO> updateVozilo(@RequestBody VoziloRequestDTO vozilo, @PathVariable Long id) {
        return ResponseEntity.ok(voziloService.updateVozilo(id, vozilo.getKapacitet(), vozilo.getRegistracija(),
                vozilo.getBrojRedova(), vozilo.getBrojKolona()));
    }

    @DeleteMapping("/api/vozila/{id}")
    public ResponseEntity<Void> deleteVozilo(@PathVariable Long id) {
        voziloService.deleteVozilo(id);
        return ResponseEntity.noContent().build();
    }
}
