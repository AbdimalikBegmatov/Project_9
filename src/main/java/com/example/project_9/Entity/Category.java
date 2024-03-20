package com.example.project_9.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Category")
@Setter
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @OneToMany
    @JoinColumn(name = "recipes")
    @JsonIgnore
    private List<Recipe> recipes;

    public Category(String name) {
        this.name = name;
    }
}
