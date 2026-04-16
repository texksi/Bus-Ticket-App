package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KorisnikMapper;
import com.busticket.app.model.dto.RequestDTOs.KorisnikRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KorisnikResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import com.busticket.app.repository.KorisnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje korisnicima
 * Omogucava sledece funkcionalnosti: kreiranje korisnika, pregled korisnika, izmena korisnika i brisanje korisnika
 * Sadrzi takodje i proveru jedinstvenosti email adrese i username-a (ne mogu postojati dva korisnika sa istim email/username)
 */
@Service
@RequiredArgsConstructor
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;

    /**
     * Metoda koja pronalazi i vraca korisnika na osnovu prosledjenog ID parametra
     * Ukoliko korisnik sa prosledjenim ID-om ne postoji u sistemu metoda baca custom Exception
     *
     * @param id  - jedinstveni indetifikator korisnika
     * @return KorisnikResponseDTO - objekat koji sadrzi podatke pronadjenog korisnika
     * @throws EntityNotFoundException - ukoliko korisnik sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Korisnik ne postoji"
     */
    public KorisnikResponseDTO getKorisnikById(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Korisnik ne postoji"));
        return korisnikMapper.toResponse(korisnik);
    }

    /**
     * Metoda koja pronalazi i vraca korisnika na osnovu prosledjenog username parametra
     * Ukoliko korisnik sa prosledjenim username-om ne postoji u sistemu metoda baca custom Exception
     *
     * @param username - jedinstevni username korisnika
     * @return KorisnikResponseDTO - objekat koji sadrzi podatke pronadjenog korisnika
     * @throws EntityNotFoundException - ukoliko korisnik sa datim username-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Korisnik ne postoji"
     */
    public KorisnikResponseDTO getKorisnikByUsername(String username) {
        Korisnik korisnik = korisnikRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        return korisnikMapper.toResponse(korisnik);
    }

    /**
     * Metoda koja pronalazi i vraca sve korisnike u sistemu
     *
     * @return List<KorisnikResponseDTO> - lista objekata KorisnikResponseDTO, gde KorisnikResponseDTO predstavlja
     * jednog korisnika i sadrzi njegove podatke
     */
    public List<KorisnikResponseDTO> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        return korisnici.stream().map(korisnikMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novog korisnika na osnovu parametra koji se nalaze u objektu KorisnikRequestDTO koji je ulazni
     * parametar. Ukoliko pri kreiranju korisnika vec postoji korisnik sa istim email/username-om, sistem prekida operaciju
     * i baca custom Exception.
     *
     * @param newKorisnik - objekat koji sadrzi podatke za kreiranje novog korisnika
     * @return KorisnikResponseDTO - objekat koji vraca podatke novosacuvanog korisnika
     * @throws EntityAlreadyExistsException - ukoliko korisnik sa istim username/email-om vec postoji u sistemu
     * baca se custom Exception sa porukom "Korisnik sa tim email-om ili username-om već postoji"
     */
    public KorisnikResponseDTO createKorisnik(KorisnikRequestDTO newKorisnik) {
        if (korisnikRepository.existsByEmail(newKorisnik.getEmail()) || korisnikRepository.existsByUsername(newKorisnik.getUsername())) {
            throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
        }
        Korisnik korisnik = korisnikRepository.save(korisnikMapper.toEntity(newKorisnik));
        return korisnikMapper.toResponse(korisnik);
    }

    /**
     * Metoda koja azurira vec postojeceg korisnika, na osnovu prosledjenog ID-a pronalazi korisnika u bazi, u slucaju
     * da korisnik sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad. Ako korisnik postoji, tada
     * se njegovi parametri azuriraju u skladu sa prosledjenim parametrima, a u slucaju promene email/username-a vrsi
     * se provera da li takav email/username postoji u bazi, ako postoji prekida se proces i baca custom Exception
     *
     * @param id - jedinstveni indetifikator koji se korisiti za pronalazenje korisnika koga treba azurirati
     * @param username - parametar za promenu username-a
     * @param email - parametar za promenu email-a
     * @param ime - parametar za promenu imena
     * @param prezime - parametar za promenu prezimena
     * @return KorisnikResponseDTO - objekat koji vraca azuriranog korisnika sa svim njegovim podacima
     * @throws EntityNotFoundException - ukoliko korisnik sa datim ID-om ne postoji u sistemu, baca se izuzetak
     * sa porukom "Korisnik ne postoji"
     * @throws EntityAlreadyExistsException - ukoliko korisnik sa istim username/email-om vec postoji u sistemu
     * baca se custom Exception sa porukom "Korisnik sa tim email-om ili username-om već postoji"
     */
    public KorisnikResponseDTO updateKorisnik(Long id, String username, String email, String ime, String prezime) {
        Korisnik savedKorisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Korisnik ne postoji"));
        if (!savedKorisnik.getUsername().equals(username) || !savedKorisnik.getEmail().equals(email)) {
            if (korisnikRepository.existsByEmail(email) || korisnikRepository.existsByUsername(username)) {
                throw new EntityAlreadyExistsException("Korisnik sa tim email-om ili username-om već postoji");
            }
        }
        savedKorisnik.setIme(ime);
        savedKorisnik.setPrezime(prezime);
        savedKorisnik.setEmail(email);
        savedKorisnik.setUsername(username);
        Korisnik korisnik = korisnikRepository.save(savedKorisnik);
        return korisnikMapper.toResponse(korisnik);
    }

    /**
     * Metoda za brisanje korisnika iz sistema na osnovu prosledjenog ID-a, ukoliko korisnik sa tim ID-om ne posotji
     * baca se customException
     *
     * @param id - jedinstveni indetifikator na osnovu kog se brise korisnik iz sistema
     *@throws EntityNotFoundException - ukoliko korisnik sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *sa porukom "Korisnik ne postoji"
     */
    public void deleteKorisnik(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Korisnik ne postoji")
        );
        korisnikRepository.deleteById(id);
    }
}
