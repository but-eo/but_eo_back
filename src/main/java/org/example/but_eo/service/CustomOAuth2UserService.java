package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.UsersRepository;
import org.example.but_eo.util.JwtUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao/naver
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String name = null;
        String profileImg = null;
        String gender = null;

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            name = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
            profileImg = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("profile_image_url");
            gender = (String) kakaoAccount.get("gender");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
            profileImg = (String) response.get("profile_image");
            gender = (String) response.get("gender");
        }

        Users user = usersRepository.findByEmail(email);
        if (user == null) {
            user = new Users();
            user.setUserHashId(String.valueOf(email.hashCode()));
            user.setEmail(email);
            user.setName(name);
            user.setProfile(profileImg);
            user.setGender(gender);
            user.setState(Users.State.ACTIVE);
            user.setDivision(Users.Division.USER);
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(""); // 비밀번호 없이 가입
            user.setTel("소셜로그인"); // placeholder

            usersRepository.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }
}
