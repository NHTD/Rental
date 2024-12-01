package com.example.server.mapper;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.models.Area;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AreaMapper {
    Area areaToArea(AreaRequest request);
    AreaResponse areaToAreaResponse(Area area);

    void areaToUpdateArea(@MappingTarget Area area, AreaRequest request);
}
