package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.PutovanjeRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.dto.ResponseDTOs.PutovanjeResponseDTO;
import com.busticket.app.service.OcenaService;
import com.busticket.app.service.PutovanjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler za upravljanje putovanjima
 * Izlaze REST API endpointe za kreiranje, pregled, azuriranje i brisanje putovanja, kao i za pregled ocena
 * vezanih za putovanje.
 */
@RestController
@RequiredArgsConstructor
public class PutovanjeController {

    private final PutovanjeService putovanjeService;
    private final OcenaService ocenaService;

    /**
     * Metoda koja vraca listu svih putovanja koja se nalaze u sistemu
     *
     * @return ResponseEntity sa listom PutovanjeResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/putovanja")
    public ResponseEntity<List<PutovanjeResponseDTO>> getAllPutovanja() {
        return ResponseEntity.ok(putovanjeService.getAllPutovanja());
    }

    /**
     * Metoda koja vraca putovanje na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator putovanja na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa PutovanjeResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/putovanja/{id}")
    public ResponseEntity<PutovanjeResponseDTO> getPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.getPutovanjeById(id));
    }

    /**
     * Metoda koja vraca listu svih ocena vezanih za putovanje sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator putovanja za koje se traze ocene
     * @return ResponseEntity sa listom OcenaResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/putovanja/{id}/ocene")
    public ResponseEntity<List<OcenaResponseDTO>> getOceneForPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(ocenaService.getOceneForPutovanje(id));
    }

    /**
     * Metoda koja kreira novo putovanje na osnovu podataka prosledjenih u PutovanjeRequestDTO objektu
     *
     * @param putovanje - PutovanjeRequestDTO objekat koji sadrzi podatke za kreiranje novog putovanja
     * @return ResponseEntity sa PutovanjeResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/putovanja")
    public ResponseEntity<PutovanjeResponseDTO> createPutovanje(@RequestBody PutovanjeRequestDTO putovanje) {
        return ResponseEntity.status(201).body(putovanjeService.createPutovanje(putovanje));
    }

    /**
     * Metoda koja azurira postojece putovanje na osnovu prosledjenog ID-a i podataka u PutovanjeRequestDTO objektu
     *
     * @param putovanje - PutovanjeRequestDTO objekat koji sadrzi nove podatke putovanja
     * @param id        - jedinstveni identifikator putovanja kojeg treba azurirati
     * @return ResponseEntity sa PutovanjeResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/putovanja/{id}")
    public ResponseEntity<PutovanjeResponseDTO> updatePutovanje(@RequestBody PutovanjeRequestDTO putovanje,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.updatePutovanje(id, putovanje.getPolaziste(),
                putovanje.getOdrediste(), putovanje.getVremePolaska(), putovanje.getVremeDolaska(),
                putovanje.getOsnovnaCena()));
    }

    /**
     * Metoda koja brise putovanje iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator putovanja kojeg treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/putovanja/{id}")
    public ResponseEntity<Void> deletePutovanje(@PathVariable Long id) {
        putovanjeService.deletePutovanje(id);
        return ResponseEntity.noContent().build();
    }


}
