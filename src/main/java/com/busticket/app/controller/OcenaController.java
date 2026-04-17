package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.service.OcenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler za upravljanje ocenama
 * Sadrzi REST API endpointe za kreiranje i brisanje ocena.
 */
@RestController
@RequiredArgsConstructor
public class OcenaController {

    private final OcenaService ocenaService;

    /**
     * Metoda koja kreira novu ocenu na osnovu podataka prosledjenih u OcenaRequestDTO objektu
     *
     * @param ocena - OcenaRequestDTO objekat koji sadrzi podatke za kreiranje nove ocene
     * @return ResponseEntity sa OcenaResponseDTO objektom i HTTP statusom 201
     */
    @PostMapping("/api/ocene")
    public ResponseEntity<OcenaResponseDTO> createOcena(@RequestBody OcenaRequestDTO ocena){
        return ResponseEntity.status(201).body(ocenaService.createOcena(ocena));
    }

    /**
     * Metoda koja brise ocenu iz sistema na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator ocene koju treba obrisati
     * @return ResponseEntity sa HTTP statusom 204
     */
    @DeleteMapping("/api/ocene/{id}")
    public ResponseEntity<Void> deleteOcena(@PathVariable Long id){
        ocenaService.deleteOcena(id);
        return ResponseEntity.noContent().build();
    }
}
