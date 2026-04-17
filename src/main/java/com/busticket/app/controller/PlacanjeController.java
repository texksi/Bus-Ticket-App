package com.busticket.app.controller;

import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.service.PlacanjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontroler za upravljanje placanjima
 * Sadrzi REST API endpointe za pregled placanja i pregled placanja vezanih za rezervaciju.
 */
@RestController
@RequiredArgsConstructor
public class PlacanjeController {

    private final PlacanjeService placanjeService;

    /**
     * Metoda koja vraca placanje na osnovu prosledjenog ID-a
     *
     * @param id - jedinstveni identifikator placanja na osnovu koga se vrsi pretraga
     * @return ResponseEntity sa PlacanjeResponseDTO objektom i HTTP statusom 200
     */
    @GetMapping("/api/placanja/{id}")
    public ResponseEntity<PlacanjeResponseDTO> getPlacanjeById(@PathVariable Long id){
        return  ResponseEntity.ok(placanjeService.getPlacanjeById(id));
    }

    /**
     * Metoda koja vraca listu svih placanja vezanih za rezervaciju sa prosledjenim ID-om
     *
     * @param id - jedinstveni identifikator rezervacije za koju se traze placanja
     * @return ResponseEntity sa listom PlacanjeResponseDTO objekata i HTTP statusom 200
     */
    @GetMapping("/api/placanja/rezervacija/{id}")
    public ResponseEntity<List<PlacanjeResponseDTO>> getPlacanjaForRezervacija(@PathVariable Long id){
        return ResponseEntity.ok(placanjeService.getPlacanjaForRezervacija(id));
    }
}
