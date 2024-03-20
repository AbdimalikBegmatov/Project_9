package com.example.project_9.Dtos.Profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {
    @Size(min = 3,max = 30, message = "Username must be from 3 to 30 characters")
    private String username;
    @Email
    private String email;
    @Pattern.List({
            @Pattern(regexp = ".{4,15}", message = "from 4 to 15 characters"),
            @Pattern(regexp = ".*[0-9].*", message = "Minimum 1 digit"),
            @Pattern(regexp = ".*[@#$%^&+=!]", message = "Minimum 1 special character (!,',#,$...)"),
            @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z]).*", message = "Lowercase and uppercase letters")
    })
    private String password;
    @NotBlank(message = "field can`t be empty")
    @NotNull(message = "field can`t be null")
    private String confirmPassword;

    public ProfileRequestDto(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
