package com.example.project_9.Repositories;

import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Integer> {
    @Query("select r from Recipe r where r.profile.Id = :id order by size(r.likes) desc")
    Page<Recipe> findByProfileIdWithSort(@Param("id")String id, Pageable pageable);
    Page<Recipe> findByProfileSave(Profile profile, Pageable pageable);
    @Query("select r from Recipe r where r.category.id = :category_id order by size(r.likes) desc")
    Page<Recipe> findAllBySortedByLikesAndCategoryId(@Param("category_id") int category_id,Pageable pageable);
    @Query("select new com.example.project_9.Dtos.Recipe.RecipeResponseToSearchDTO(r.id,r.name,r.image) from Recipe r where r.name like :name%")
    List<RecipeResponseToSearchDTO> getByName(@Param("name")String name);
}
