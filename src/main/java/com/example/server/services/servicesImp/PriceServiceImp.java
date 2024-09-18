package com.example.server.services.servicesImp;

import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.response.PriceResponse;
import com.example.server.exception.RentalHomeDataInvalidException;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.PriceMapper;
import com.example.server.models.Price;
import com.example.server.models.Province;
import com.example.server.repositories.PriceRepository;
import com.example.server.services.PriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceServiceImp implements PriceService {

    PriceRepository priceRepository;
    PriceMapper priceMapper;

    @Override
    public PriceResponse createPrice(PriceRequest request) {
        Optional<Price> priceOptional = priceRepository.findPriceByCode(request.getCode());
        if(priceOptional.isPresent()){
            throw new RentalHomeDataInvalidException("This code {} is existed", request.getCode());
        }

        Price price = priceMapper.priceToPrice(request);

        return priceMapper.priceToPriceResponse(priceRepository.save(price));
    }

    @Override
    public List<PriceResponse> getPrices() {
        return priceRepository.findAll()
                .stream()
                .map(priceMapper::priceToPriceResponse).collect(Collectors.toList());
    }

    @Override
    public PriceResponse updatePrice(Long id, PriceRequest priceRequest) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This id {} is not found", id));

        priceMapper.priceToUpdatePrice(price, priceRequest);

        return priceMapper.priceToPriceResponse(priceRepository.save(price));
    }

    @Override
    public void deletePrice(Long id) {
        priceRepository.deleteById(id);
    }
}
