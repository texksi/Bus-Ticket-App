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

@RestController
@RequiredArgsConstructor
public class PutovanjeController {

    private final PutovanjeService putovanjeService;
    private final OcenaService ocenaService;

    @GetMapping("/api/putovanja")
    public ResponseEntity<List<PutovanjeResponseDTO>> getAllPutovanja() {
        return ResponseEntity.ok(putovanjeService.getAllPutovanja());
    }

    @GetMapping("/api/putovanja/{id}")
    public ResponseEntity<PutovanjeResponseDTO> getPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.getPutovanjeById(id));
    }

    @GetMapping("/api/putovanja/{id}/ocene")
    public ResponseEntity<List<OcenaResponseDTO>> getOceneForPutovanje(@PathVariable Long id) {
        return ResponseEntity.ok(ocenaService.getOceneForPutovanje(id));
    }

    @PostMapping("/api/putovanja")
    public ResponseEntity<PutovanjeResponseDTO> createPutovanje(@RequestBody PutovanjeRequestDTO putovanje) {
        return ResponseEntity.status(201).body(putovanjeService.createPutovanje(putovanje));
    }

    @PutMapping("/api/putovanja/{id}")
    public ResponseEntity<PutovanjeResponseDTO> updatePutovanje(@RequestBody PutovanjeRequestDTO putovanje,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(putovanjeService.updatePutovanje(id, putovanje.getPolaziste(),
                putovanje.getOdrediste(), putovanje.getVremePolaska(), putovanje.getVremeDolaska(),
                putovanje.getOsnovnaCena()));
    }

    @DeleteMapping("/api/putovanja/{id}")
    public ResponseEntity<Void> deletePutovanje(@PathVariable Long id) {
        putovanjeService.deletePutovanje(id);
        return ResponseEntity.noContent().build();
    }


}
