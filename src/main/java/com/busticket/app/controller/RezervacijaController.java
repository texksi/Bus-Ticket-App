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

/**
 * Kontroler za upravljanje rezervacijama
 * Izlaze REST API endpointe za kreiranje, pregled, azuriranje i brisanje rezervacija, kao i za pregled karata vezanih
 * za rezervaciju.
 */
@RestController
@RequiredArgsConstructor
public class RezervacijaController {

    private final RezervacijaService rezervacijaService;
    private final KartaService kartaService;

    /**
     * Metoda koja vraca listu svih rezervacija koje se nalaze u sistemu
     *
     * @return ResponseEntity sa listom RezervacijaResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/rezervacije")
    public ResponseEntity<List<RezervacijaResponseDTO>> getAllRezervacije() {
        return ResponseEntity.ok(rezervacijaService.getAllRezervacije());
    }

    /**
     * Metoda koja vraca rezervaciju na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator rezervacije na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa RezervacijaResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/rezervacije/{id}")
    public ResponseEntity<RezervacijaResponseDTO> getRezervacijaById(@PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.getRezervacijaById(id));
    }

    /**
     * Metoda koja vraca listu svih karata vezanih za rezervaciju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator rezervacije za koju se traze karte
     * @return ResponseEntity sa listom KartaResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/rezervacije/{id}/karte")
    public ResponseEntity<List<KartaResponseDTO>> getAllKarteForRezervacija(@PathVariable Long id) {
        return ResponseEntity.ok(kartaService.getAllKarteForRezervacija(id));
    }

    /**
     * Metoda koja kreira novu rezervaciju na osnovu podataka prosledjenih u RezervacijaRequestDTO objektu
     *
     * @param rezervacija - RezervacijaRequestDTO objekat koji sadrzi podatke za kreiranje nove rezervacije
     * @return ResponseEntity sa RezervacijaResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/rezervacije")
    public ResponseEntity<RezervacijaResponseDTO> createRezervacija(@RequestBody RezervacijaRequestDTO rezervacija) {
        return ResponseEntity.status(201).body(rezervacijaService.createRezervacija(rezervacija));
    }

    /**
     * Metoda koja azurira postojecu rezervaciju na osnovu prosledjenog ID-a i podataka u RezervacijaRequestDTO objektu
     *
     * @param rezervacija - RezervacijaRequestDTO objekat koji sadrzi nove podatke rezervacije
     * @param id          - jedinstveni identifikator rezervacije koju treba azurirati
     * @return ResponseEntity sa RezervacijaResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/rezervacije/{id}")
    public ResponseEntity<RezervacijaResponseDTO> updateRezervacija(@RequestBody RezervacijaRequestDTO rezervacija,
                                                                    @PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.updateRezervacija(id, rezervacija.getStatus(),
                rezervacija.getNacinPlacanja(), rezervacija.getUkupanIznos()));
    }

    /**
     * Metoda koja brise rezervaciju iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator rezervacije koju treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/rezervacije/{id}")
    public ResponseEntity<Void> deleteRezervacija(@PathVariable Long id) {
        rezervacijaService.deleteRezervacija(id);
        return ResponseEntity.noContent().build();
    }
}
