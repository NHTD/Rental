package com.example.server.controllers;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.services.AreaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AreaController {

    AreaService areaService;

    @PostMapping
    ResponseEntity<AreaResponse> createArea(@RequestBody AreaRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(areaService.createArea(request));
    }

    @GetMapping
    ResponseEntity<List<AreaResponse>> getAreas() {
        return ResponseEntity.status(HttpStatus.OK).body(areaService.getAreas());
    }

    @PostMapping("/{id}")
    ResponseEntity<AreaResponse> updatePrice(@PathVariable("id") Long id, AreaRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(areaService.updateArea(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProvince(@PathVariable("id") Long id){
        areaService.deleteArea(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
