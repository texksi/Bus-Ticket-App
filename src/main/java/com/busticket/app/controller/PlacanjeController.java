package com.busticket.app.controller;

import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.service.PlacanjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlacanjeController {

    private final PlacanjeService placanjeService;

    @GetMapping("/api/placanja/{id}")
    public ResponseEntity<PlacanjeResponseDTO> getPlacanjeById(@PathVariable Long id){
        return  ResponseEntity.ok(placanjeService.getPlacanjeById(id));
    }

    @GetMapping("/api/placanja/rezervacija/{id}")
    public ResponseEntity<List<PlacanjeResponseDTO>> getPlacanjaForRezervacija(@PathVariable Long id){
        return ResponseEntity.ok(placanjeService.getPlacanjaForRezervacija(id));
    }
}
