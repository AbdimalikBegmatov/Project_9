package com.example.project_9.Services;

import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeRequestDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO;
import com.example.project_9.Entity.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {

    RecipeResponseDto create(RecipeRequestDto recipeRequestDto, MultipartFile image);

    DataResponseDTO<RecipeResponseToProfilePage> getToProfilePage(int pageNumber);

    HttpStatus likeRecipe(Integer id);
    HttpStatus savedRecipe(Integer id);

    HttpStatus notLikeRecipe(Integer id);

    HttpStatus notSavedRecipe(Integer id);

    DataResponseDTO<RecipeResponseToProfilePage> getAll(int page,int category_id);

    RecipeResponseDto get(int id);

    List<RecipeResponseToSearchDTO> searchByRecipe(String name);
}
