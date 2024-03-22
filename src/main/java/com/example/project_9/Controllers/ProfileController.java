package com.example.project_9.Controllers;

import com.example.project_9.Dtos.Author.AuthorResponseDto;
import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Profile.ProfileResponseDto;
import com.example.project_9.Dtos.Profile.ProfileToSearchResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Services.ProfileService;
import com.example.project_9.Services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final RecipeService recipeService;

    public ProfileController(ProfileService profileService, RecipeService recipeService) {
        this.profileService = profileService;
        this.recipeService = recipeService;
    }

//    @GetMapping("test")
//    public String test(){
//        return "From profile controller"+ SecurityContextHolder.getContext().getAuthentication().getName();
//    }

    @GetMapping("/get-profile")
    public ResponseEntity<ProfileResponseDto> getProfile(){
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PostMapping("/edit-profile")
    public ResponseEntity<ProfileResponseDto> editProfile(@RequestPart("name")String name, @RequestPart("bio")String bio, @RequestPart("image") MultipartFile image){
        return ResponseEntity.ok(profileService.editProfile(name,bio,image));
    }

    @GetMapping("/get-profile-recipe")
    public ResponseEntity<DataResponseDTO<RecipeResponseToProfilePage>> getProfileRecipe(@RequestParam(value = "page",defaultValue = "1")int page){
        return new ResponseEntity<>(recipeService.getToProfilePage(page), HttpStatus.OK);
    }
    @GetMapping("/get-profile-saved-recipe")
    public ResponseEntity<DataResponseDTO<RecipeResponseToProfilePage>> getProfileSavedRecipe(@RequestParam(value = "page",defaultValue = "1")int page){
        return new ResponseEntity<>(profileService.getToProfileSavedPage(page), HttpStatus.OK);
    }

    @GetMapping("/author")
    public ResponseEntity<AuthorResponseDto> getAuthor(@RequestParam("author_id")String id){
        return ResponseEntity.ok(profileService.getAuthor(id));
    }

    @GetMapping("/follow")
    public ResponseEntity<HttpStatus> follow(@RequestParam("profile_id")String id){
        return ResponseEntity.ok(profileService.follow(id));
    }
    @GetMapping("/not-follow")
    public ResponseEntity<HttpStatus> notFollow(@RequestParam("profile_id")String id){
        return ResponseEntity.ok(profileService.notFollow(id));
    }

    @GetMapping("/search-author")
    public ResponseEntity<List<ProfileToSearchResponseDTO>> searchByRecipe(@RequestParam("search") String name){
        return ResponseEntity.ok(profileService.searchByProfile(name));
    }
}
