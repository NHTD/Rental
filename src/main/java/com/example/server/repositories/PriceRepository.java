package com.example.server.repositories;

import com.example.server.models.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p.code FROM Price p")
    List<String> findAllPriceCodes();

    Optional<Price> findPriceByCode(String code);
}
