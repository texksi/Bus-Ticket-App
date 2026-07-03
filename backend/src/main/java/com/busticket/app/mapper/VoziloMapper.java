package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.VoziloRequestDTO;
import com.busticket.app.model.dto.response.VoziloResponseDTO;
import com.busticket.app.model.entity.Vozilo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoziloMapper {

    @Mapping(source = "kompanija.id", target = "kompanijaId")
    VoziloResponseDTO toResponse(Vozilo vozilo);

    Vozilo toEntity(VoziloRequestDTO requestDTO);
}
