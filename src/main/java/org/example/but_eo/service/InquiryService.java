package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.example.but_eo.entity.Inquiry;
import org.example.but_eo.entity.InquiryAnswer;
import org.example.but_eo.entity.Users;
import org.example.but_eo.repository.InquiryAnswerRepository;
import org.example.but_eo.repository.InquiryRepository;
import org.example.but_eo.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void createInquiry(String userId, String title, String content, String password) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryId(UUID.randomUUID().toString());
        inquiry.setUser(user);
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setPassword(password);
        inquiry.setVisibility(password != null && !password.isBlank() ? Inquiry.Visibility.PRIVATE : Inquiry.Visibility.PUBLIC);
        inquiry.setCreatedAt(LocalDateTime.now());

        inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getAccessibleInquiries(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");

        if (user.getDivision() == Users.Division.ADMIN) {
            // 관리자는 최신순으로 모든 문의를 볼 수 있도록 수정 (선택 사항)
            return inquiryRepository.findAllByOrderByCreatedAtDesc();
        }

        // 일반 사용자는 공개 문의 + 자신의 문의를 최신순으로 (이 부분은 복잡한 쿼리가 필요할 수 있음)
        // 우선은 기존 로직을 유지하되, getMyInquiries는 확실히 정렬됩니다.
        // 필요하다면 이 부분도 Repository에 별도 메소드 정의 필요
        return inquiryRepository.findAll().stream()
                .filter(i -> i.getVisibility() == Inquiry.Visibility.PUBLIC || i.getUser().getUserHashId().equals(userId))
                // .sorted((i1, i2) -> i2.getCreatedAt().compareTo(i1.getCreatedAt())) // 스트림에서 정렬
                .toList();
    }

    public List<Inquiry> getPublicInquiries() {
        // 공개 문의도 최신순으로 제공 (선택 사항)
        return inquiryRepository.findByVisibilityOrderByCreatedAtDesc(Inquiry.Visibility.PUBLIC);
    }

    public List<Inquiry> getMyInquiries(String userId) {
        Users user = usersRepository.findByUserHashId(userId);
        if (user == null) throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        // *** 여기가 수정된 부분입니다: findByUserOrderByCreatedAtDesc 사용 ***
        return inquiryRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Inquiry getInquiryDetail(String inquiryId, String userId, String passwordFromRequest) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글이 존재하지 않습니다."));

        // 디버깅 로그 추가
        System.out.println("--- Inquiry Detail Check ---");
        System.out.println("Inquiry ID: " + inquiryId);
        System.out.println("Requested by User ID: " + userId);
        System.out.println("Inquiry Visibility: " + inquiry.getVisibility());
        System.out.println("Inquiry Owner ID: " + inquiry.getUser().getUserHashId());
        System.out.println("Inquiry Stored Password: " + inquiry.getPassword());
        System.out.println("Password from Request: " + passwordFromRequest);


        if (inquiry.getVisibility() == Inquiry.Visibility.PUBLIC) {
            System.out.println("Access granted: Public inquiry.");
            return inquiry;
        }

        Users user = null;
        if (userId != null) {
            user = usersRepository.findByUserHashId(userId);
        }

        boolean isAdmin = user != null && user.getDivision() == Users.Division.ADMIN;
        boolean isOwner = user != null && inquiry.getUser().getUserHashId().equals(userId);

        System.out.println("Is Admin: " + isAdmin);
        System.out.println("Is Owner: " + isOwner);


        if (isAdmin || isOwner) {
            System.out.println("Access granted: User is admin or owner.");
            return inquiry;
        }

        if (inquiry.getPassword() != null && inquiry.getPassword().equals(passwordFromRequest)) {
            System.out.println("Access granted: Password matched.");
            return inquiry;
        }
        System.out.println("Access denied: No conditions met. Throwing SecurityException.");
        throw new SecurityException("비공개 글 접근 권한 없음"); // 이 예외는 Spring Security의 AccessDeniedException 등으로 대체하는 것이 더 일반적입니다.
    }

    @Transactional
    public void answerInquiry(String inquiryId, String adminId, String content) {
        Inquiry inquiry = inquiryRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("문의글이 존재하지 않습니다."));

        Users admin = usersRepository.findByUserHashId(adminId);
        if (admin == null || admin.getDivision() != Users.Division.ADMIN) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        // 이미 답변이 있는지 확인하고, 있다면 업데이트, 없다면 새로 생성 (선택적 로직)
        InquiryAnswer answer = inquiry.getAnswer();
        if (answer == null) {
            answer = new InquiryAnswer();
            answer.setAnswerId(UUID.randomUUID().toString());
            answer.setInquiry(inquiry);
            answer.setAdmin(admin);
        } else { // 기존 답변 수정 시 관리자 변경 가능성 고려 (또는 최초 답변자만 수정 가능)
            answer.setAdmin(admin); // 답변자 업데이트
        }
        answer.setContent(content);
        answer.setAnsweredAt(LocalDateTime.now());

        answerRepository.save(answer);
        // inquiry.setAnswer(answer); // Inquiry 엔티티에도 관계 설정 (양방향 시)
        // inquiryRepository.save(inquiry); // answer 저장 시 inquiry도 함께 업데이트 될 수 있음 (cascade 설정에 따라)
    }
}