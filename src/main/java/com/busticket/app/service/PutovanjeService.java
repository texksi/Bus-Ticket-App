package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.PutovanjeMapper;
import com.busticket.app.model.dto.RequestDTOs.PutovanjeRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.PutovanjeResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.VoziloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servis za upravljanje putovanjima
 * Omogucava sledece funkcionalnosti: pregled putovanja, kreiranje putovanja, azuriranje putovanja, brisanje putovanja
 *
 */
@Service
@RequiredArgsConstructor
public class PutovanjeService {

    private final PutovanjeRepository putovanjeRepository;
    private final PutovanjeMapper putovanjeMapper;
    private final VoziloRepository voziloRepository;
    private final KompanijaRepository kompanijaRepository;

    /**
     * Metoda koja pronalazi i vraca putovanje na osnovu prosledjenog ID-a, prvo proverava da li to putovanje postoji
     * u sistemu, u slucaju da ne postoji baca custom Exception
     *
     * @param id - jedinstveni indetifikator putovanja na osnovu koga se radi pretraga
     * @return PutovanjeResponseDTO - objekat koji sadrzi podatke pronadjenog putovanja
     * @throws EntityNotFoundException - ukoliko putovanje sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Putovanje nije pronadjeno"
     */
    public PutovanjeResponseDTO getPutovanjeById(Long id) {
        Putovanje putovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno"));
        return putovanjeMapper.toResponse(putovanje);
    }

    /**
     * Metoda koja pronalazi i vraca sva putovanja u sistemu
     *
     * @return List<PutovanjeResponseDTO> - lista objekata PutovanjeResponseDTO, gde PutovanjeResponseDTO
     * predstavlja jedno putovanje i sadrzi njegove podatke
     */
    public List<PutovanjeResponseDTO> getAllPutovanja() {
        List<Putovanje> putovanja = putovanjeRepository.findAll();
        return putovanja.stream().map(putovanjeMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novo putovanje na osnovu parametra koji se nalaze u objektu PutovanjeRequestDTO koji
     * je ulazni parametar. Pri kreiranju putovanja proverava se da li u sistemu postoji vozilo i kompanija za koga
     * se ona kreira. Ako ne postoje, baca se custom Exception
     *
     * @param newPutovanje - objekat koji sadrzi podatke za kreiranje novog putovanja
     * @return PutovanjeResponseDTO - objekat koji vraca podatke novosacuvanog putovanja
     * @throws EntityNotFoundException - ukoliko ne postoji vozilo ili kompanija sa odgovaracujim ID-om za koga
     *                                 se kreira karta baca se custom exception u zavisnosti od nedostajuceg parametra sa
     *                                 porukom "Vozilo nije pronadjeno"/"Kompanija nije pronadjena"
     */
    public PutovanjeResponseDTO createPutovanje(PutovanjeRequestDTO newPutovanje) {
        Putovanje putovanje = putovanjeMapper.toEntity(newPutovanje);
        Vozilo vozilo = voziloRepository.findById(newPutovanje.getVoziloId()).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno")
        );
        Kompanija kompanija = kompanijaRepository.findById(newPutovanje.getKompanijaId()).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        putovanje.setVozilo(vozilo);
        putovanje.setKompanija(kompanija);
        Putovanje saved = putovanjeRepository.save(putovanje);
        return putovanjeMapper.toResponse(saved);
    }

    /**
     * Metoda koja azurira vec postojece putovanje, na osnovu prosledjenog ID-a pronalazi putovanje u bazi i azurira njegove
     * vrednosti, u slucaju da putovanje sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad
     *
     * @param id           - jedinstveni indetifikator koji se korisiti za pronalazenje putovanja kojeg treba azurirati
     * @param polaziste    - parametar za promenu polazista
     * @param odrediste    - parametar za promenu odredista
     * @param vremePolaska - parametar za promenu vremena polaska
     * @param vremeDolaska - parametar za promenu vremena dolaska
     * @param osnovnaCena  - parametar za promenu osnovne cene
     * @return PutovanjeResponseDTO - objekat koji vraca azurirano putovanje sa svim njegovim podacima
     * @throws EntityNotFoundException - ukoliko putovanje sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Putovanje nije pronadjeno"
     */
    public PutovanjeResponseDTO updatePutovanje(Long id, String polaziste, String odrediste, LocalDateTime vremePolaska, LocalDateTime vremeDolaska, double osnovnaCena) {
        Putovanje savedPutovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        savedPutovanje.setPolaziste(polaziste);
        savedPutovanje.setOdrediste(odrediste);
        savedPutovanje.setVremePolaska(vremePolaska);
        savedPutovanje.setVremeDolaska(vremeDolaska);
        savedPutovanje.setOsnovnaCena(osnovnaCena);
        Putovanje putovanje = putovanjeRepository.save(savedPutovanje);
        return putovanjeMapper.toResponse(putovanje);
    }

    /**
     * Metoda za brisanje putovanja iz baze na osnovu prosledjenog ID parametra, ukoliko putovanje sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise putovanje
     * @throws EntityNotFoundException - ukoliko putovanje sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Putovanje nije pronadjeno"
     */
    public void deletePutovanje(Long id) {
        Putovanje putovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        putovanjeRepository.deleteById(id);
    }

    /**
     * Metoda koja pronalazi i vraca listu putovanja od kompanije, na osnovu prosledjenog ID-a kompanije, vrsi se provera
     * da li kompanija sa datim ID-om postoji, ako ne postoji baca se custom Exception i prekida rad procesa
     *
     * @param kompanijaId - jedinstveni indetifikator kompanije na osnovu koga se pronalaze putovanja
     * @return List<PutovanjeResponseDTO> - lista objekata PutovanjeResponseDTO, gde PutovanjeResponseDTO
     * predstavlja jedno putovanje i sadrzi njegove podatke
     * @throws EntityNotFoundException - ako kompanija sa datim ID-om ne postoji baca se custom exception sa porukom
     *                                 "Kompanija nije pronadjena"
     */
    public List<PutovanjeResponseDTO> getPutovanjaByKompanija(Long kompanijaId) {
        Kompanija kompanija = kompanijaRepository.findById(kompanijaId).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        List<Putovanje> putovanja = putovanjeRepository.findAllByKompanijaId(kompanijaId);
        return putovanja.stream().map(putovanjeMapper::toResponse).toList();
    }

}
