package com.example.server.controllers;

import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.services.ProvinceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProvinceController {

    ProvinceService provinceService;

    @PostMapping
    ResponseEntity<ProvinceResponse> createProvince(@RequestBody ProvinceRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(provinceService.createProvince(request));
    }

    @GetMapping
    ResponseEntity<List<ProvinceResponse>> getProvinces() {
        return ResponseEntity.status(HttpStatus.OK).body(provinceService.getProvinces());
    }

    @PostMapping("/{id}")
    ResponseEntity<ProvinceResponse> updateProvince(@PathVariable("id") Long id, ProvinceRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(provinceService.updateProvince(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProvince(@PathVariable("id") Long id){
        provinceService.deleteProvince(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
