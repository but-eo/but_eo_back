package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.but_eo.dto.KakaoLoginDto;
import org.example.but_eo.dto.UserLoginRequestDto;
import org.example.but_eo.dto.UserLoginResponseDto;
import org.example.but_eo.dto.UserRegisterRequestDto;
import org.example.but_eo.entity.Users;
import org.example.but_eo.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.UUID;

import java.time.LocalDateTime;
import java.util.Map;

import org.example.but_eo.util.JwtUtil;
import org.example.but_eo.repository.UsersRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UsersController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequestDto dto) {
        usersService.registerUser(dto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto dto) {
//        log.info("로그인 요청 들어옴 : 이메일 = " +dto.getEmail());
        System.out.println("로그인 요청 들어옴 : 이메일 = " + dto.getEmail());
        UserLoginResponseDto response = usersService.login(dto);
//        log.info("로그인 응답 보냄 : " + response);
        System.out.println("로그인 응답 보냄 : " + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<String> kakaologin(@RequestBody KakaoLoginDto kakaoLoginDto) {
        try {
            String userHashId = UUID.randomUUID().toString();
            String userPassword = UUID.randomUUID().toString();
            Users existingUser = usersRepository.findByEmail(kakaoLoginDto.getEmail());
            if (existingUser == null) {
                Users newUser = new Users();
                newUser.setUserHashId(userHashId);
                newUser.setName(kakaoLoginDto.getNickName());
                newUser.setPassword(userPassword);
                newUser.setEmail(kakaoLoginDto.getEmail());
                newUser.setProfile(kakaoLoginDto.getProfileImage());
                newUser.setGender(kakaoLoginDto.getGender());
                newUser.setBirth(kakaoLoginDto.getBirthYear());
                newUser.setRefreshToken(kakaoLoginDto.getRefreshToken());
                newUser.setDivision(Users.Division.USER);
                newUser.setState(Users.State.ACTIVE);
                newUser.setCreatedAt(LocalDateTime.now());
                usersRepository.save(newUser);
            }
            else { // 기존 사용자가 있으면 정보 업데이트
                existingUser.setName(kakaoLoginDto.getNickName()); // 닉네임 변경 가능
                existingUser.setProfile(kakaoLoginDto.getProfileImage());
                existingUser.setRefreshToken(kakaoLoginDto.getRefreshToken());

                usersRepository.save(existingUser);
            }

            System.out.println("카카오 로그인 요청 수신 : " + kakaoLoginDto);
            return ResponseEntity.ok("로그인 성공 : " + kakaoLoginDto.getNickName());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패");
        }
    }

    @GetMapping("/my-info")
    public ResponseEntity<String> myInfo(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok("현재 로그인된 사용자 ID: " + userId);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Refresh Token이 유효하지 않습니다.");
        }

        String userId = jwtUtil.getUserIdFromToken(token);

        Users user = usersRepository.findByUserHashId(userId);
        if (user == null || !token.equals(user.getRefreshToken())) {
            return ResponseEntity.status(401).body("Refresh Token이 일치하지 않습니다.");
        }

        String newAccessToken = jwtUtil.generateAccessToken(userId);

        return ResponseEntity.ok().body(Map.of(
                "accessToken", newAccessToken
        ));
    }

    @RestController
    @RequestMapping("/oauth2")
    public class OAuth2Controller {
        @GetMapping("/success")
        public String oauthLoginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
            return "소셜 로그인 성공! 유저 이름: " + oAuth2User.getAttribute("name");
        }
    }


}
