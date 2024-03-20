package com.example.project_9.Repositories;

import com.example.project_9.Dtos.Category.CategoryResponseDto;
import com.example.project_9.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select new com.example.project_9.Dtos.Category.CategoryResponseDto(c.id,c.name) from Category c")
    List<CategoryResponseDto> findAllToDto();
}
