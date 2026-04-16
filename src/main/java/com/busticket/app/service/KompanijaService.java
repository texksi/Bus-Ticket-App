package com.busticket.app.service;

import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.KompanijaMapper;
import com.busticket.app.model.dto.RequestDTOs.KompanijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KompanijaResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import com.busticket.app.repository.KompanijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servis za upravljanje kompanijama
 * Omogucava sledece funkcionalnosti: pretraga kompanija, kreiranje kompanija, azuriranje kompanija , brisanje kompanija
 */
@Service
@RequiredArgsConstructor
public class KompanijaService {

    private final KompanijaRepository kompanijaRepository;
    private final KompanijaMapper kompanijaMapper;

    /**
     * Metoda koja pronalazi i vraca kompaniju na osnovu prosledjenog ID-a, prvo proverava da li kompanija postoji
     * u sistemu, u slucaju da ne postoji baca custom Exception
     *
     * @param id - jedinstveni indetifikator kompanije na osnovu koga se radi pretraga
     * @return KompanijaResponseDTO - objekat koji sadrzi podatke pronadjene kompanije
     * @throws EntityNotFoundException - ukoliko kompanija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Kompanija nije pronadjena"
     */
    public KompanijaResponseDTO getKompanijaById(Long id) {
        Kompanija kompanija = kompanijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena"));
        return kompanijaMapper.toResponse(kompanija);
    }

    /**
     * Metoda koja pronalazi i vraca sve kompanije u sistemu
     *
     * @return List<KompanijaResponseDTO> - lista objekata KompanijaResponseDTO, gde KompanijaResponseDTO
     * predstavlja jednu kompaniju i sadrzi njene podatke
     */
    public List<KompanijaResponseDTO> getAllKompanije() {
        List<Kompanija> kompanije = kompanijaRepository.findAll();
        return kompanije.stream().map(kompanijaMapper::toResponse).toList();
    }

    /**
     * Metoda koja kreira novu kompaniju na osnovu parametra koji se nalaze u objektu KompanijaRequestDTO koji
     * je ulazni parametar.
     *
     * @param newKompanija - objekat koji sadrzi podatke za kreiranje nove kompanije
     * @return KompanijaResponseDTO - objekat koji vraca podatke novosacuvane kompanije
     */
    public KompanijaResponseDTO createKompanija(KompanijaRequestDTO newKompanija) {
        Kompanija kompanija = kompanijaMapper.toEntity(newKompanija);
        Kompanija saved = kompanijaRepository.save(kompanija);
        return kompanijaMapper.toResponse(saved);
    }

    /**
     * Metoda koja azurira vec postojecu kompaniju, na osnovu prosledjenog ID-a pronalazi kompaniju u bazi i azurira
     * njene vrednosti, u slucaju da kompanija sa tim ID-om ne postoji metoda baca custom Exception i prekida se njen rad
     *
     * @param id      - jedinstveni indetifikator koji se korisiti za pronalazenje kompanije koju treba azurirati
     * @param naziv   - parametar za promenu naziva
     * @param kontakt - parametar za promenu kontakta
     * @return KompanijaResponseDTO - objekat koji vraca azuriranu kompaniju sa svim njenim podacima
     * @throws EntityNotFoundException - ukoliko kompanija koju je potrebno azurirati ne postoji u sistemu baca se
     *                                 custom Exception sa porukom "Kompanija nije pronadjena"
     */
    public KompanijaResponseDTO updateKompanija(Long id, String naziv, String kontakt) {
        Kompanija savedKompanija = kompanijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        savedKompanija.setNaziv(naziv);
        savedKompanija.setKontakt(kontakt);
        Kompanija kompanija = kompanijaRepository.save(savedKompanija);
        return kompanijaMapper.toResponse(kompanija);
    }

    /**
     * Metoda za brisanje kompanije iz baze na osnovu prosledjenog ID parametra, ukoliko kompanija sa tim ID-om ne
     * postoji baca se custom Exception
     *
     * @param id - jedinstveni indetifikator na osnovu koga se brise kompanija
     * @throws EntityNotFoundException - ukoliko kompanija sa datim ID-om ne postoji u sistemu, baca se izuzetak
     *                                 sa porukom "Kompanija nije pronadjena"
     */
    public void deleteKompanija(Long id) {
        Kompanija kompanija = kompanijaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Kompanija nije pronadjena")
        );
        kompanijaRepository.deleteById(id);
    }
}
