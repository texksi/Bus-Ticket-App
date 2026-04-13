package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.RezervacijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.RezervacijaResponseDTO;
import com.busticket.app.model.entity.Rezervacija;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RezervacijaMapper {

    @Mapping(source = "korisnik.id", target = "korisnikId")
    RezervacijaResponseDTO toResponse(Rezervacija rezervacija);

    Rezervacija toEntity(RezervacijaRequestDTO requestDTO);
}
