package com.example.project_9.Dtos.Recipe;

import com.example.project_9.Entity.Ingredient;

import java.util.List;

public record RecipeResponseDto(
        Integer id,
        String name,
        String description,
        String image,
        String cookingTime,
        String difficult,
        List<Ingredient> ingredient,
        String category,
        int likes,
        String profile) {
}
