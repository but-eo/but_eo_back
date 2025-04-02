package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.UserLoginRequestDto;
import org.example.but_eo.dto.UserLoginResponseDto;
import org.example.but_eo.dto.UserRegisterRequestDto;
import org.example.but_eo.dto.UserUpdateRequestDto;
import org.example.but_eo.entity.Users;
import org.example.but_eo.service.UsersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
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
        System.out.println("로그인 요청 들어옴 : 이메일 = " +dto.getEmail());
        UserLoginResponseDto response = usersService.login(dto);
//        log.info("로그인 응답 보냄 : " + response);
        System.out.println("로그인 응답 보냄 : " + response);
        return ResponseEntity.ok(response);
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

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @ModelAttribute UserUpdateRequestDto request,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        usersService.updateUser(userId, request);

        return ResponseEntity.ok("회원 정보 수정 완료");
    }

    //상태 변경
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        usersService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    //영구 삭제
    @DeleteMapping("/permanent")
    public ResponseEntity<?> deleteUserPermanently(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        usersService.deleteUserPermanently(userId);
        return ResponseEntity.ok("계정이 완전히 삭제되었습니다.");
    }


}
