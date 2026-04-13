package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.KartaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KartaResponseDTO;
import com.busticket.app.model.entity.Karta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KartaMapper {

    @Mapping(source = "rezervacija.id", target = "rezervacijaId")
    @Mapping(source = "putovanje.id", target = "putovanjeId")
    KartaResponseDTO toResponse(Karta karta);

    Karta toEntity(KartaRequestDTO requestDTO);
}
