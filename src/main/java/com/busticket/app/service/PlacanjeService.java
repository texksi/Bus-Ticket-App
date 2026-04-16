package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PlacanjeMapper;
import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.model.entity.Placanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.PlacanjeRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje placanjima
 * Omogucava funkcionalnosti: pregled placanja i pregled placanja za rezervaciju
 */
@Service
@RequiredArgsConstructor
public class PlacanjeService {

    private final PlacanjeRepository placanjeRepository;
    private final PlacanjeMapper placanjeMapper;
    private final RezervacijaRepository rezervacijaRepository;

    /**
     * Metoda koja pronalazi i vraca placanje na osnovu prosledjenog ID-a, prvo proverava da li placanje postoji
     * u sistemu, u slucaju da ne postoji baca custom Exception
     *
     * @param id - jedinstveni indetifikator placanja na osnovu koga se radi pretraga
     * @return PlacanjeResponseDTO - objekat koji sadrzi podatke pronadjenog placanja
     * @throws EntityNotFoundException - ukoliko placanje sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Placanje nije pronadjena"
     */
    public PlacanjeResponseDTO getPlacanjeById(Long id) {
        Placanje placanje = placanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Placanje nije pronadjeno"));
        return placanjeMapper.toResponse(placanje);
    }

    /**
     * Metoda koja pronalazi i vraca sva placana vezana za rezervaciju, prvo proverava da li rezervacija za koju se
     * izvlace placanja postoji, ako ne postoji baca se custom Exception
     *
     * @param id - jedinstveni indentifikator rezervacije za koju se izvlace placanja
     * @return List<PlacanjeResponseDTO> - lista objekata PlacanjeResponseDTO, gde PlacanjeResponseDTO
     * predstavlja jedno placanje i sadrzi njene podatke
     * @throws EntityNotFoundException - ukoliko rezervacija za koju se izvlace placanja ne postoji u sistemu baca se
     *                                 custom exception sa porukom "Rezervacija nije pronadjena"
     */
    public List<PlacanjeResponseDTO> getPlacanjaForRezervacija(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena")
        );
        List<Placanje> placanja = placanjeRepository.findAllByRezervacijaId(id);
        return placanja.stream().map(placanjeMapper::toResponse).toList();
    }
}
