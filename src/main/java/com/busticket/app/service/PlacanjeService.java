package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PlacanjeMapper;
import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.repository.PlacanjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacanjeService {

    private final PlacanjeRepository placanjeRepository;
    private final PlacanjeMapper placanjeMapper;

    public PlacanjeResponseDTO getPlacanjeById(Long id) {
        Placanje placanje = placanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Placanje nije pronadjeno"));
        return placanjeMapper.toResponse(placanje);
    }

    public List<PlacanjeResponseDTO> getPlacanjaForRezervacija(Long id) {
        List<Placanje> placanja = placanjeRepository.findAllByRezervacijaId(id);
        return placanja.stream().map(placanjeMapper::toResponse).toList();
    }
}
