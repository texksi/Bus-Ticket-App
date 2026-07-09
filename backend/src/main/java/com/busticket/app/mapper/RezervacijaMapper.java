package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.RezervacijaRequestDTO;
import com.busticket.app.model.dto.response.RezervacijaResponseDTO;
import com.busticket.app.model.entity.Rezervacija;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RezervacijaMapper {

    @Mapping(source = "korisnik.id", target = "korisnikId")
    RezervacijaResponseDTO toResponse(Rezervacija rezervacija);

    @Mapping(target = "korisnik", ignore = true)
    @Mapping(target = "karte", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "datumKreiranja", ignore = true)
    Rezervacija toEntity(RezervacijaRequestDTO requestDTO);
}
