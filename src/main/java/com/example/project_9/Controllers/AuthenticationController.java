package com.example.project_9.Controllers;

import com.example.project_9.Dtos.Authentication.AuthenticationRequest;
import com.example.project_9.Dtos.Authentication.AuthenticationResponse;
import com.example.project_9.Dtos.Profile.ProfileRequestDto;
import com.example.project_9.Exceptions.CustomException;
import com.example.project_9.Services.Impl.AuthenticationService;
import com.example.project_9.Services.ProfileService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ProfileService profileService;

    public AuthenticationController(AuthenticationService authenticationService, ProfileService profileService) {
        this.authenticationService = authenticationService;
        this.profileService = profileService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid ProfileRequestDto profileRequestDto){
        return ResponseEntity.ok(authenticationService.register(profileRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestHeader(value = "Authorization",required = false)String request) throws IOException {
        if (request == null)
        {
            throw new CustomException("Need authentication");
        }
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout(){
        return ResponseEntity.ok(authenticationService.logout());
    }

    @GetMapping("/email-confirm/{id}")
    public ResponseEntity<HttpStatus> emailConfirm(@PathVariable("id") String id){
        return new ResponseEntity<>(profileService.confirm_email(id),HttpStatus.OK);
    }

    @GetMapping("/new_email-confirm")
    public ResponseEntity<HttpStatus> emailConfirm(){
        return new ResponseEntity<>(profileService.newEmailConfirmUrl(),HttpStatus.OK);
    }
}
