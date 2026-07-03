package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.PutovanjeRequestDTO;
import com.busticket.app.model.dto.response.PutovanjeResponseDTO;
import com.busticket.app.model.entity.Putovanje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PutovanjeMapper {

    @Mapping(source = "kompanija.id", target = "kompanijaId")
    @Mapping(source = "vozilo.id", target = "voziloId")
    @Mapping(source = "polaziste.id", target = "polazisteId")
    @Mapping(source = "odrediste.id", target = "odredisteId")
    PutovanjeResponseDTO toResponse(Putovanje putovanje);

    Putovanje toEntity(PutovanjeRequestDTO requestDTO);
}
