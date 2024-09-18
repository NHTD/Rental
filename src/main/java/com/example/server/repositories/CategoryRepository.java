package com.example.server.repositories;

import com.example.server.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.code FROM Category c")
    List<String> findAllCategoryCodes();

    Optional<Category> findCategoryByCode(String code);
}
