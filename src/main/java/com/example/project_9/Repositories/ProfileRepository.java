package com.example.project_9.Repositories;

import com.example.project_9.Dtos.Profile.ProfileLiteDto;
import com.example.project_9.Dtos.Profile.ProfileResponseDto;
import com.example.project_9.Entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    @Query("select new com.example.project_9.Dtos.Profile.ProfileLiteDto(p.Id,p.email,p.password) from Profile p where p.email = :email")
    Optional<ProfileLiteDto> findByEmailLite(@Param("email") String email);
    Optional<Profile> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query("select p from Profile p where p.activationCode = :code")
    Optional<Profile> findByActivationCode(@Param("code") String code);
}
