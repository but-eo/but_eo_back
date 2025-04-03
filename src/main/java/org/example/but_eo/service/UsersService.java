package org.example.but_eo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.but_eo.dto.*;
import org.example.but_eo.entity.TeamMember;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.*;
import org.example.but_eo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final ChattingMemberRepository chattingMemberRepository;
    private final NotificationRepository notificationRepository;
    private final FileRepository fileRepository;

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
        user.setUserHashId(generateUserHash(dto.getEmail())); // 예: SHA-256 등
        user.setState(Users.State.ACTIVE);
        user.setDivision(Users.Division.USER);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setTel(dto.getTel());
        user.setGender(dto.getGender());
        user.setPreferSports(dto.getPreferSports());
        user.setBirth(dto.getBirthYear());
        user.setRegion(dto.getRegion());
        user.setCreatedAt(LocalDateTime.now());

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

        //  상태 확인
        if (user.getState() != Users.State.ACTIVE) {
            throw new IllegalStateException("현재 탈퇴 대기 중이거나 비활성화된 계정입니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUserHashId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserHashId());

        user.setRefreshToken(refreshToken);
        usersRepository.save(user);

        return new UserLoginResponseDto(accessToken, refreshToken, user.getName());
    }

    //유저 업데이트 로직
    @Transactional
    public void updateUser(String userId, UserUpdateRequestDto dto) {
        Users user = usersRepository.findByUserHashId(userId);

        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getRegion() != null) user.setRegion(dto.getRegion());
        if (dto.getPreferSports() != null) user.setPreferSports(dto.getPreferSports());
        if (dto.getTel() != null) user.setTel(dto.getTel());
        if (dto.getGender() != null) user.setGender(dto.getGender());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getProfile() != null && !dto.getProfile().isEmpty()) {
            String newProfileUrl = saveProfileImage(dto.getProfile());
            user.setProfile(newProfileUrl);
        }

        usersRepository.save(user);
    }

    //이미지 저장 로직
    private String saveProfileImage(MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/profiles/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());

            return "/uploads/profiles/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }

    //State change to DELETED_WAIT
    @Transactional
    public void deleteUser(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        // 팀 리더인지 확인
        List<TeamMember> memberList = teamMemberRepository.findAllByUser(user);
        for (TeamMember member : memberList) {
            if (member.getType() == TeamMember.Type.LEADER) {
                throw new IllegalStateException("팀 리더는 계정을 삭제할 수 없습니다. 리더 위임 후 진행해주세요.");
            }
        }

        // 실제 삭제 or soft delete (상태만 변경) 일단은 삭제 대기로
        user.setState(Users.State.DELETED_WAIT);
        usersRepository.save(user);

        System.out.println(user.getEmail() + " 계정 탈퇴 처리됨.");
    }

    //영구 삭제 코드, 외래키 제약 조건 때문에 아직은 미완 고민좀 해봐야할듯
    @Transactional
    public void deleteUserPermanently(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        if (user.getState() != Users.State.DELETED_WAIT) {
            throw new IllegalStateException("삭제 대기 상태인 유저만 완전 삭제할 수 있습니다.");
        }

        // 팀 멤버 삭제
        teamMemberRepository.deleteAllByUser(user);
        // 댓글 삭제
        commentRepository.deleteAllByUser(user);
        // 게시글 삭제
        boardRepository.deleteAllByUser(user);
        // 채팅 멤버 삭제
        chattingMemberRepository.deleteAllByUser(user);
        // 알림 (보낸/받은) 삭제
        notificationRepository.deleteAllByReceiverUser(user);
        notificationRepository.deleteAllBySenderUser(user);
        // 파일 삭제 (파일 엔티티가 사용자와 연관돼 있을 경우)
        fileRepository.deleteAllByUserHashId(user);

        //유저 삭제
        usersRepository.delete(user);
        System.out.println("유저 완전 삭제 완료: " + user.getEmail());
    }

    //자기 자신 조회
    public UserInfoResponseDto getUserInfo(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("사용자 없음");

        return new UserInfoResponseDto(
                user.getName(),
                user.getEmail(),
                user.getTel(),
                user.getRegion(),
                user.getPreferSports(),
                user.getGender(),
                user.getProfile(),
                user.getBirth(),
                user.getBadmintonScore(),
                user.getTennisScore(),
                user.getTableTennisScore(),
                user.getBowlingScore(),
                user.getCreatedAt()
        );
    }

    public List<UserInfoResponseDto> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserInfoResponseDto convertToDto(Users user) {
        return new UserInfoResponseDto(
                user.getName(),
                user.getEmail(),
                user.getTel(),
                user.getRegion(),
                user.getPreferSports(),
                user.getGender(),
                user.getProfile(),
                user.getBirth(),
                user.getBadmintonScore(),
                user.getTennisScore(),
                user.getTableTennisScore(),
                user.getBowlingScore(),
                user.getCreatedAt()
        );
    }


}
