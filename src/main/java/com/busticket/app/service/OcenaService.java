package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.repository.OcenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcenaService {

    private final OcenaRepository ocenaRepository;

    public List<Ocena> getOceneByKorisnik(Long id) {
        return ocenaRepository.findAllByKorisnikId(id);
    }

    public List<Ocena> getOceneForPutovanje(Long id) {
        return ocenaRepository.findAllByPutovanjeId(id);
    }

    public Ocena createOcena(Ocena ocena) {
        if(ocenaRepository.existsByKorisnikIdAndPutovanjeId(ocena.getKorisnik().getId(),ocena.getPutovanje().getId())){
            throw new EntityAlreadyExistsException("Korisnik je već ocenio ovo putovanje");
        }
        return ocenaRepository.save(ocena);
    }

    public void deleteOcena(Long id) {
        ocenaRepository.deleteById(id);
    }


}
