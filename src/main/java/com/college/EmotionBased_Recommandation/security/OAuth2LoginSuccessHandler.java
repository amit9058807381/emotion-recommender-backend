package com.college.EmotionBased_Recommandation.security;

import com.college.EmotionBased_Recommandation.entity.User;
import com.college.EmotionBased_Recommandation.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub"); // Google's unique user ID

        // Find existing user by email, or create a new one
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setAuthProvider("GOOGLE");
            user.setProviderId(googleId);
            userRepository.save(user);
        }

        // Issue our own JWT (so all future API calls work the same way, regardless of login method)
        String token = jwtUtil.generateToken(user.getEmail());

        // Redirect back to frontend with the token (adjust URL once React is ready)
        response.sendRedirect("http://localhost:5173/oauth-success?token=" + token + "&userId=" + user.getId() + "&username=" + user.getUsername());
    }
}