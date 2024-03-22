package com.example.project_9.Controllers;

import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeRequestDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO;
import com.example.project_9.Services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

//    @GetMapping("/all-recipe/{category_id}")
//    public ResponseEntity<List<Recipe>> getAll(@PathVariable("category_id")Integer id){
//        return ResponseEntity.ok(recipeService.getAll(id));
//    }

    @PostMapping("/create")
    public ResponseEntity<RecipeResponseDto> create(@RequestPart("ObjectRecipe") @Valid RecipeRequestDto requestDto, @RequestPart("image")MultipartFile image){
        return new ResponseEntity<>(recipeService.create(requestDto,image), HttpStatus.CREATED);
    }


    @GetMapping("/like-recipe")
    public ResponseEntity<HttpStatus> likeRecipe(@RequestParam("id") Integer id){
        return ResponseEntity.ok(recipeService.likeRecipe(id));
    }
    @GetMapping("/not-like-recipe")
    public ResponseEntity<HttpStatus> notLikeRecipe(@RequestParam("id") Integer id){
        return ResponseEntity.ok(recipeService.notLikeRecipe(id));
    }
    @GetMapping("/save-recipe")
    public ResponseEntity<HttpStatus> savedRecipe(@RequestParam("id") Integer id){
        return ResponseEntity.ok(recipeService.savedRecipe(id));
    }
    @GetMapping("/not-save-recipe")
    public ResponseEntity<HttpStatus> notSavedRecipe(@RequestParam("id") Integer id){
        return ResponseEntity.ok(recipeService.notSavedRecipe(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<DataResponseDTO<RecipeResponseToProfilePage>> getAll(@RequestParam(value = "page",defaultValue = "1")int page,@RequestParam(value = "category_id",defaultValue = "1") int category_id){
        return new ResponseEntity<>(recipeService.getAll(page,category_id),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> get(@PathVariable("id")int id){
        return new ResponseEntity<>(recipeService.get(id),HttpStatus.OK);
    }

    @GetMapping("/search-recipe")
    public ResponseEntity<List<RecipeResponseToSearchDTO>> searchByRecipe(@RequestParam("search") String name){
        return ResponseEntity.ok(recipeService.searchByRecipe(name));
    }
}
