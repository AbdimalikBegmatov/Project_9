package com.example.project_9.Dtos.Ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientRequestDto {
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String weight;

    public IngredientRequestDto(String name, String weight) {
        this.name = name;
        this.weight = weight;
    }
}
