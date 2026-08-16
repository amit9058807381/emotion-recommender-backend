package com.college.EmotionBased_Recommandation.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password; // null rahega agar social login se aaya user hai

    @Column(name = "auth_provider")
    private String authProvider; // "LOCAL", "GOOGLE", "FACEBOOK", "GITHUB"

    @Column(name = "provider_id")
    private String providerId; // Google/Facebook/GitHub ka unique user ID

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EmotionLog> emotionLogs;



}