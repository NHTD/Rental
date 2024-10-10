package com.example.server.repositories;

import com.example.server.dtos.response.ProvinceResponse;
import com.example.server.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Query("select p.code from Province p")
    List<String> findAllProvinceCodes();

    @Query("select p.value from Province p")
    List<String> findAllProvinceValues();

    Optional<Province> findProvinceByCode(String code);

    @Query("select p from Province p where p.value like %:value%")
    Province findProvinceCodeByValue(String value);

    void deleteByCode(String code);
}
