package com.example.server.mapper;

import com.example.server.dtos.request.DistrictRequest;
import com.example.server.dtos.response.DistrictResponse;
import com.example.server.models.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    @Mapping(target = "province", ignore = true)
    District districtToDistrict(DistrictRequest request);

    DistrictResponse districtToDistrictResponse(District district);
}
