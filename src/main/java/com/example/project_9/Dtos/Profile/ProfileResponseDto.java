package com.example.project_9.Dtos.Profile;

import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDto {
    private String Id;
    private String username;
    private String aboutMe;
    private String email;
    private boolean isActive;
    private String image;
    private Integer followers;
    private Integer following;
    private Integer recipeCount;

    public ProfileResponseDto(String id, String username, String aboutMe, String email, boolean isActive, String image, Integer followers, Integer following, Integer recipeCount) {
        Id = id;
        this.username = username;
        this.aboutMe = aboutMe;
        this.email = email;
        this.isActive = isActive;
        this.image = image;
        this.followers = followers;
        this.following = following;
        this.recipeCount = recipeCount;
    }


}
