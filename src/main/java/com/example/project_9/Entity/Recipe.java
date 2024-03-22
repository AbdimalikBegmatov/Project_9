package com.example.project_9.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Recipe")
@Setter
@Getter
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",unique = true,nullable = false)
    private String name;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "image",nullable = false)
    private String image;
    @Column(name = "cookingTime",nullable = false)
    private String cookingTime;
    @Column(name = "difficulty",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(
            name = "saved_recipe_profile",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "profile_id")}
    )
    private List<Profile> profileSave;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ingredients")
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "category",nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "likes_table",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "profile_id")}
    )
    private List<Profile> likes;

    @ManyToOne
    @JoinColumn(name = "profile")
    private Profile profile;

    public Recipe(String name, String description, String image, String cookingTime, Difficulty difficulty, List<Ingredient> ingredients, Category category, Profile profile) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.cookingTime = cookingTime;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.category = category;
        this.profile = profile;
    }

    public void setLikes(Profile likes) {
        this.likes.add(likes);
    }

    public void setProfileSave(Profile profileSave) {
        this.profileSave.add(profileSave);
    }
}
