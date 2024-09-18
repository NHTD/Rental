package com.example.server.services;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.dtos.response.PriceResponse;

import java.util.List;

public interface AreaService {
    AreaResponse createArea(AreaRequest request);
    List<AreaResponse> getAreas();
    AreaResponse updateArea(Long id, AreaRequest request);
    void deleteArea(Long id);
}
