package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.PutovanjeRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.PutovanjeResponseDTO;
import com.busticket.app.model.entity.Putovanje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PutovanjeMapper {

    @Mapping(source = "kompanija.id", target = "kompanijaId")
    @Mapping(source = "vozilo.id", target = "voziloId")
    PutovanjeResponseDTO toResponse(Putovanje putovanje);

    Putovanje toEntity(PutovanjeRequestDTO requestDTO);
}
