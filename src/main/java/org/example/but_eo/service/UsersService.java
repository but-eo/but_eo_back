package org.example.but_eo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.example.but_eo.dto.UserLoginRequestDto;
import org.example.but_eo.dto.UserLoginResponseDto;
import org.example.but_eo.dto.UserRegisterRequestDto;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.UsersRepository;
import org.example.but_eo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void registerUser(UserRegisterRequestDto dto) {
        // 이메일 중복 확인
        if (usersRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 확인
//        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }

        // TODO: 전화번호 인증 코드 검증 로직 추가 필요

        Users user = new Users();
        user.setUser_hash_id(generateUserHash(dto.getEmail())); // 예: SHA-256 등
        user.setState(Users.State.ACTIVE);
        user.setDivision(Users.Division.USER);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setTel(dto.getTel());
        user.setGender(dto.getGender());
        user.setPrefer_sports(dto.getPreferSports());
        user.setBirth(dto.getBirthYear());
        user.setRegion(dto.getRegion());
        user.setCreated_at(LocalDateTime.now());

        usersRepository.save(user);

        System.out.println(user.getEmail() + "로 회원가입에 성공했습니다.");
    }

    private String generateUserHash(String email) {
        // TODO: SHA-256이나 UUID 등으로 유저 해시 ID 생성
        return String.valueOf(email.hashCode()); // 간단 예시
    }

    public UserLoginResponseDto login(UserLoginRequestDto dto) {
        Users user = usersRepository.findByEmail(dto.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUser_hash_id());

        return new UserLoginResponseDto(token, user.getName());
    }

}
