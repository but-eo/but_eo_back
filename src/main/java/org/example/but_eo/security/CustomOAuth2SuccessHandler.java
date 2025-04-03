package org.example.but_eo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.but_eo.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        String accessToken = jwtUtil.generateAccessToken(String.valueOf(email.hashCode()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(email.hashCode()));

        System.out.println(accessToken);
        System.out.println(refreshToken);

        response.sendRedirect("/login/success?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
    }
}

