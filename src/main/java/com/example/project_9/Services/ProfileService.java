package com.example.project_9.Services;

import com.example.project_9.Dtos.Author.AuthorResponseDto;
import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Profile.ProfileRequestDto;
import com.example.project_9.Dtos.Profile.ProfileResponseDto;
import com.example.project_9.Dtos.Profile.ProfileToSearchResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Entity.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService extends UserDetailsService {
    Profile create(ProfileRequestDto profileRequestDto);
    ProfileResponseDto getProfile();
    HttpStatus newEmailConfirmUrl();
    HttpStatus confirm_email(String id);
    ProfileResponseDto editProfile(String name, String bio, MultipartFile image);
    DataResponseDTO<RecipeResponseToProfilePage> getToProfileSavedPage(int page);
    AuthorResponseDto getAuthor(String id);
    HttpStatus follow(String id);

    HttpStatus notFollow(String id);

    List<ProfileToSearchResponseDTO> searchByProfile(String name);
}
