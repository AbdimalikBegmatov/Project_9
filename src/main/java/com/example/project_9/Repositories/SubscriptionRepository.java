package com.example.project_9.Repositories;

import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    boolean existsByFollowerAndFollowing(Profile follower,Profile following);
    Subscription findByFollowerAndFollowing(Profile follower,Profile following);
    Integer countByFollower(Profile profile);
    Integer countByFollowing(Profile profile);
}
