package com.example.server.controllers;

import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.PriceResponse;
import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.models.Price;
import com.example.server.services.PriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceController {

    PriceService priceService;

    @PostMapping
    ResponseEntity<PriceResponse> createPrice(@RequestBody PriceRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(priceService.createPrice(request));
    }

    @GetMapping
    ResponseEntity<List<PriceResponse>> getPrices() {
        return ResponseEntity.status(HttpStatus.OK).body(priceService.getPrices());
    }

    @PostMapping("/{id}")
    ResponseEntity<PriceResponse> updatePrice(@PathVariable("id") Long id, PriceRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(priceService.updatePrice(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProvince(@PathVariable("id") Long id){
        priceService.deletePrice(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
