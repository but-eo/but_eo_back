package org.example.but_eo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.but_eo.dto.UserLoginRequestDto;
import org.example.but_eo.dto.UserLoginResponseDto;
import org.example.but_eo.dto.UserRegisterRequestDto;
import org.example.but_eo.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UsersController {

    private final UsersService usersService;

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

}
