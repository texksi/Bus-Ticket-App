package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.PlacanjeRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.PlacanjeResponseDTO;
import com.busticket.app.model.entity.Placanje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacanjeMapper {

    @Mapping(source = "rezervacija.id", target = "rezervacijaId")
    PlacanjeResponseDTO toResponse(Placanje placanje);

    Placanje toEntity(PlacanjeRequestDTO requestDTO);
}
