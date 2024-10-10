package com.example.server.repositories;

import com.example.server.dtos.response.DistrictResponse;
import com.example.server.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    @Query("select d from District d join d.province p where p.code like %:provinceCode%")
    List<District> getDistrictsByProvinceCode(String provinceCode);
}
