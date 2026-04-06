package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.repository.PlacanjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacanjeService {

    private final PlacanjeRepository placanjeRepository;

    public Placanje getPlacanjeById(Long id){
        return placanjeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Placanje nije pronadjeno"));
    }

    public List<Placanje> getPlacanjaForRezervacija(Long id){
        return  placanjeRepository.findAllByRezervacijaId(id);
    }
}
