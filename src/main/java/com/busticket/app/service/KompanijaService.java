package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KompanijaMapper;
import com.busticket.app.model.dto.RequestDTOs.KompanijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KompanijaResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.repository.KompanijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KompanijaService {

    private final KompanijaRepository kompanijaRepository;
    private final KompanijaMapper kompanijaMapper;

    public KompanijaResponseDTO getKompanijaById(Long id) {
        Kompanija kompanija = kompanijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronađena"));
        return kompanijaMapper.toResponse(kompanija);
    }

    public List<KompanijaResponseDTO> getAllKompanije() {
        List<Kompanija> kompanije = kompanijaRepository.findAll();
        return kompanije.stream().map(kompanijaMapper::toResponse).toList();
    }

    public KompanijaResponseDTO createKompanija(KompanijaRequestDTO newKompanija) {
        Kompanija kompanija = kompanijaMapper.toEntity(newKompanija);
        Kompanija saved = kompanijaRepository.save(kompanija);
        return kompanijaMapper.toResponse(saved);
    }

    public KompanijaResponseDTO updateKompanija(Long id, String naziv, String kontakt) {
        Kompanija savedKompanija = kompanijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        savedKompanija.setNaziv(naziv);
        savedKompanija.setKontakt(kontakt);
        Kompanija kompanija = kompanijaRepository.save(savedKompanija);
        return kompanijaMapper.toResponse(kompanija);
    }

    public void deleteKompanija(Long id) {
        kompanijaRepository.deleteById(id);
    }
}
