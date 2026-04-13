package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.OcenaMapper;
import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.OcenaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcenaService {

    private final OcenaRepository ocenaRepository;
    private final OcenaMapper ocenaMapper;
    private final KorisnikRepository korisnikRepository;
    private final PutovanjeRepository putovanjeRepository;

    public List<OcenaResponseDTO> getOceneByKorisnik(Long id) {
        List<Ocena> ocene = ocenaRepository.findAllByKorisnikId(id);
        return ocene.stream().map(ocenaMapper::toResponse).toList();
    }

    public List<OcenaResponseDTO> getOceneForPutovanje(Long id) {
        List<Ocena> ocene = ocenaRepository.findAllByPutovanjeId(id);
        return ocene.stream().map(ocenaMapper::toResponse).toList();
    }

    public OcenaResponseDTO createOcena(OcenaRequestDTO newOcena) {
        Ocena ocena = ocenaMapper.toEntity(newOcena);
        if(ocenaRepository.existsByKorisnikIdAndPutovanjeId(newOcena.getKorisnikId(), newOcena.getPutovanjeId())){
            throw new EntityAlreadyExistsException("Korisnik je već ocenio ovo putovanje");
        }
        Korisnik korisnik = korisnikRepository.findById(newOcena.getKorisnikId()).orElseThrow(
                () -> new EntityNotFoundException("Korisnik nije pronadjen")
        );
        Putovanje putovanje = putovanjeRepository.findById(newOcena.getPutovanjeId()).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        ocena.setKorisnik(korisnik);
        ocena.setPutovanje(putovanje);
        Ocena saved = ocenaRepository.save(ocena);
        return ocenaMapper.toResponse(saved);
    }

    public void deleteOcena(Long id) {
        ocenaRepository.deleteById(id);
    }


}
