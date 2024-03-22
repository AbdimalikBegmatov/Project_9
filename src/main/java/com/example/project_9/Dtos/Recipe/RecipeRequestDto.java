package com.example.project_9.Dtos.Recipe;

import com.example.project_9.Dtos.Ingredient.IngredientRequestDto;
import com.example.project_9.Entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeRequestDto {
    private String name;
    private String description;
    private String cookingTime;
    private Difficulty difficulty;
    private List<IngredientRequestDto> ingredients;
    private Integer category_id;
}
