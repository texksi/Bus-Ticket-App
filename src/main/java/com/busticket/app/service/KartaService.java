package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Karta;
import com.busticket.app.repository.KartaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KartaService {

    private final KartaRepository kartaRepository;

    public Karta getKartaById(Long id){
        return kartaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Karta nije pronađena"));
    }

    public List<Karta> getAllKarte(){
        return kartaRepository.findAll();
    }

    public Karta createKarta(Karta karta){
        return kartaRepository.save(karta);
    }

    public Karta updateKarta(Long id, String brojSedista, String tip){
        Karta savedKarta = getKartaById(id);
        savedKarta.setBrojSedista(brojSedista);
        savedKarta.setTip(tip);
        return kartaRepository.save(savedKarta);
    }

    public void deleteKarta(Long id){
        kartaRepository.deleteById(id);
    }

    public List<Karta> getAllKarteForPutovanje(Long id){
        return kartaRepository.findAllByPutovanjeId(id);
    }

    public List<Karta> getAllKarteForRezervacija(Long id){
        return kartaRepository.findAllByRezervacijaId(id);
    }
}
