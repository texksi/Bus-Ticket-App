package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.service.VoziloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler za upravljanje vozilima u sistemu za kupovinu autobuskih karata.
 * Sadrzi REST API endpointe za kreiranje, pregled, azuriranje i brisanje vozila, kao i za pregled vozila vezanih
 * za kompaniju.
 */
@RestController
@RequiredArgsConstructor
public class VoziloController {

    private final VoziloService voziloService;

    /**
     * Metoda koja vraca vozilo na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator vozila na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa VoziloResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/vozila/{id}")
    public ResponseEntity<VoziloResponseDTO> getVoziloById(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getVoziloById(id));
    }

    /**
     * Metoda koja vraca listu svih vozila vezanih za kompaniju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator kompanije za koju se traze vozila
     * @return ResponseEntity sa listom VoziloResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/vozila/kompanija/{id}")
    public ResponseEntity<List<VoziloResponseDTO>> getVozilaForKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getAllVozilaForKompanija(id));
    }

    /**
     * Metoda koja kreira novo vozilo na osnovu podataka prosledjenih u VoziloRequestDTO objektu
     *
     * @param vozilo - VoziloRequestDTO objekat koji sadrzi podatke za kreiranje novog vozila
     * @return ResponseEntity sa VoziloResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/vozila")
    public ResponseEntity<VoziloResponseDTO> createVozilo(@RequestBody VoziloRequestDTO vozilo) {
        return ResponseEntity.status(201).body(voziloService.createVozilo(vozilo));
    }

    /**
     * Metoda koja azurira postojece vozilo na osnovu prosledjenog ID-a i podataka u VoziloRequestDTO objektu
     *
     * @param vozilo - VoziloRequestDTO objekat koji sadrzi nove podatke vozila
     * @param id     - jedinstveni identifikator vozila kojeg treba azurirati
     * @return ResponseEntity sa VoziloResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/vozila/{id}")
    public ResponseEntity<VoziloResponseDTO> updateVozilo(@RequestBody VoziloRequestDTO vozilo, @PathVariable Long id) {
        return ResponseEntity.ok(voziloService.updateVozilo(id, vozilo.getKapacitet(), vozilo.getRegistracija(),
                vozilo.getBrojRedova(), vozilo.getBrojKolona()));
    }

    /**
     * Metoda koja brise vozilo iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator vozila kojeg treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/vozila/{id}")
    public ResponseEntity<Void> deleteVozilo(@PathVariable Long id) {
        voziloService.deleteVozilo(id);
        return ResponseEntity.noContent().build();
    }
}
