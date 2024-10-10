package com.example.server.services;

import com.example.server.dtos.request.DistrictRequest;
import com.example.server.dtos.response.DistrictResponse;

import java.util.List;

public interface DistrictService {
    DistrictResponse createDistrict(DistrictRequest request);
    List<DistrictResponse> getDistricts();
    List<DistrictResponse> getDistrictsByProvinceCode(String provinceCode);
}
