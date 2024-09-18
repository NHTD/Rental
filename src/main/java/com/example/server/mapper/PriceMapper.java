package com.example.server.mapper;

import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.PriceResponse;
import com.example.server.models.Price;
import com.example.server.models.Province;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    Price priceToPrice(PriceRequest request);
    PriceResponse priceToPriceResponse(Price price);

    void priceToUpdatePrice(@MappingTarget Price price, PriceRequest request);
}
