package com.busticket.app.service;

import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.repository.KompanijaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KompanijaService {

    private final KompanijaRepository kompanijaRepository;

    public Kompanija getKompanijaById(Long id){
        return kompanijaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Kompanija> getAllKompanije(){
        return kompanijaRepository.findAll();
    }

    public Kompanija createKompanija(Kompanija kompanija){
        return kompanijaRepository.save(kompanija);
    }

    public Kompanija updateKompanija(Long id, String naziv, String kontakt){
        Kompanija savedKompanija = getKompanijaById(id);
        savedKompanija.setNaziv(naziv);
        savedKompanija.setKontakt(kontakt);
        return kompanijaRepository.save(savedKompanija);
    }

    public void deleteKompanija(Long id){
        kompanijaRepository.deleteById(id);
    }
}
