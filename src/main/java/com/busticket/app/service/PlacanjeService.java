package com.busticket.app.service;

import com.busticket.app.model.entity.Placanje;
import com.busticket.app.repository.PlacanjeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacanjeService {

    private final PlacanjeRepository placanjeRepository;

    public Placanje getPlacanjeById(Long id){
        return placanjeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Placanje> getPlacanjaForRezervacija(Long id){
        return  placanjeRepository.findAllByRezervacijaId(id);
    }
}
