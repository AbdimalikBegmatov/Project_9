package com.example.project_9.Repositories;

import com.example.project_9.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Query("select t from Token t inner join Profile p on t.profile.Id = p.Id where p.Id = :id and (t.expired=false or t.revoked = false)")
    List<Token> findAllValidTokenByProfile(String id);
    Optional<Token> findByToken(String token);
}
