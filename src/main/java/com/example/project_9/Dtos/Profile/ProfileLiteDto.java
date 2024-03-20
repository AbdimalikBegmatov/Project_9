package com.example.project_9.Dtos.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileLiteDto {
    private String id;
    private String username;
    private String password;

    public ProfileLiteDto(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
