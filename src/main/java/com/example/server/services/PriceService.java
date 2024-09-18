package com.example.server.services;

import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.PriceResponse;
import com.example.server.dtos.response.ProvinceResponse;

import java.util.List;

public interface PriceService {
    PriceResponse createPrice(PriceRequest request);
    List<PriceResponse> getPrices();
    PriceResponse updatePrice(Long id, PriceRequest priceRequest);
    void deletePrice(Long id);
}
