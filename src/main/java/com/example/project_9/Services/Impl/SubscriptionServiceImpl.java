package com.example.project_9.Services.Impl;

import com.example.project_9.Entity.Profile;
import com.example.project_9.Entity.Subscription;
import com.example.project_9.Exceptions.CustomException;
import com.example.project_9.Repositories.SubscriptionRepository;
import com.example.project_9.Services.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    @Transactional
    public HttpStatus addSubscription(Profile follower, Profile following) {
        if (subscriptionRepository.existsByFollowerAndFollowing(follower,following)){
            throw new CustomException("Already follow");
        }
        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowing(following);
        subscriptionRepository.save(subscription);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus removeSubscription(Profile follower, Profile following) {
        Subscription subscription = subscriptionRepository.findByFollowerAndFollowing(follower,following);
        if (subscription == null){
            throw new CustomException("You are not follow");
        }

        subscriptionRepository.delete(subscription);

        return HttpStatus.OK;
    }

    @Override
    public Integer findByFollower(Profile follower) {
        return subscriptionRepository.countByFollower(follower);
    }

    @Override
    public Integer findByFollowing(Profile following) {
        return subscriptionRepository.countByFollowing(following);
    }
}
