package com.example.project_9.Dtos.Recipe;

public record RecipeResponseToProfilePage(
        Integer id,
        String name,
        String image,
        String author,
        int likes,
        int saves
) {
}
