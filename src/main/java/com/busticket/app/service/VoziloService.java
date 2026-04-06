package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.VoziloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoziloService {

    private final VoziloRepository voziloRepository;

    public Vozilo getVoziloById(Long id) {
        return voziloRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vozilo nije pronadjeno"));
    }

    public List<Vozilo> getAllVozilaForKompanija(Long kompanijaId) {
        return voziloRepository.findAllByKompanijaId(kompanijaId);
    }

    public Vozilo createVozilo(Vozilo vozilo) {
        if (voziloRepository.existsByRegistracija(vozilo.getRegistracija())) {
            throw new EntityAlreadyExistsException("Vozilo sa ovom registracijom vec postoji");
        }
        return voziloRepository.save(vozilo);
    }

    public Vozilo updateVozilo(Long id, int kapacitet, String registracija, int brojRedova, int brojKolona) {
        Vozilo savedVozilo = getVoziloById(id);
        savedVozilo.setKapacitet(kapacitet);
        savedVozilo.setRegistracija(registracija);
        savedVozilo.setBrojKolona(brojKolona);
        savedVozilo.setBrojRedova(brojRedova);
        return voziloRepository.save(savedVozilo);
    }

    public void deleteVozilo(Long id) {
        voziloRepository.deleteById(id);
    }


}
