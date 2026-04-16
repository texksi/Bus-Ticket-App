package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.KartaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.service.KartaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler za upravljanje kartama
 * Prikazuje REST API endpointe za kreiranje, pregled, azuriranje i brisanje karata, kao i za pregled karata vezanih
 * za putovanje.
 */
@RestController
@RequiredArgsConstructor
public class KartaController {

    private final KartaService kartaService;

    /**
     * Metoda koja vraca kartu na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator karte na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa KartaResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/karte/{id}")
    public ResponseEntity<KartaResponseDTO> getKartaById(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getKartaById(id));
    }

    /**
     * Metoda koja vraca listu svih karata vezanih za putovanje sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator putovanja za koje se traze karte
     * @return ResponseEntity sa listom KartaResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/karte/putovanje/{id}")
    public ResponseEntity<List<KartaResponseDTO>> getKarteForPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getAllKarteForPutovanje(id));
    }

    /**
     * Metoda koja kreira novu kartu na osnovu podataka prosledjenih u KartaRequestDTO objektu
     *
     * @param karta - KartaRequestDTO objekat koji sadrzi podatke za kreiranje nove karte
     * @return ResponseEntity sa KartaResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/karte")
    public ResponseEntity<KartaResponseDTO> createKarta(@RequestBody KartaRequestDTO karta) {
        return ResponseEntity.status(201).body(kartaService.createKarta(karta));
    }

    /**
     * Metoda koja azurira postojecu kartu na osnovu prosledjenog ID-a i podataka u KartaRequestDTO objektu
     *
     * @param karta - KartaRequestDTO objekat koji sadrzi nove podatke karte
     * @param id    - jedinstveni identifikator karte koju treba azurirati
     * @return ResponseEntity sa KartaResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/karte/{id}")
    public ResponseEntity<KartaResponseDTO> updateKarta(@RequestBody KartaRequestDTO karta, @PathVariable Long id) {
        return ResponseEntity.ok(kartaService.updateKarta(id, karta.getBrojSedista(), karta.getTip()));
    }

    /**
     * Metoda koja brise kartu iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator karte koju treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/karte/{id}")
    public ResponseEntity<Void> deleteKarta(@PathVariable Long id) {
        kartaService.deleteKarta(id);
        return ResponseEntity.noContent().build();
    }
}
