package com.busticket.app.controller;

import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.service.OcenaService;
import com.busticket.app.service.PutovanjeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PutovanjeController {

    private final PutovanjeService putovanjeService;
    private final OcenaService ocenaService;

    @GetMapping("/api/putovanja")
    public List<Putovanje> getAllPutovanja() {
        return putovanjeService.getAllPutovanja();
    }

    @GetMapping("/api/putovanja/{id}")
    public Putovanje getPutovanje(@PathVariable Long id) {
        return putovanjeService.getPutovanjeById(id);
    }

    @GetMapping("/api/putovanja/{id}/ocene")
    public List<Ocena> getOceneForPutovanje(@PathVariable Long id) {
        return ocenaService.getOceneForPutovanje(id);
    }

    @PostMapping("/api/putovanja")
    public Putovanje createPutovanje(@RequestBody Putovanje putovanje) {
        return putovanjeService.createPutovanje(putovanje);
    }

    @PutMapping("/api/putovanja/{id}")
    public Putovanje updatePutovanje(@RequestBody Putovanje putovanje, @PathVariable Long id) {
        return putovanjeService.updatePutovanje(id, putovanje.getPolaziste(), putovanje.getOdrediste(),
                putovanje.getVremePolaska(), putovanje.getVremeDolaska(), putovanje.getOsnovnaCena());
    }

    @DeleteMapping("/api/putovanja/{id}")
    public void deletePutovanje(@PathVariable Long id) {
        putovanjeService.deletePutovanje(id);
    }


}
