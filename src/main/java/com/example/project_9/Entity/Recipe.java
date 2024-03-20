package com.example.project_9.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @OneToMany
    @JoinColumn(name = "ingredients",nullable = false)
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "category",nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "profile_id")}
    )
    private Set<Recipe> likes;

    @ManyToOne
    @JoinColumn(name = "profile")
    private Profile profile;
}
