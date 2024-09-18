package com.example.server.repositories;

import com.example.server.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("SELECT a.id FROM Attribute a")
    List<String> findAllAttributeIds();
}
