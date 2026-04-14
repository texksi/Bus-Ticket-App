package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.KompanijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KompanijaResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.PutovanjeResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.service.KompanijaService;
import com.busticket.app.service.PutovanjeService;
import com.busticket.app.service.VoziloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KompanijaController {

    private final KompanijaService kompanijaService;
    private final PutovanjeService putovanjeService;
    private final VoziloService voziloService;

    @GetMapping("/api/kompanije")
    public ResponseEntity<List<KompanijaResponseDTO>> getAllKompanije() {
        return ResponseEntity.ok(kompanijaService.getAllKompanije());
    }

    @GetMapping("/api/kompanije/{id}")
    public ResponseEntity<KompanijaResponseDTO> getKompanijaById(@PathVariable Long id) {
        return ResponseEntity.ok(kompanijaService.getKompanijaById(id));
    }

    @GetMapping("/api/kompanije/{id}/putovanja")
    public ResponseEntity<List<PutovanjeResponseDTO>> getPutovanjaByKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.getPutovanjaByKompanija(id));
    }

    @GetMapping("/api/kompanije/{id}/vozila")
    public ResponseEntity<List<VoziloResponseDTO>> getVozilaByKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getAllVozilaForKompanija(id));
    }

    @PostMapping("/api/kompanije")
    public ResponseEntity<KompanijaResponseDTO> createKompanija(@RequestBody KompanijaRequestDTO kompanija) {
        return ResponseEntity.status(201).body(kompanijaService.createKompanija(kompanija));
    }

    @PutMapping("/api/kompanije/{id}")
    public ResponseEntity<KompanijaResponseDTO> updateKompanija(@RequestBody KompanijaRequestDTO kompanija,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(kompanijaService.updateKompanija(id, kompanija.getNaziv(), kompanija.getKontakt()));
    }

    @DeleteMapping("/api/kompanije/{id}")
    public ResponseEntity<Void> deleteKompanija(@PathVariable Long id) {
        kompanijaService.deleteKompanija(id);
        return ResponseEntity.noContent().build();
    }


}
