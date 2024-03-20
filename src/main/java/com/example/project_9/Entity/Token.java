package com.example.project_9.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private boolean revoked;
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Profile profile;

    public Token(String token, boolean revoked, boolean expired, Profile profile) {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.profile = profile;
    }
}
