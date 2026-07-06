package com.busticket.app.controller;

import com.busticket.app.model.dto.request.GradRequestDTO;
import com.busticket.app.model.dto.response.GradResponseDTO;
import com.busticket.app.service.GradService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/gradovi")
public class GradController {

    private final GradService gradService;

    @GetMapping("/{id}")
    public ResponseEntity<GradResponseDTO> getGradById(@PathVariable Long id){
        return ResponseEntity.ok(gradService.getGradById(id));
    }

    //take another look for both search parametar
    @GetMapping
    public ResponseEntity<List<GradResponseDTO>> getAllGradovi(
            @RequestParam(required = false) String naziv,
            @RequestParam(required = false) String skracenica) {
        if (naziv != null) return ResponseEntity.ok(List.of(gradService.getGradByNaziv(naziv)));
        if (skracenica != null) return ResponseEntity.ok(List.of(gradService.getGradBySkracenica(skracenica)));
        return ResponseEntity.ok(gradService.getAllGradovi());
    }

    @PostMapping
    public ResponseEntity<GradResponseDTO> createGrad(@Valid @RequestBody GradRequestDTO dto){
        return ResponseEntity.status(201).body(gradService.createGrad(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GradResponseDTO> updateGrad(@PathVariable Long id,@Valid @RequestBody GradRequestDTO dto){
        return ResponseEntity.ok(gradService.updateGrad(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrad(@PathVariable Long id) {
        gradService.deleteGrad(id);
        return ResponseEntity.noContent().build();
    }
}
