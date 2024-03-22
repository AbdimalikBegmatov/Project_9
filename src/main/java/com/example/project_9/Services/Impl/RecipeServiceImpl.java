package com.example.project_9.Services.Impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeRequestDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseDto;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO;
import com.example.project_9.Entity.Category;
import com.example.project_9.Entity.Ingredient;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Recipe;
import com.example.project_9.Exceptions.CustomException;
import com.example.project_9.Exceptions.CustomNotFoundException;
import com.example.project_9.Repositories.CategoryRepository;
import com.example.project_9.Repositories.IngredientsRepository;
import com.example.project_9.Repositories.ProfileRepository;
import com.example.project_9.Repositories.RecipeRepository;
import com.example.project_9.Services.CloudinaryService;
import com.example.project_9.Services.ProfileService;
import com.example.project_9.Services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;
    private final ProfileRepository profileRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientsRepository ingredientsRepository;

    @Value("${pagesize}")
    int pagesize;

    public RecipeServiceImpl(RecipeRepository recipeRepository, CloudinaryService cloudinaryService, ProfileRepository profileRepository, CategoryRepository categoryRepository, IngredientsRepository ingredientsRepository) {
        this.recipeRepository = recipeRepository;
        this.cloudinaryService = cloudinaryService;
        this.profileRepository = profileRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientsRepository = ingredientsRepository;
    }

    @Override
    public RecipeResponseDto create(RecipeRequestDto recipeRequestDto, MultipartFile image) {

        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("User not found"));
        Category category = categoryRepository.findById(recipeRequestDto.getCategory_id()).orElseThrow(()->new CustomNotFoundException("Category not found"));

        List<Ingredient> ingredientList = recipeRequestDto.getIngredients().stream().map(data->new Ingredient(
                data.getName(),
                data.getWeight()
        )).toList();

        List<Ingredient> ingredients = ingredientsRepository.saveAll(ingredientList);

        String imageUrl;

        try {
            imageUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Recipe recipe = recipeRepository.save(new Recipe(
                recipeRequestDto.getName(),
                recipeRequestDto.getDescription(),
                imageUrl,
                recipeRequestDto.getCookingTime(),
                recipeRequestDto.getDifficulty(),
                ingredients,
                category,
                profile
        ));

        List<Profile> likes = recipe.getLikes();
        int likesCount = (likes != null) ? likes.size() : 0;

        return new RecipeResponseDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getImage(),
                recipeRequestDto.getCookingTime(),
                recipe.getDifficulty().name(),
                recipe.getIngredients(),
                recipe.getCategory().getName(),
                likesCount,
                recipe.getProfile().getUsername()
        );
    }

    @Override
    public DataResponseDTO<RecipeResponseToProfilePage> getToProfilePage(int pageNumber) {

        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("User not found"));
        Page<Recipe> recipe = recipeRepository.findByProfileIdWithSort(profile.getId(), PageRequest.of(pageNumber-1,pagesize));

        List<RecipeResponseToProfilePage> response = new ArrayList<>();

        recipe.getContent().forEach(r-> response.add(new RecipeResponseToProfilePage(
                r.getId(),
                r.getName(),
                r.getImage(),
                r.getProfile().getUsername(),
                r.getLikes().size(),
                r.getProfileSave().size()
        )));

        return new DataResponseDTO<RecipeResponseToProfilePage>(
                recipe.getTotalPages(),
                pageNumber,
                recipe.hasPrevious(),
                recipe.hasNext(),
                response
        );
    }

    @Override
    @Transactional
    public HttpStatus likeRecipe(Integer id) {

        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Recipe not found"));
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        if (recipe.getLikes().contains(profile)){
            throw new CustomException("you`re already like");
        }
        recipe.setLikes(profile);

        recipeRepository.save(recipe);

        return HttpStatus.OK;
    }

    @Override
    @Transactional
    public HttpStatus savedRecipe(Integer id){

        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Recipe not found"));
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));

        if (recipe.getProfileSave().contains(profile)){
            throw new CustomException("you`re already saved");
        }

        recipe.setProfileSave(profile);

        recipeRepository.save(recipe);

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus notLikeRecipe(Integer id) {
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Recipe not found"));

        if (recipe.getLikes().contains(profile)){
            recipe.getLikes().remove(profile);
            recipeRepository.save(recipe);
        }
        else {
            throw new CustomException("You`re not liked before");
        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus notSavedRecipe(Integer id) {
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Recipe not found"));

        if (recipe.getProfileSave().contains(profile)){
            recipe.getProfileSave().remove(profile);
            recipeRepository.save(recipe);
        }
        else {
            throw new CustomException("You`re not saved before");
        }

        return HttpStatus.OK;
    }

    @Override
    public DataResponseDTO<RecipeResponseToProfilePage> getAll(int page,int category_id) {
        Page<Recipe> recipe = recipeRepository.findAllBySortedByLikesAndCategoryId(category_id,PageRequest.of(page-1,pagesize));

        List<RecipeResponseToProfilePage> response = new ArrayList<>();

        recipe.getContent().forEach(r-> response.add(new RecipeResponseToProfilePage(
                r.getId(),
                r.getName(),
                r.getImage(),
                r.getProfile().getUsername(),
                r.getLikes().size(),
                r.getProfileSave().size()
        )));

        return new DataResponseDTO<RecipeResponseToProfilePage>(
                recipe.getTotalPages(),
                page,
                recipe.hasPrevious(),
                recipe.hasNext(),
                response
        );
    }

    @Override
    public RecipeResponseDto get(int id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Recipe not found"));

        List<Profile> likes = recipe.getLikes();
        int likesCount = (likes != null) ? likes.size() : 0;

        return new RecipeResponseDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getImage(),
                recipe.getCookingTime(),
                recipe.getDifficulty().name(),
                recipe.getIngredients(),
                recipe.getCategory().getName(),
                likesCount,
                recipe.getProfile().getUsername()
        );
    }

    @Override
    public List<RecipeResponseToSearchDTO> searchByRecipe(String name) {
        return recipeRepository.getByName(name);
    }
}
