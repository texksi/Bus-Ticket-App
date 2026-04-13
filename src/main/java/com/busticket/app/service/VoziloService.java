package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.VoziloMapper;
import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.VoziloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoziloService {

    private final VoziloRepository voziloRepository;
    private final VoziloMapper voziloMapper;
    private final KompanijaRepository kompanijaRepository;

    public VoziloResponseDTO getVoziloById(Long id) {
        Vozilo vozilo = voziloRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno"));
        return voziloMapper.toResponse(vozilo);
    }

    public List<VoziloResponseDTO> getAllVozilaForKompanija(Long kompanijaId) {
        List<Vozilo> vozila = voziloRepository.findAllByKompanijaId(kompanijaId);
        return vozila.stream().map(voziloMapper::toResponse).toList();
    }

    public VoziloResponseDTO createVozilo(VoziloRequestDTO newVozilo) {
        if (voziloRepository.existsByRegistracija(newVozilo.getRegistracija())) {
            throw new EntityAlreadyExistsException("Vozilo sa ovom registracijom vec postoji");
        }
        Vozilo vozilo = voziloMapper.toEntity(newVozilo);
        Kompanija kompanija = kompanijaRepository.findById(newVozilo.getKompanijaId()).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        vozilo.setKompanija(kompanija);
        Vozilo saved = voziloRepository.save(vozilo);
        return voziloMapper.toResponse(saved);
    }

    public VoziloResponseDTO updateVozilo(Long id, int kapacitet, String registracija, int brojRedova, int brojKolona) {
        Vozilo savedVozilo = voziloRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno")
        );
        savedVozilo.setKapacitet(kapacitet);
        savedVozilo.setRegistracija(registracija);
        savedVozilo.setBrojKolona(brojKolona);
        savedVozilo.setBrojRedova(brojRedova);
        Vozilo vozilo = voziloRepository.save(savedVozilo);
        return voziloMapper.toResponse(vozilo);
    }

    public void deleteVozilo(Long id) {
        voziloRepository.deleteById(id);
    }


}
