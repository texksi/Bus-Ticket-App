package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.RezervacijaMapper;
import com.busticket.app.model.dto.RequestDTOs.RezervacijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.RezervacijaResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Rezervacija;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.RezervacijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                () -> new EntityNotFoundException("Rezervacija nije pronadjena"));
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
    public RezervacijaResponseDTO createRezervacija(RezervacijaRequestDTO newRezervacija){
        Rezervacija rezervacija = rezervacijaMapper.toEntity(newRezervacija);
        Korisnik korisnik = korisnikRepository.findById(newRezervacija.getKorisnikId())
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        rezervacija.setKorisnik(korisnik);
        return rezervacijaMapper.toResponse(rezervacijaRepository.save(rezervacija));
    }

    /**
     * Metoda koja azurira vec postojecu rezervaciju, na osnovu prosledjenog ID-a pronalazi rezervaciju u bazi i
     * azurira njene podatke, u slucaju da rezervacija sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad,
     *
     * @param id - jedinstveni indetifikator koji se korisiti za pronalazenje rezervacije koju treba azurirati
     * @param status - parametar za promenu statusa
     * @param nacinPlacanja - parametar za promenu nacina placanja
     * @param ukupanIznos - parametar za promenu ukupnog iznosa
     * @return RezervacijaResponseDTO - objekat koji vraca azuriranu rezervaciju sa svim njenim podacima
     * @throws EntityNotFoundException - ukoliko rezervacija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Rezervacija nije pronadjena"
     */
    public RezervacijaResponseDTO updateRezervacija(Long id, String status, String nacinPlacanja, double ukupanIznos){
        Rezervacija savedRezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena"));
        savedRezervacija.setStatus(status);
        savedRezervacija.setNacinPlacanja(nacinPlacanja);
        savedRezervacija.setUkupanIznos(ukupanIznos);
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
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rezervacija nije pronadjena")
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
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElseThrow(
                () -> new EntityNotFoundException("Korisnik ne postoji")
        );
        List<Rezervacija> rezervacije = rezervacijaRepository.findAllByKorisnikId(korisnikId);
        return rezervacije.stream().map(rezervacijaMapper::toResponse).toList();
    }
}
