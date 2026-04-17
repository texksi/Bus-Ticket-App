package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.VoziloMapper;
import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.model.entity.Vozilo;
import com.busticket.app.repository.KompanijaRepository;
import com.busticket.app.repository.VoziloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje vozilima
 * Omogucava sledece funkcionalnosti: pretragu vozila, kreiranje vozila, azuriranje vozila i birasanje vozila
 */
@Service
@RequiredArgsConstructor
public class VoziloService {

    private final VoziloRepository voziloRepository;
    private final VoziloMapper voziloMapper;
    private final KompanijaRepository kompanijaRepository;

    /**
     * Metoda koja pronalazi i vraca vozilo na osnovu prosledjenog ID-a, prvo proverava da li to vozilo postoji
     * u sistemu, u slucaju da ne postoji baca custom Exception
     *
     * @param id - jedinstveni indetifikator vozila na osnovu koga se radi pretraga
     * @return VoziloResponseDTO - objekat koji sadrzi podatke pronadjenog vozila
     * @throws EntityNotFoundException - ukoliko vozilo sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Vozilo nije pronadjeno"
     */
    public VoziloResponseDTO getVoziloById(Long id) {
        Vozilo vozilo = voziloRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno"));
        return voziloMapper.toResponse(vozilo);
    }

    /**
     * Metoda koja pronalazi i vraca listu vozila od kompanije, na osnovu prosledjenog ID-a kompanije, vrsi se provera
     * da li kompanija sa datim ID-om postoji, ako ne postoji baca se custom Exception i prekida rad procesa
     *
     * @param kompanijaId - jedinstveni indetifikator kompanije na osnovu koga se pronalaze vozila
     * @return List<VoziloResponseDTO> - lista objekata VoziloResponseDTO, gde VoziloResponseDTO
     * predstavlja jedno vozilo i sadrzi njegove podatke
     * @throws EntityNotFoundException - ako kompanija sa datim ID-om ne postoji baca se custom exception sa porukom
     *                                 "Kompanija nije pronadjena"
     */
    public List<VoziloResponseDTO> getAllVozilaForKompanija(Long kompanijaId) {
        Kompanija kompanija = kompanijaRepository.findById(kompanijaId).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        List<Vozilo> vozila = voziloRepository.findAllByKompanijaId(kompanijaId);
        return vozila.stream().map(voziloMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novo vozilo na osnovu parametra koji se nalaze u objektu VoziloRequestDTO koji
     * je ulazni parametar. Pri kreiranju vozila proverava se da li u sistemu postoji  kompanija za koju
     * se ono kreira. Ako ne postoji, baca se custom Exception, takdoje vrsi se i jedinstvena provera registracije vozila
     *
     * @param newVozilo - objekat koji sadrzi podatke za kreiranje novog vozila
     * @return VoziloResponseDTO - objekat koji vraca podatke novosacuvanog vozila
     * @throws EntityNotFoundException      - ukoliko ne postoji komoanija sa odgovaracujim ID-om za koga
     *                                      se kreira vozilo baca se custom exception sa
     *                                      porukom "Kompanija nije pronadjena"
     * @throws EntityAlreadyExistsException - ukoliko vozilo koje se kreira ima istu registraciju kao neko vozilo
     *                                      koje vec postoji u sistemu baca se custom Exception sa porukom
     *                                      "Vozilo sa ovom registracijom vec postoji"
     */
    public VoziloResponseDTO createVozilo(VoziloRequestDTO newVozilo) {
        if (voziloRepository.existsByRegistracija(newVozilo.getRegistracija())) {
            throw new EntityAlreadyExistsException("Vozilo sa ovom registracijom vec postoji");
        }
        Kompanija kompanija = kompanijaRepository.findById(newVozilo.getKompanijaId()).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        Vozilo vozilo = voziloMapper.toEntity(newVozilo);
        vozilo.setKompanija(kompanija);
        Vozilo saved = voziloRepository.save(vozilo);
        return voziloMapper.toResponse(saved);
    }

    /**
     * Metoda koja azurira vec postojece vozilo, na osnovu prosledjenog ID-a pronalazi vozilo u bazi i azurira njegove
     * vrednosti, u slucaju da vozilo sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad,
     * takodje radi se provera jedinstvenosti registracije
     *
     * @param id           - jedinstveni indetifikator koji se korisiti za pronalazenje vozila kojeg treba azurirati
     * @param kapacitet    - parametar za promenu kapaciteta
     * @param registracija - parametar za promenu registracije
     * @param brojRedova   - parametar za promenu broja redova
     * @param brojKolona   - parametar za prmenu broja kolona
     * @return VoziloResponseDTO - objekat koji vraca azurirano vozilo sa svim njegovim podacima
     * @throws EntityNotFoundException      - ukoliko vozilo sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                      sa porukom "Vozilo nije pronadjeno"
     * @throws EntityAlreadyExistsException - ukoliko vozilo koje se azurira ima istu registraciju kao neko vozilo
     *                                      koje vec postoji u sistemu baca se custom Exception sa porukom
     *                                      "Vozilo sa ovom registracijom vec postoji"
     */
    public VoziloResponseDTO updateVozilo(Long id, int kapacitet, String registracija, int brojRedova, int brojKolona) {
        Vozilo savedVozilo = voziloRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno")
        );
        if (voziloRepository.existsByRegistracija(registracija)) {
            throw new EntityAlreadyExistsException("Vozilo sa ovom registracijom vec postoji");
        }
        savedVozilo.setKapacitet(kapacitet);
        savedVozilo.setRegistracija(registracija);
        savedVozilo.setBrojKolona(brojKolona);
        savedVozilo.setBrojRedova(brojRedova);
        Vozilo vozilo = voziloRepository.save(savedVozilo);
        return voziloMapper.toResponse(vozilo);
    }

    /**
     * Metoda za brisanje vozila iz baze na osnovu prosledjenog ID parametra, ukoliko vozilo sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise vozilo
     * @throws EntityNotFoundException - ukoliko vozilo sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Vozilo nije pronadjeno"
     */
    public void deleteVozilo(Long id) {
        Vozilo vozilo = voziloRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vozilo nije pronadjeno")
        );
        voziloRepository.deleteById(id);
    }


}
