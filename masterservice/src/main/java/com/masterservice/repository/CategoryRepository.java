package com.masterservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.masterservice.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByCategoryNameIgnoreCase(String categoryName);

    @Query("SELECT c FROM Category c WHERE c.status = ?1 ORDER BY c.id DESC")
    List<Category> findByStatusOrderByidDesc(int status);

}
