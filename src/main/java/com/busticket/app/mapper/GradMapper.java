package com.busticket.app.mapper;

import com.busticket.app.model.dto.request.GradRequestDTO;
import com.busticket.app.model.dto.response.GradResponseDTO;
import com.busticket.app.model.entity.Grad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GradMapper {

    GradResponseDTO toResponse(Grad grad);
    Grad toEntity(GradRequestDTO gradRequestDTO);
}
