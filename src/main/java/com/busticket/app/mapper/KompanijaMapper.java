package com.busticket.app.mapper;

import com.busticket.app.model.dto.RequestDTOs.KompanijaRequestDTO;
import com.busticket.app.model.dto.ResponseDTOs.KompanijaResponseDTO;
import com.busticket.app.model.entity.Kompanija;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KompanijaMapper {

    KompanijaResponseDTO toResponse(Kompanija kompanija);

    Kompanija toEntity(KompanijaRequestDTO requestDTO);
}
