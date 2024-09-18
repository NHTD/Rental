package com.example.server.services.servicesImp;

import com.example.server.dtos.request.AreaRequest;
import com.example.server.dtos.response.AreaResponse;
import com.example.server.exception.RentalHomeDataInvalidException;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.AreaMapper;
import com.example.server.models.Area;
import com.example.server.models.Price;
import com.example.server.repositories.AreaRepository;
import com.example.server.services.AreaService;
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
public class AreaServiceImp implements AreaService {

    AreaRepository areaRepository;
    AreaMapper areaMapper;

    @Override
    public AreaResponse createArea(AreaRequest request) {
        Optional<Area> areaOptional = areaRepository.findAreaByCode(request.getCode());
        if(areaOptional.isPresent()){
            throw new RentalHomeDataInvalidException("This code {} is existed", request.getCode());
        }

        Area area = areaMapper.areaToArea(request);

        return areaMapper.areaToAreaResponse(areaRepository.save(area));
    }

    @Override
    public List<AreaResponse> getAreas() {
        return areaRepository.findAll()
                .stream()
                .map(areaMapper::areaToAreaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AreaResponse updateArea(Long id, AreaRequest request) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This id {} is not found", id));

        areaMapper.areaToUpdateArea(area, request);

        return areaMapper.areaToAreaResponse(areaRepository.save(area));
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }
}
