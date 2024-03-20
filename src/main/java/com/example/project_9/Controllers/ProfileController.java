package com.example.project_9.Controllers;

import com.example.project_9.Dtos.Profile.ProfileResponseDto;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
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
}
