package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KartaMapper;
import com.busticket.app.model.dto.RequestDTOs.KartaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.KartaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KartaService {

    private final KartaRepository kartaRepository;
    private final KartaMapper kartaMapper;
    private final RezervacijaRepository rezervacijaRepository;
    private final PutovanjeRepository putovanjeRepository;

    public KartaResponseDTO getKartaById(Long id){
        Karta karta = kartaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Karta nije pronađena"));
        return kartaMapper.toResponse(karta);
    }

    public List<KartaResponseDTO> getAllKarte(){
        List<Karta> karte = kartaRepository.findAll();
        return karte.stream().map(kartaMapper::toResponse).toList();
    }

    public KartaResponseDTO createKarta(KartaRequestDTO newKarta){
        Karta karta = kartaMapper.toEntity(newKarta);
        Rezervacija rezervacija = rezervacijaRepository.findById(newKarta.getRezervacijaId()).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena")
        );
        karta.setRezervacija(rezervacija);
        Putovanje putovanje = putovanjeRepository.findById(newKarta.getPutovanjeId()).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        karta.setPutovanje(putovanje);
        Karta saved = kartaRepository.save(karta);
        return kartaMapper.toResponse(saved);
    }

    public KartaResponseDTO updateKarta(Long id, String brojSedista, String tip){
        Karta savedKarta = kartaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Karta nije pronadjena"));
        savedKarta.setBrojSedista(brojSedista);
        savedKarta.setTip(tip);
        Karta karta = kartaRepository.save(savedKarta);
        return kartaMapper.toResponse(karta);
    }

    public void deleteKarta(Long id){
        kartaRepository.deleteById(id);
    }

    public List<KartaResponseDTO> getAllKarteForPutovanje(Long id){
        List<Karta> karte = kartaRepository.findAllByPutovanjeId(id);
        return karte.stream().map(kartaMapper::toResponse).toList();
    }

    public List<KartaResponseDTO> getAllKarteForRezervacija(Long id){
        List<Karta> karte = kartaRepository.findAllByRezervacijaId(id);
        return karte.stream().map(kartaMapper::toResponse).toList();

    }
}
