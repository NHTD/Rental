package com.example.server.repositories;

import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Query("SELECT p.code FROM Province p")
    List<String> findAllProvinceCodes();

    @Query("SELECT p.value FROM Province p")
    List<String> findAllProvinceValues();

    Optional<Province> findProvinceByCode(String code);

    @Query("SELECT p.code FROM Province p WHERE p.value LIKE %:value%")
    ProvinceResponse findProvinceCodeByValue(String value);

    void deleteByCode(String code);
}
