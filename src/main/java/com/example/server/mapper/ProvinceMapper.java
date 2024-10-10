package com.example.server.mapper;

import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.models.Province;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProvinceMapper {
    Province provinceToProvince(ProvinceRequest request);
    ProvinceResponse provinceToProvinceResponse(Province province);

    void provinceToUpdateProvince(@MappingTarget Province province, ProvinceRequest request);
}
