package com.example.server.services.servicesImp;

import com.example.server.dtos.request.ProvinceRequest;
import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.exception.RentalHomeDataInvalidException;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.ProvinceMapper;
import com.example.server.models.Province;
import com.example.server.repositories.ProvinceRepository;
import com.example.server.services.ProvinceService;
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
public class ProvinceServiceImp implements ProvinceService {

    ProvinceRepository provinceRepository;
    ProvinceMapper provinceMapper;

    @Override
    public ProvinceResponse createProvince(ProvinceRequest provinceRequest) {
        Optional<Province> provinceOptional = provinceRepository.findProvinceByCode(provinceRequest.getCode());
        if(provinceOptional.isPresent()){
            throw new RentalHomeDataInvalidException("This code {} is existed", provinceRequest.getCode());
        }

        Province province = provinceMapper.provinceToProvince(provinceRequest);

        return provinceMapper.provinceToProvinceResponse(provinceRepository.save(province));
    }

    @Override
    public List<ProvinceResponse> getProvinces() {
        return provinceRepository.findAll()
                .stream()
                .map(provinceMapper::provinceToProvinceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProvinceResponse updateProvince(Long id, ProvinceRequest provinceRequest) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This id {} is not found", id));

        provinceMapper.provinceToUpdateProvince(province, provinceRequest);

        return provinceMapper.provinceToProvinceResponse(provinceRepository.save(province));
    }

    @Override
    public void deleteProvince(Long id) {
        provinceRepository.deleteById(id);
    }
}
