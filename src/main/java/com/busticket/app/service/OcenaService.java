package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.OcenaMapper;
import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.model.entity.Ocena;
import com.busticket.app.model.entity.Putovanje;
import com.busticket.app.repository.KorisnikRepository;
import com.busticket.app.repository.OcenaRepository;
import com.busticket.app.repository.PutovanjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje ocenama
 * Omogucava funkcionalnosti: kreiranje ocene, pregled ocena po korisniku i putovanjima, brisanje ocena, takodje
 * uvedena je provera da korisnik ne moze dva puta oceniti isto putovanje
 */
@Service
@RequiredArgsConstructor
public class OcenaService {

    private final OcenaRepository ocenaRepository;
    private final OcenaMapper ocenaMapper;
    private final KorisnikRepository korisnikRepository;
    private final PutovanjeRepository putovanjeRepository;

    /**
     * Metoda koja vraca listu ocena na osnovu prosledjenog ID-a korisnika, vrsi se provera da li korisnik za koga se
     * izvlace ocene postoji u sistemu, ako ne postoji baca se custom Exception i prekida metoda.
     *
     * @param id - jedinstveni indetifikator korisnika za koga se pronalaze ocene
     * @return List<OcenaResponseDTO> - lista objekata OcenaResponseDTO, gde OcenaResponseDTO
     * predstavlja jednu ocenu i sadrzi njene podatke
     * @throws EntityNotFoundException - ako korisnik za koga se izvlace ocene ne postoji u sistemu baca se custom
     *                                 Exception sa porukom "Korisnik ne postoji"
     */
    public List<OcenaResponseDTO> getOceneByKorisnik(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Korisnik ne postoji")
        );
        List<Ocena> ocene = ocenaRepository.findAllByKorisnikId(id);
        return ocene.stream().map(ocenaMapper::toResponse).toList();
    }

    /**
     * Metoda koja vraca listu ocena na osnovu prosledjenog ID-a putovanja, vrsi se provera da li putovanje za koga se
     * izvlace ocene postoji u sistemu, ako ne postoji baca se custom Exception i prekida metoda.
     *
     * @param id - jedinstveni indetifikator putovanja za koje se pronalaze ocene
     * @return List<OcenaResponseDTO> - lista objekata OcenaResponseDTO, gde OcenaResponseDTO
     * predstavlja jednu ocenu i sadrzi njene podatke
     * @throws EntityNotFoundException - ako putovanje za koje se izvlace ocene ne postoji u sistemu baca se custom
     *                                 Exception sa porukom "Putovanje nije pronadjeno"
     */
    public List<OcenaResponseDTO> getOceneForPutovanje(Long id) {
        Putovanje putovanje = putovanjeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        List<Ocena> ocene = ocenaRepository.findAllByPutovanjeId(id);
        return ocene.stream().map(ocenaMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novu ocenu na osnovu parametra koji se nalaze u objektu OcenaRequestDTO koji
     * je ulazni parametar. Pri kreiranju ocene proverava se da li u sistemu postoji korisnik i putovanje za koga
     * se ona kreira. Ako ne postoje, baca se custom Exception. Takodje proverava se da li je korisnik vec ocenio
     * to putovanje, jer ne moze oceniti vise puta isto putovanje
     *
     * @param newOcena - objekat koji sadrzi podatke za kreiranje nove ocene
     * @return OcenaResponseDTO - objekat koji vraca podatke novosacuvane ocene
     * @throws EntityNotFoundException      - ukoliko korisnik/putovanje za koga se kreira ocena ne postoje baca se custom
     *                                      exception sa porukom "Korisnik nije pronadjen"/"Putovanje nije pronadjeno"
     * @throws EntityAlreadyExistsException - ukoliko je korisnik vec ocenio odredjeno putovanje baca se exception sa
     *                                      porukom "Korisnik je već ocenio ovo putovanje"
     */
    public OcenaResponseDTO createOcena(OcenaRequestDTO newOcena) {
        Korisnik korisnik = korisnikRepository.findById(newOcena.getKorisnikId()).orElseThrow(
                () -> new EntityNotFoundException("Korisnik nije pronadjeno")
        );
        Putovanje putovanje = putovanjeRepository.findById(newOcena.getPutovanjeId()).orElseThrow(
                () -> new EntityNotFoundException("Putovanje nije pronadjeno")
        );
        Ocena ocena = ocenaMapper.toEntity(newOcena);
        if (ocenaRepository.existsByKorisnikIdAndPutovanjeId(newOcena.getKorisnikId(), newOcena.getPutovanjeId())) {
            throw new EntityAlreadyExistsException("Korisnik je već ocenio ovo putovanje");
        }
        ocena.setKorisnik(korisnik);
        ocena.setPutovanje(putovanje);
        Ocena saved = ocenaRepository.save(ocena);
        return ocenaMapper.toResponse(saved);
    }

    /**
     * Metoda za brisanje ocene iz baze na osnovu prosledjenog ID parametra, ukoliko ocena sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise ocena
     * @throws EntityNotFoundException - ukoliko ocena sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Ocena nije pronadjena"
     */
    public void deleteOcena(Long id) {
        Ocena ocena = ocenaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ocena nije pronadjena")
        );
        ocenaRepository.deleteById(id);
    }


}
