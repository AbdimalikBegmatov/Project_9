package com.example.project_9.Services.Impl;

import com.example.project_9.Dtos.Authentication.AuthenticationRequest;
import com.example.project_9.Dtos.Authentication.AuthenticationResponse;
import com.example.project_9.Dtos.Profile.ProfileRequestDto;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Token;
import com.example.project_9.Exceptions.CustomException;
import com.example.project_9.Repositories.ProfileRepository;
import com.example.project_9.Repositories.TokenRepository;
import com.example.project_9.Services.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthenticationService {
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;


    public AuthenticationService(ProfileService profileService, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.profileRepository = profileRepository;
    }
    @Transactional
    public AuthenticationResponse register(ProfileRequestDto profileRequestDto){

        Profile profile = profileService.create(profileRequestDto);

        String token = jwtService.generateToken(new User(
                profile.getEmail(),
                profile.getPassword(),
                Collections.emptyList()));

        String refreshToken = jwtService.generateRefreshToken(new User(
                        profile.getEmail(),
                profile.getPassword(),
                        Collections.emptyList()));

        tokenRepository.save(new Token(
                token,
                false,
                false,
                profile
        ));
        return new AuthenticationResponse(token,refreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        Profile profile = profileRepository.findByEmail(request.getEmail()).orElseThrow(()->
                new CustomException("User not found")
        );

        String token = jwtService.generateToken(new User(profile.getEmail(), profile.getPassword(), Collections.emptyList()));
        String refreshToken = jwtService.generateRefreshToken(new User(profile.getEmail(), profile.getPassword(),Collections.emptyList()));

        revokeAllUserToken(profile);
        tokenRepository.save(new Token(token,false,false,profile));

        return new AuthenticationResponse(token,refreshToken);
    }


    public AuthenticationResponse refreshToken(String refreshToken) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals("anonymousUser")){
            throw new CustomException("Need authentication to take refresh token");
        }

        if (!refreshToken.startsWith("Bearer ")){
            throw new CustomException("This is not token");
        }

        String token = refreshToken.substring(7);

        String userEmail = jwtService.getUserName(token);

        if (userEmail == null) {
            throw new CustomException("Wrong refresh token");
        }

        var profile = profileRepository.findByEmail(userEmail)
                .orElseThrow(()->
                        new CustomException("User not found from refresh token"));

        if (!jwtService.isTokenValid(token, new User(profile.getEmail(), profile.getPassword(), Collections.emptyList()))) {
            throw new CustomException("Refresh token incorrect");
        }

        var accessToken = jwtService.generateToken(new User(profile.getEmail(), profile.getPassword(), Collections.emptyList()));
        revokeAllUserToken(profile);
        tokenRepository.save(new Token(accessToken,false,false,profile));

        return new AuthenticationResponse(accessToken,token);
    }


    public void revokeAllUserToken(Profile profile){

        var userTokens = tokenRepository.findAllValidTokenByProfile(profile.getId());

        if (userTokens.isEmpty()) {
            return;
        }
        userTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(userTokens);
    }

    public HttpStatus logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null){
            Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()-> new CustomException("User not Authorization"));
            revokeAllUserToken(profile);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return HttpStatus.OK;
    }
}
