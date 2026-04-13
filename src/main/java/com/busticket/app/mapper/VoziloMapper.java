package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.VoziloRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.VoziloResponseDTO;
import com.busticket.app.model.entity.Vozilo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoziloMapper {

    @Mapping(source = "kompanija.id", target = "kompanijaId")
    VoziloResponseDTO toResponse(Vozilo vozilo);

    Vozilo toEntity(VoziloRequestDTO requestDTO);
}
