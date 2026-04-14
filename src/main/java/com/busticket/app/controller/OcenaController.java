package com.busticket.app.controller;

import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.service.OcenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OcenaController {

    private final OcenaService ocenaService;

    @PostMapping("/api/ocene")
    public ResponseEntity<OcenaResponseDTO> createOcena(@RequestBody OcenaRequestDTO ocena){
        return ResponseEntity.status(201).body(ocenaService.createOcena(ocena));
    }

    @DeleteMapping("/api/ocene/{id}")
    public ResponseEntity<Void> deleteOcena(@PathVariable Long id){
        ocenaService.deleteOcena(id);
        return ResponseEntity.noContent().build();
    }
}
