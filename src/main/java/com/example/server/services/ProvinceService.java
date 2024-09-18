package com.example.server.services;

import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.ProvinceResponse;

import java.util.List;

public interface ProvinceService {
    ProvinceResponse createProvince(ProvinceRequest provinceRequest);
    List<ProvinceResponse> getProvinces();
    ProvinceResponse updateProvince(Long id, ProvinceRequest provinceRequest);
    void deleteProvince(Long id);
}
