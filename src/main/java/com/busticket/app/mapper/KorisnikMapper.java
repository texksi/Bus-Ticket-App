package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.KorisnikRequestDTO;
import com.busticket.app.model.dto.response.KorisnikResponseDTO;
import com.busticket.app.model.entity.Korisnik;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KorisnikMapper {

    KorisnikResponseDTO toResponse(Korisnik korisnik);

    Korisnik toEntity(KorisnikRequestDTO requestDTO);
}
