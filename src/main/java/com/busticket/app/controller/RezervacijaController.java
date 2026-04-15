package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.RezervacijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.RezervacijaResponseDTO;
import com.busticket.app.service.KartaService;
import com.busticket.app.service.RezervacijaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RezervacijaController {

    private final RezervacijaService rezervacijaService;
    private final KartaService kartaService;

    @GetMapping("/api/rezervacije")
    public ResponseEntity<List<RezervacijaResponseDTO>> getAllRezervacije() {
        return ResponseEntity.ok(rezervacijaService.getAllRezervacije());
    }

    @GetMapping("/api/rezervacije/{id}")
    public ResponseEntity<RezervacijaResponseDTO> getRezervacijaById(@PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.getRezervacijaById(id));
    }

    @GetMapping("/api/rezervacije/{id}/karte")
    public ResponseEntity<List<KartaResponseDTO>> getAllKarteForRezervacija(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getAllKarteForRezervacija(id));
    }

    @PostMapping("/api/rezervacije")
    public ResponseEntity<RezervacijaResponseDTO> createRezervacija(@RequestBody RezervacijaRequestDTO rezervacija) {
        return ResponseEntity.status(201).body(rezervacijaService.createRezervacija(rezervacija));
    }

    @PutMapping("/api/rezervacije/{id}")
    public ResponseEntity<RezervacijaResponseDTO> updateRezervacija(@RequestBody RezervacijaRequestDTO rezervacija,
                                                                    @PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.updateRezervacija(id, rezervacija.getStatus(),
                rezervacija.getNacinPlacanja(), rezervacija.getUkupanIznos()));
    }

    @DeleteMapping("/api/rezervacije/{id}")
    public ResponseEntity<Void> deleteRezervacija(@PathVariable Long id) {
        rezervacijaService.deleteRezervacija(id);
        return ResponseEntity.noContent().build();
    }
}
