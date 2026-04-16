package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KartaMapper;
import com.busticket.app.model.dto.RequestDTOs.KartaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.KartaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje kartama
 * Omogucava sledece funkcionalmosti: pregled karata, kreiranje karte, azuriranje karte, brisanje karte, pregled
 * karata za rezervacije i putovanja
 */
@Service
@RequiredArgsConstructor
public class KartaService {

    private final KartaRepository kartaRepository;
    private final KartaMapper kartaMapper;
    private final RezervacijaRepository rezervacijaRepository;
    private final PutovanjeRepository putovanjeRepository;

    /**
     * Metoda koja pronalazi i vraca kartu na osnovu prosledjenog ID-a, prvo proverava da li ta karta uospte postoji
     * u sistemu, u slucaju da ne postoji baca custom Exception
     *
     * @param id - jedinstveni indetifikator karte
     * @return KartaResponseDTO - objekat koji sadrzi podatke pronadjene karte
     * @throws EntityNotFoundException - ukoliko karta sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Karta nije pronadjena"
     */
    public KartaResponseDTO getKartaById(Long id) {
        Karta karta = kartaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Karta nije pronadjena"));
        return kartaMapper.toResponse(karta);
    }

    /**
     * Metoda koja pronalazi i vraca sve karte u sistemu
     *
     * @return List<KartaResponseDTO> - lista objekata KartaResponseDTO, gde KartaResponseDTO
     * predstavlja jednu kartu i sadrzi njene podatke
     */
    public List<KartaResponseDTO> getAllKarte() {
        List<Karta> karte = kartaRepository.findAll();
        return karte.stream().map(kartaMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novu kartu na osnovu parametra koji se nalaze u objektu KartaRequestDTO koji
     * je ulazni parametar. Pri kreiranju karte proverava se da li u sistemu postoji rezervacija i putovanje za koga
     * se ona kreira. Ako ne postoje, baca se custom Exception
     *
     * @param newKarta - objekat koji sadrzi podatke za kreiranje nove karte
     * @return KartaResponseDTO - objekat koji vraca podatke novosacuvane karte
     * @throws EntityNotFoundException - ukoliko ne postoji putovanje ili rezervacija sa odgovaracujim ID-om za koga
     *                                 se kreira karta baca se custom exception u zavisnosti od nedostajuceg parametra sa porukom
     *                                 "Rezervacija nije pronadjena"/"Putovanje nije pronadjeno"
     */
    public KartaResponseDTO createKarta(KartaRequestDTO newKarta) {
        Karta karta = kartaMapper.toEntity(newKarta);
        Rezervacija rezervacija = rezervacijaRepository.findById(newKarta.getRezervacijaId()).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena")
        );
        karta.setRezervacija(rezervacija);
        Putovanje putovanje = putovanjeRepository.findById(newKarta.getPutovanjeId()).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        karta.setPutovanje(putovanje);
        Karta saved = kartaRepository.save(karta);
        return kartaMapper.toResponse(saved);
    }

    /**
     * Metoda koja azurira vec postojecu kartu, na osnovu prosledjenog ID-a pronalazi kartu u bazi i azurira njene
     * vrednosti, u slucaju da karta sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad
     *
     * @param id          - jedinstveni indetifikator koji se korisiti za pronalazenje karte koju treba azurirati
     * @param brojSedista - parametar za promenu brojaSedista
     * @param tip         - parametar za promenu tipa karte
     * @return KartaResponseDTO - objekat koji vraca azuriranu kartu sa svim njenim podacima
     * @throws EntityNotFoundException - ukoliko karta sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Karta nije pronadjena"
     */
    public KartaResponseDTO updateKarta(Long id, String brojSedista, String tip) {
        Karta savedKarta = kartaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Karta nije pronadjena"));
        savedKarta.setBrojSedista(brojSedista);
        savedKarta.setTip(tip);
        Karta karta = kartaRepository.save(savedKarta);
        return kartaMapper.toResponse(karta);
    }

    /**
     * Metoda za brisanje karte iz baze na osnovu prosledjenog ID parametra, ukoliko karta sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise karta
     * @throws EntityNotFoundException - ukoliko karta sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Karta nije pronadjena"
     */
    public void deleteKarta(Long id) {
        Karta karta = kartaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Karta nije pronadjena")
        );
        kartaRepository.deleteById(id);
    }

    /**
     * Metoda koja pronalazi i vraca listu karata za putovanje, na osnovu prosledjenog ID-a putovanja, vrsi se provera
     * da li putovanje sa datim ID-om postoji, ako ne postoji baca se custom Exception i prekida rad procesa
     *
     * @param id - jedinstveni indetntifikator putovanja za koga se pronalaze karte
     * @return List<KartaResponseDTO> - lista objekata KartaResponseDTO, gde KartaResponseDTO predstavlja jednu kartu
     * i sadrzi njene podatke
     * @throws EntityNotFoundException - ukoliko putovanje za koje se traze karte ne postoji u sistemu baca se
     *                                 custom exception sa porukom "Putovanje nije pronadjeno"
     */
    public List<KartaResponseDTO> getAllKarteForPutovanje(Long id) {
        Putovanje putovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        List<Karta> karte = kartaRepository.findAllByPutovanjeId(id);
        return karte.stream().map(kartaMapper::toResponse).toList();
    }

    /**
     * Metoda koja pronalazi i vraca listu karata za rezervaciju, na osnovu prosledjenog ID-a rezervacije, vrsi se
     * provera da li rezervacija sa datim ID-om postoji, ako ne postoji baca se custom Exception i prekida rad procesa
     *
     * @param id - jedinstveni indetntifikator rezervacije za koga se pronalaze karte
     * @return List<KartaResponseDTO> - lista objekata KartaResponseDTO, gde KartaResponseDTO predstavlja jednu kartu
     * i sadrzi njene podatke
     * @throws EntityNotFoundException - ukoliko rezervacija za koje se traze karte ne postoji u sistemu baca se
     *                                 custom exception sa porukom "Rezervacija nije pronadjena"
     */
    public List<KartaResponseDTO> getAllKarteForRezervacija(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena")
        );
        List<Karta> karte = kartaRepository.findAllByRezervacijaId(id);
        return karte.stream().map(kartaMapper::toResponse).toList();

    }
}
