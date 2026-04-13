package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PutovanjeMapper;
import com.busticket.app.model.dto.RequestDTOs.PutovanjeRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.PutovanjeResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.VoziloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PutovanjeService {

    private final PutovanjeRepository putovanjeRepository;
    private final PutovanjeMapper putovanjeMapper;
    private final VoziloRepository voziloRepository;
    private final KompanijaRepository kompanijaRepository;

    public PutovanjeResponseDTO getPutovanjeById(Long id) {
        Putovanje putovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno"));
        return putovanjeMapper.toResponse(putovanje);
    }

    public List<PutovanjeResponseDTO> getAllPutovanja() {
        List<Putovanje> putovanja = putovanjeRepository.findAll();
        return putovanja.stream().map(putovanjeMapper::toResponse).toList();
    }

    public PutovanjeResponseDTO createPutovanje(PutovanjeRequestDTO newPutovanje) {
        Putovanje putovanje = putovanjeMapper.toEntity(newPutovanje);
        Vozilo vozilo = voziloRepository.findById(newPutovanje.getVoziloId()).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno")
        );
        Kompanija kompanija = kompanijaRepository.findById(newPutovanje.getKompanijaId()).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        putovanje.setVozilo(vozilo);
        putovanje.setKompanija(kompanija);
        Putovanje saved = putovanjeRepository.save(putovanje);
        return putovanjeMapper.toResponse(saved);
    }

    public PutovanjeResponseDTO updatePutovanje(Long id, String polaziste, String odrediste, LocalDateTime vremePolaska, LocalDateTime vremeDolaska, double osnovnaCena) {
        Putovanje savedPutovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        savedPutovanje.setPolaziste(polaziste);
        savedPutovanje.setOdrediste(odrediste);
        savedPutovanje.setVremePolaska(vremePolaska);
        savedPutovanje.setVremeDolaska(vremeDolaska);
        savedPutovanje.setOsnovnaCena(osnovnaCena);
        Putovanje putovanje = putovanjeRepository.save(savedPutovanje);
        return putovanjeMapper.toResponse(putovanje);
    }

    public void deletePutovanje(Long id) {
        putovanjeRepository.deleteById(id);
    }

    public List<PutovanjeResponseDTO> getPutovanjaByKompanija(Long kompanijaId) {
        List<Putovanje> putovanja = putovanjeRepository.findAllByKompanijaId(kompanijaId);
        return putovanja.stream().map(putovanjeMapper::toResponse).toList();
    }

}
