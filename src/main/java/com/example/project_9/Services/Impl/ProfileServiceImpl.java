package com.example.project_9.Services.Impl;

import com.example.project_9.Dtos.Author.AuthorResponseDto;
import com.example.project_9.Dtos.DataResponseDTO;
import com.example.project_9.Dtos.Profile.ProfileLiteDto;
import com.example.project_9.Dtos.Profile.ProfileRequestDto;
import com.example.project_9.Dtos.Profile.ProfileResponseDto;
import com.example.project_9.Dtos.Profile.ProfileToSearchResponseDTO;
import com.example.project_9.Dtos.Recipe.RecipeResponseToProfilePage;
import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Recipe;
import com.example.project_9.Exceptions.CustomException;
import com.example.project_9.Exceptions.CustomNotFoundException;
import com.example.project_9.Repositories.ProfileRepository;
import com.example.project_9.Repositories.RecipeRepository;
import com.example.project_9.Services.CloudinaryService;
import com.example.project_9.Services.ProfileService;
import com.example.project_9.Services.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final RecipeRepository recipeRepository;
    private final CloudinaryService cloudinaryService;
    private final SubscriptionService subscriptionService;
    private final EmailService emailService;
    @Value("${confirm_url}")
    private String urlToConfirm;

    @Value("${pagesize}")
    int pagesize;

    public ProfileServiceImpl(ProfileRepository profileRepository, RecipeRepository recipeRepository, CloudinaryService cloudinaryService, SubscriptionService subscriptionService, EmailService emailService) {
        this.profileRepository = profileRepository;
        this.recipeRepository = recipeRepository;
        this.cloudinaryService = cloudinaryService;
        this.subscriptionService = subscriptionService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        ProfileLiteDto profile = profileRepository.findByEmailLite(username)
                .orElseThrow(
                        ()->
                                new CustomException(
                                        String.format(
                                                "Username with email '%S' not found",
                                                username))
                );

        return new User(profile.getUsername(), profile.getPassword(), Collections.emptyList());
    }
    @Override
    @Transactional
    public Profile create(ProfileRequestDto profileRequestDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String image = "https://res.cloudinary.com/drfnzmkqf/image/upload/v1709743707/istockphoto-1337144146-2048x2048_llzyxc.jpg";

        if (profileRepository.existsByEmail(profileRequestDto.getEmail())){
            throw new CustomException(String.format("this '%s' email already exists",profileRequestDto.getEmail()));
        }
        if (!profileRequestDto.getPassword().equals(profileRequestDto.getConfirmPassword())){
            throw new CustomException("Password are not confirm");
        }

        Map<String,String> codeGeneration = generateActivationCodeAndUrl();

        Profile profile = profileRepository.save(new Profile(
                profileRequestDto.getUsername(),
                profileRequestDto.getEmail(),
                passwordEncoder.encode(profileRequestDto.getPassword()),
                codeGeneration.get("uuid"),
                LocalDateTime.now(),
                image
        ));

        emailService.sendEmail(profile.getEmail(), "Подвердите регистрацию",
                String.format("<p>Перейдите по ссылке для завершение регистрации <a href=%s>Нажмите сюда</a></p>",codeGeneration.get("url")));

        return profile;
    }

    @Override
    public ProfileResponseDto getProfile() {

        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomException("Profile not found"));

        int follower = subscriptionService.findByFollower(profile);
        int following = subscriptionService.findByFollowing(profile);

        return new ProfileResponseDto(
                profile.getId(),
                profile.getUsername(),
                profile.getAboutMe(),
                profile.getEmail(),
                profile.isActive(),
                profile.getImage(),
                follower,
                following,
                profile.getRecipes().size());
    }


    @Transactional
    @Override
    public HttpStatus confirm_email(String id) {
        Profile profile = profileRepository.findByActivationCode(id).orElseThrow(()->new CustomException("Неправильня ссылыка потверждени или ссылка повреждена"));
        if (!profile.getCodeConfirmBeginDate().isBefore(profile.getCodeConfirmBeginDate().plusMinutes(5)) && !profile.getActivationCode().equals(id)){
            throw  new CustomException("Время для потверждение email истек или неправельный ссылка, отправьте заново");
        }
        if (profile.isActive()){
            throw  new CustomException("Вы уже активировали ссылку, нет надобности повторной активации");
        }

        profile.setActive(true);
        profileRepository.save(profile);

        emailService.sendEmail(profile.getEmail(),
                "Подвердите регистрацию",
                "Вы успешно завершили подверждение");

        return HttpStatus.OK;
    }

    @Transactional
    @Override
    public ProfileResponseDto editProfile(String name, String bio, MultipartFile image) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();



        if (email == null){
            throw new CustomException("You must authentication");
        }

        Profile result = profileRepository.findByEmail(email).orElseThrow(()-> new CustomNotFoundException("Profile not found"));

        int follower = subscriptionService.findByFollower(result);
        int following = subscriptionService.findByFollowing(result);

        String imageUrl;

        try {
            imageUrl = cloudinaryService.uploadImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        result.setImage(imageUrl);
        result.setUsername(name);
        result.setAboutMe(bio);

        Profile profile = profileRepository.save(result);

        return new ProfileResponseDto(
                profile.getId(),
                profile.getUsername(),
                profile.getAboutMe(),
                profile.getEmail(),
                profile.isActive(),
                profile.getImage(),
                follower,
                following,
                profile.getRecipes().size());
    }

    @Override
    public DataResponseDTO<RecipeResponseToProfilePage> getToProfileSavedPage(int page) {
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        Page<Recipe> recipe = recipeRepository.findByProfileSave(profile, PageRequest.of(page-1,pagesize, Sort.by("likes")));

        List<RecipeResponseToProfilePage> response = new ArrayList<>();

        recipe.getContent().forEach(r-> response.add(new RecipeResponseToProfilePage(
                r.getId(),
                r.getName(),
                r.getImage(),
                r.getProfile().getUsername(),
                r.getLikes().size(),
                r.getProfileSave().size()
        )));

        return new DataResponseDTO<RecipeResponseToProfilePage>(
                recipe.getTotalPages(),
                page,
                recipe.hasPrevious(),
                recipe.hasNext(),
                response
        );
    }

    @Override
    public AuthorResponseDto getAuthor(String id) {
        Profile profile = profileRepository.findByIdWithRecipe(id).orElseThrow(()->new CustomNotFoundException("Author not found"));
        int follower = subscriptionService.findByFollower(profile);
        int following = subscriptionService.findByFollowing(profile);

        List<RecipeResponseToProfilePage> recipes = new ArrayList<>();

        profile.getRecipes().forEach(r->{
            recipes.add(new RecipeResponseToProfilePage(
                    r.getId(),
                    r.getName(),
                    r.getImage(),
                    profile.getUsername(),
                    r.getLikes().size(),
                    r.getProfileSave().size()
            ));
        });
        return new AuthorResponseDto(
                profile.getId(),
                profile.getUsername(),
                profile.getAboutMe(),
                profile.getEmail(),
                profile.getImage(),
                follower,
                following,
                profile.getRecipes().size(),
                recipes
        );
    }

    @Override
    @Transactional
    public HttpStatus follow(String id) {

        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        Profile profileFollow = profileRepository.findByIdString(id).orElseThrow(()->new CustomNotFoundException("Profile to follow not found"));



        if (profile.getId().equals(profileFollow.getId())){
            throw  new CustomException("Profile not be equals");
        }

        return subscriptionService.addSubscription(profile,profileFollow);
    }

    @Override
    @Transactional
    public HttpStatus notFollow(String id) {
        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new CustomNotFoundException("Profile not found"));
        Profile profileFollow = profileRepository.findByIdString(id).orElseThrow(()->new CustomNotFoundException("Profile to follow not found"));

        if (profile.getId().equals(profileFollow.getId())){
            throw  new CustomException("Profile not be equals");
        }

        return subscriptionService.removeSubscription(profile,profileFollow);
    }

    @Override
    public List<ProfileToSearchResponseDTO> searchByProfile(String name) {
        return profileRepository.getByName(name);
    }

    @Transactional
    @Override
    public HttpStatus newEmailConfirmUrl() {

        Profile profile = profileRepository.findByEmail(SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName()).orElseThrow(()-> new CustomException("Приозошла ошибка при отправление повторного подверждение email"));
        if (profile.isActive()){
            throw  new CustomException("Вы уже активировали ссылку, нет надобности повторной активации");
        }
        Map<String,String> codeGeneration = generateActivationCodeAndUrl();
        profile.setActivationCode(codeGeneration.get("uuid"));
        profile.setCodeConfirmBeginDate(LocalDateTime.now());

        Profile result = profileRepository.save(profile);


        emailService.sendEmail(result.getEmail(),
                "Подвердите регистрацию",
                String.format("<p>Перейдите по ссылке для завершение регистрации <a href=%s>Нажмите сюда</a></p>",codeGeneration.get("url")));


        return HttpStatus.OK;
    }



    public Map<String,String> generateActivationCodeAndUrl(){
        Map<String,String> result = new HashMap<>();

        String uuid = UUID.randomUUID().toString();
        String url = urlToConfirm+uuid;

        result.put("uuid",uuid);
        result.put("url",url);

        return result;
    }


}
