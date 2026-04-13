package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.OcenaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.OcenaResponseDTO;
import com.busticket.app.model.entity.Ocena;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OcenaMapper {

    @Mapping(source = "korisnik.id", target = "korisnikId")
    @Mapping(source = "putovanje.id", target = "putovanjeId")
    OcenaResponseDTO toResponse(Ocena ocena);

    Ocena toEntity(OcenaRequestDTO requestDTO);
}
