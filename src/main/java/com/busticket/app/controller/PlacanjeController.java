package com.busticket.app.controller;

import com.busticket.app.model.entity.Placanje;
import com.busticket.app.service.PlacanjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlacanjeController {

    private final PlacanjeService placanjeService;

    @GetMapping("/api/placanja/{id}")
    public Placanje getPlacanjeById(@PathVariable Long id){
        return  placanjeService.getPlacanjeById(id);
    }

    @GetMapping("/api/placanja/rezervacija/{id}")
    public List<Placanje> getPlacanjaForRezervacija(@PathVariable Long id){
        return placanjeService.getPlacanjaForRezervacija(id);
    }
}
