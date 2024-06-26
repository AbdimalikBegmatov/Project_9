package com.example.project_9.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Profile")
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;
    @Column(name = "username",nullable = false)
    private String username;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "isActive")
    private boolean isActive;
    @Column(name = "activationCode")
    private String activationCode;
    @Column(name = "codeConfirmBeginDate")
    private LocalDateTime codeConfirmBeginDate;
    @Column(name = "aboutme")
    private String aboutMe;
    @Column(name = "image",nullable = false)
    private String image;

    @ManyToMany(mappedBy = "profileSave")
    private List<Recipe> savedRecipes;

    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "profile")
    private List<Recipe> recipes;

    public Profile(String username, String email, String password, String activationCode,LocalDateTime codeConfirmBeginDate, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.activationCode = activationCode;
        this.codeConfirmBeginDate = codeConfirmBeginDate;
        this.image = image;
    }

}
