package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.RezervacijaMapper;
import com.busticket.app.model.dto.request.RezervacijaRequestDTO;
import com.busticket.app.model.dto.response.RezervacijaResponseDTO;
import com.busticket.app.model.entity.Karta;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servis za upravljanje rezervacijama
 * Omogucava sledece funkcionalnosti: kreiranje rezervacija, pregled rezervacija, izmena rezervacija i brisanje
 * rezervacija
 */
@Service
@RequiredArgsConstructor
public class RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final KorisnikRepository korisnikRepository;
    private static final String REZERVACIJA_NOT_FOUND = "Rezervacija nije pronadjena";

    /**
     * Metoda koja pronalazi i vraca rezervaciju na osnovu prosledjenog ID parametra
     * Ukoliko rezervacija sa prosledjenim ID-om ne postoji u sistemu metoda baca custom Exception
     *
     * @param id  - jedinstveni indetifikator rezervacije
     * @return RezervacijaResponseDTO - objekat koji sadrzi podatke pronadjene rezervacija
     * @throws EntityNotFoundException - ukoliko rezervacija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Rezervacija nije pronadjena"
     */
    public RezervacijaResponseDTO getRezervacijaById(Long id){
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(REZERVACIJA_NOT_FOUND));
        return rezervacijaMapper.toResponse(rezervacija);
    }

    /**
     * Metoda koja pronalazi i vraca sve rezervacije u sistemu
     *
     * @return List<RezervacijaResponseDTO> - lista objekata RezervacijaResponseDTO, gde RezervacijaResponseDTO
     * predstavlja jednu rezervaciju i sadrzi njene podatke
     */
    public List<RezervacijaResponseDTO> getAllRezervacije(){
        List<Rezervacija> rezervacije = rezervacijaRepository.findAll();
        return rezervacije.stream().map(rezervacijaMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novu rezervaciju na osnovu parametra koji se nalaze u objektu RezervacijaRequestDTO koji
     * je ulazni parametar. Pri kreiranju rezervacije proverava se da li u sistemu postoji korisnik za koga se ona
     * kreira. Ako korisnik ne postoji, baca se custom Exception
     *
     * @param newRezervacija - objekat koji sadrzi podatke za kreiranje nove rezervacije
     * @return RezervacijaResponseDTO - objekat koji vraca podatke novosacuvane rezervacije
     * @throws EntityNotFoundException - ukoliko ne postoji korisnik sa odgovaracujim ID-om za koga se kreira rezervacija
     * baca se custom exception sa porukom "Korisnik ne postoji"
     */
    public RezervacijaResponseDTO createRezervacija(RezervacijaRequestDTO newRezervacija) {
        Korisnik korisnik = korisnikRepository.findById(newRezervacija.getKorisnikId())
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        Rezervacija rezervacija = rezervacijaMapper.toEntity(newRezervacija);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setUkupanIznos(newRezervacija.getUkupanIznos());
        rezervacija.setStatus(newRezervacija.getStatus());
        rezervacija.setNacinPlacanja(newRezervacija.getNacinPlacanja());
        return rezervacijaMapper.toResponse(rezervacijaRepository.save(rezervacija));
    }

    /**
     * Metoda koja azurira vec postojecu rezervaciju, na osnovu prosledjenog ID-a pronalazi rezervaciju u bazi i
     * azurira njene podatke, u slucaju da rezervacija sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad,
     *
     * @param id - jedinstveni indetifikator koji se korisiti za pronalazenje rezervacije koju treba azurirati
     * @param dto - ojbekat rezervazija za azuriranje
     * @return RezervacijaResponseDTO - objekat koji vraca azuriranu rezervaciju sa svim njenim podacima
     * @throws EntityNotFoundException - ukoliko rezervacija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Rezervacija nije pronadjena"
     */
    public RezervacijaResponseDTO updateRezervacija(Long id, RezervacijaRequestDTO dto){
        Rezervacija savedRezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(REZERVACIJA_NOT_FOUND));
        savedRezervacija.setStatus(dto.getStatus());
        savedRezervacija.setNacinPlacanja(dto.getNacinPlacanja());
        Rezervacija rezervacija = rezervacijaRepository.save(savedRezervacija);
        return rezervacijaMapper.toResponse(rezervacija);
    }

    /**
     * Metoda za brisanje rezervacija iz baze na osnovu prosledjenog ID parametra, ukoliko rezervacija sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise rezervacija
     * @throws EntityNotFoundException - ukoliko rezervacija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Rezervacija nije pronadjena"
     */
    public void deleteRezervacija(Long id){
        rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(REZERVACIJA_NOT_FOUND)
        );
        rezervacijaRepository.deleteById(id);
    }

    /**
     * Metoda koja prima jedinstveni indetifikator korisnika i na osnovu toga pronalazi i vraca listu svih rezervacija
     * koje pripadaju tom korisniku. Metoda takodje proverava da li korisnik za koga se pretrazuju rezervacije postoji,
     * ako ne postoji baca se custom Exception
     *
     * @param korisnikId - jedinstveni indentifikator korisnika za koga se traze rezervacije
     * @return List<RezervacijaResponseDTO> - lista rezervacija za prosledjenog korisnika
     * @throws EntityNotFoundException - custom exception koji se baca ukoliko korisnik za koga se traze rezervacije
     * ne postoji u sistemu sa porukom "Korisnik ne postoji"
     */
    public List<RezervacijaResponseDTO> getRezervacijeByKorisnik(Long korisnikId){
        korisnikRepository.findById(korisnikId).orElseThrow(
                () -> new EntityNotFoundException("Korisnik ne postoji")
        );
        List<Rezervacija> rezervacije = rezervacijaRepository.findAllByKorisnikId(korisnikId);
        return rezervacije.stream().map(rezervacijaMapper::toResponse).toList();
    }

    @Transactional
    public void azurirajUkupanIznos(Long rezervacijaId) {
        Rezervacija rezervacija = rezervacijaRepository.findById(rezervacijaId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija ne postoji"));

        double stvarniIznos = rezervacija.getKarte()
                .stream()
                .mapToDouble(Karta::getFinalnaCena)
                .sum();

        rezervacija.setUkupanIznos(stvarniIznos);
        rezervacijaRepository.save(rezervacija);
    }
}
