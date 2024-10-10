package com.example.server.controllers;

import com.example.server.dtos.request.DistrictRequest;
import com.example.server.dtos.response.DistrictResponse;
import com.example.server.services.DistrictService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DistrictController {

    DistrictService districtService;

    @PostMapping
    ResponseEntity<DistrictResponse> createDistrict(@RequestBody DistrictRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(districtService.createDistrict(request));
    }

    @GetMapping
    ResponseEntity<List<DistrictResponse>> getDistricts() {
        return ResponseEntity.status(HttpStatus.OK).body(districtService.getDistricts());
    }

    @GetMapping("/{provinceCode}")
    ResponseEntity<List<DistrictResponse>> getDistrictByProvinceId(@PathVariable("provinceCode") String provinceCode) {
        return ResponseEntity.status(HttpStatus.OK).body(districtService.getDistrictsByProvinceCode(provinceCode));
    }

}
