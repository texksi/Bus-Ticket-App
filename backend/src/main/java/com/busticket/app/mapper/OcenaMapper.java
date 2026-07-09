package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.OcenaRequestDTO;
import com.busticket.app.model.dto.response.OcenaResponseDTO;
import com.busticket.app.model.entity.Ocena;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OcenaMapper {

    @Mapping(source = "korisnik.id", target = "korisnikId")
    @Mapping(source = "putovanje.id", target = "putovanjeId")
    @Mapping(source = "ocenaVrednost", target = "ocena")
    OcenaResponseDTO toResponse(Ocena ocena);

    @Mapping(target = "korisnik", ignore = true)
    @Mapping(target = "putovanje", ignore = true)
    @Mapping(target = "id", ignore = true)
    Ocena toEntity(OcenaRequestDTO requestDTO);
}
