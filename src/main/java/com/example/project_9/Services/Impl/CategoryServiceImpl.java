package com.example.project_9.Services.Impl;

import com.example.project_9.Dtos.Category.CategoryRequestDto;
import com.example.project_9.Dtos.Category.CategoryResponseDto;
import com.example.project_9.Entity.Category;
import com.example.project_9.Exceptions.CustomNotFoundException;
import com.example.project_9.Repositories.CategoryRepository;
import com.example.project_9.Services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Category is not found"));
    }

    @Override
    public Category create(CategoryRequestDto categoryRequestDto) {
        return categoryRepository.save(new Category(categoryRequestDto.getName()));
    }

    @Override
    public HttpStatus delete(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Category not found"));
        categoryRepository.delete(category);
        return HttpStatus.OK;
    }
}
