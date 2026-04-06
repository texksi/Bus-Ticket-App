package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.repository.PutovanjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PutovanjeService {

    private final PutovanjeRepository putovanjeRepository;

    public Putovanje getPutovanjeById(Long id){
        return putovanjeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Putovanje nije pronadjeno"));
    }

    public List<Putovanje> getAllPutovanja(){
        return putovanjeRepository.findAll();
    }

    public Putovanje createPutovanje(Putovanje putovanje){
        return putovanjeRepository.save(putovanje);
    }

    public Putovanje updatePutovanje(Long id, String polaziste, String odrediste, LocalDateTime vremePolaska, LocalDateTime vremeDolaska, double osnovnaCena){
        Putovanje savedPutovanje = getPutovanjeById(id);
        savedPutovanje.setPolaziste(polaziste);
        savedPutovanje.setOdrediste(odrediste);
        savedPutovanje.setVremePolaska(vremePolaska);
        savedPutovanje.setVremeDolaska(vremeDolaska);
        savedPutovanje.setOsnovnaCena(osnovnaCena);
        return putovanjeRepository.save(savedPutovanje);
    }

    public void deletePutovanje(Long id){
        putovanjeRepository.deleteById(id);
    }

    public List<Putovanje> getPutovanjaByKompanija(Long kompanijaId){
        return putovanjeRepository.findAllByKompanijaId(kompanijaId);
    }

}
