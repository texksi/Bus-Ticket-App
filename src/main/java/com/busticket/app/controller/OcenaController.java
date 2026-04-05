package com.busticket.app.controller;

import com.busticket.app.model.entity.Ocena;
import com.busticket.app.service.OcenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OcenaController {

    private final OcenaService ocenaService;

    @PostMapping("/api/ocene")
    public Ocena createOcena(@RequestBody Ocena ocena){
        return ocenaService.createOcena(ocena);
    }

    @DeleteMapping("/api/ocene/{id}")
    public void deleteOcena(@PathVariable Long id){
        ocenaService.deleteOcena(id);
    }
}
