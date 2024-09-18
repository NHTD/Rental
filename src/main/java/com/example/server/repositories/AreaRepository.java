package com.example.server.repositories;

import com.example.server.models.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query("SELECT a.code FROM Area a")
    List<String> findAllAreaCodes();

    Optional<Area> findAreaByCode(String code);
}
