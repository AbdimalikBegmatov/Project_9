package com.example.project_9.Dtos.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryRequestDto {
    @NotNull
    @NotBlank
    private String name;

    public CategoryRequestDto(String name) {
        this.name = name;
    }
}
