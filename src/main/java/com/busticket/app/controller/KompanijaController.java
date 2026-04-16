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

/**
 * Kontroler za upravljanje kompanijama
 * Sadrzo REST API endpointe za kreiranje, pregled, azuriranje i brisanje kompanija,
 * kao i za pregled putovanja i vozila vezanih za kompaniju.
 */
@RestController
@RequiredArgsConstructor
public class KompanijaController {

    private final KompanijaService kompanijaService;
    private final PutovanjeService putovanjeService;
    private final VoziloService voziloService;

    /**
     * Metoda koja vraca listu svih kompanija koje se nalaze u sistemu
     *
     * @return ResponseEntity sa listom KompanijaResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/kompanije")
    public ResponseEntity<List<KompanijaResponseDTO>> getAllKompanije() {
        return ResponseEntity.ok(kompanijaService.getAllKompanije());
    }

    /**
     * Metoda koja vraca kompaniju na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator kompanije na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa KompanijaResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/kompanije/{id}")
    public ResponseEntity<KompanijaResponseDTO> getKompanijaById(@PathVariable Long id) {
        return ResponseEntity.ok(kompanijaService.getKompanijaById(id));
    }

    /**
     * Metoda koja vraca listu svih putovanja vezanih za kompaniju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator kompanije za koju se traze putovanja
     * @return ResponseEntity sa listom PutovanjeResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/kompanije/{id}/putovanja")
    public ResponseEntity<List<PutovanjeResponseDTO>> getPutovanjaByKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.getPutovanjaByKompanija(id));
    }

    /**
     * Metoda koja vraca listu svih vozila vezanih za kompaniju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator kompanije za koju se traze vozila
     * @return ResponseEntity sa listom VoziloResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/kompanije/{id}/vozila")
    public ResponseEntity<List<VoziloResponseDTO>> getVozilaByKompanija(@PathVariable Long id) {
        return ResponseEntity.ok(voziloService.getAllVozilaForKompanija(id));
    }

    /**
     * Metoda koja kreira novu kompaniju na osnovu podataka prosledjenih u KompanijaRequestDTO objektu
     *
     * @param kompanija - KompanijaRequestDTO objekat koji sadrzi podatke za kreiranje nove kompanije
     * @return ResponseEntity sa KompanijaResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/kompanije")
    public ResponseEntity<KompanijaResponseDTO> createKompanija(@RequestBody KompanijaRequestDTO kompanija) {
        return ResponseEntity.status(201).body(kompanijaService.createKompanija(kompanija));
    }

    /**
     * Metoda koja azurira postojecu kompaniju na osnovu prosledjenog ID-a i podataka u KompanijaRequestDTO objektu
     *
     * @param kompanija - KompanijaRequestDTO objekat koji sadrzi nove podatke kompanije
     * @param id        - jedinstveni identifikator kompanije koju treba azurirati
     * @return ResponseEntity sa KompanijaResponseDTO objektom i HTTP statusom 200
     */
    @PutMapping("/api/kompanije/{id}")
    public ResponseEntity<KompanijaResponseDTO> updateKompanija(@RequestBody KompanijaRequestDTO kompanija,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(kompanijaService.updateKompanija(id, kompanija.getNaziv(), kompanija.getKontakt()));
    }

    /**
     * Metoda koja brise kompaniju iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator kompanije koju treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/kompanije/{id}")
    public ResponseEntity<Void> deleteKompanija(@PathVariable Long id) {
        kompanijaService.deleteKompanija(id);
        return ResponseEntity.noContent().build();
    }


}
