package com.example.project_9.Controllers;

import com.example.project_9.Dtos.Category.CategoryRequestDto;
import com.example.project_9.Dtos.Category.CategoryResponseDto;
import com.example.project_9.Entity.Category;
import com.example.project_9.Services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Category>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/get-category/{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(categoryService.getById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequestDto categoryRequestDto){
        return new ResponseEntity<>(categoryService.create(categoryRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id){
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
