package com.example.project_9.Services;

import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Subscription;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface SubscriptionService {
    HttpStatus addSubscription(Profile follower,Profile following);
    HttpStatus removeSubscription(Profile follower,Profile following);
    Integer findByFollower(Profile follower);
    Integer findByFollowing(Profile following);
}
