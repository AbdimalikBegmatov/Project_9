package com.example.project_9.Services;

import com.example.project_9.Dtos.Category.CategoryRequestDto;
import com.example.project_9.Dtos.Category.CategoryResponseDto;
import com.example.project_9.Entity.Category;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(Integer id);

    Category create(CategoryRequestDto categoryRequestDto);

    HttpStatus delete(Integer id);
}
