package com.example.server.services.servicesImp;

import com.example.server.dtos.request.DistrictRequest;
import com.example.server.dtos.response.DistrictResponse;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.DistrictMapper;
import com.example.server.models.District;
import com.example.server.models.Province;
import com.example.server.repositories.DistrictRepository;
import com.example.server.repositories.ProvinceRepository;
import com.example.server.services.DistrictService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DistrictServiceImp implements DistrictService {

    DistrictRepository districtRepository;
    ProvinceRepository provinceRepository;

    DistrictMapper districtMapper;

    @Override
    public DistrictResponse createDistrict(DistrictRequest request) {
        Province province = provinceRepository.findProvinceByCode(request.getProvinceCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This province is not found"));

        District district = districtMapper.districtToDistrict(request);
        district.setProvince(province);

        return districtMapper.districtToDistrictResponse(districtRepository.save(district));
    }

    @Override
    public List<DistrictResponse> getDistricts() {
        List<District> districts = districtRepository.findAll();
        return districts.stream().map(districtMapper::districtToDistrictResponse).collect(Collectors.toList());
    }

    @Override
    public List<DistrictResponse> getDistrictsByProvinceCode(String provinceCode) {
        Province province = provinceRepository.findProvinceByCode(provinceCode)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This province is not found"));

        List<District> districts = districtRepository.getDistrictsByProvinceCode(province.getCode());

        return districts.stream().map(districtMapper::districtToDistrictResponse).collect(Collectors.toList());
    }
}
