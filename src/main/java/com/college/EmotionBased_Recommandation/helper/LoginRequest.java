package com.college.EmotionBased_Recommandation.helper;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LoginRequest {
    private String email;
    private String password;
}