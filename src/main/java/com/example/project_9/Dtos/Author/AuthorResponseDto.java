package com.example.project_9.Dtos.Author;

import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;

import java.util.List;

public record AuthorResponseDto(String Id,
        String username,
         String aboutMe,
         String email,
        String image,
        Integer followers,
        Integer following,
        Integer recipeCount,
        List<RecipeResponseToProfilePage>recipes) {
}
